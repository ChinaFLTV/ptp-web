package com.fltv.web.service.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fltv.web.service.monitor.constants.DockerContainerConstants;
import com.fltv.web.service.monitor.mapper.MySqlMapper;
import com.fltv.web.service.monitor.model.po.DatabaseStatus;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.model.po.TableInfo;
import com.fltv.web.service.monitor.service.MySqlService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.InvocationBuilder;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pfp.fltv.common.model.po.manage.Asset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 4:08:47
 * @description MySQL数据库监控服务接口实现
 * @filename MySqlServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class MySqlServiceImpl extends ServiceImpl<MySqlMapper, Asset> implements MySqlService {


    private final DockerClient dockerClient;
    private final DockerContainerConstants dockerContainerConstants;


    @Override
    public Map<String, Integer> getConnectionsStatistics(@Nonnull @NotNull Long id) {

        Map<String, Integer> map = new HashMap<>();

        map.put("max_connections", baseMapper.getMaxConnections());
        map.put("active_connections", baseMapper.getActiveConnections());

        return map;

    }


    @Override
    public List<ProcessListEntry> getFullProcessListById(@Nonnull Long id) {

        return baseMapper.getFullProcessList();

    }


    @Override
    public Map<String, Object> getStatusById(@Nonnull @NotNull Long id) {


        Map<String, Object> map = new HashMap<>();

        List<DatabaseStatus> databaseStatuses = baseMapper.getStatus();

        if (databaseStatuses != null && !databaseStatuses.isEmpty()) {

            for (DatabaseStatus databaseStatus : databaseStatuses) {

                map.put(databaseStatus.getVariableName(), databaseStatus.getValue());

            }

        }

        return map;

    }


    @Override
    public Map<String, Object> getVariablesById(@Nonnull @NotNull Long id) {

        Map<String, Object> map = new HashMap<>();

        List<DatabaseStatus> variables = baseMapper.getVariables();
        if (variables != null && !variables.isEmpty()) {

            for (DatabaseStatus databaseStatus : variables) {

                map.put(databaseStatus.getVariableName(), databaseStatus.getValue());

            }

        }

        return map;

    }


    @Override
    public Map<String, Object> getBaseStatusById(@Nonnull Long id) throws InterruptedException {

        Map<String, Object> map = new HashMap<>();

        // 2024-12-16  18:06-获取当前指定ID的MySQL数据库的数据库磁盘占用数据(数值单位为Byte)
        InspectContainerResponse response = dockerClient.inspectContainerCmd(dockerContainerConstants.getPtpBackendMySql1ContainerId()).exec();
        if (response == null || response.getSizeRw() == null) {

            // map.put("db_disk_usage", response.getSizeRw() == null ? 0 : response.getSizeRw());
            // 2024-12-17  12:32-由于目前无法审查MySQL容器中的各个挂载数据块的大小总和 , 因此这里取近似值 , 即直接统计当前指定ID的MySQL数据库中的全部数据库表&索引的内存占用大小之和
            Long allTablesTotalSize = baseMapper.getAllTablesTotalSize();
            map.put("db_disk_usage", allTablesTotalSize == null ? 0L : allTablesTotalSize);

        } else {

            map.put("db_disk_usage", response.getSizeRw() == null ? 0 : response.getSizeRw());

        }
        map.put("status", response == null ? "unknown" : response.getState().getStatus()); // 2024-12-17  12:59-记录当前的MySQL数据库容器的健康状态
        map.put("start_at", response == null ? LocalDateTime.now().toString() : response.getState().getStartedAt());

        // 2024-12-16  18:06-获取当前指定ID的MySQL数据库的当前活跃的连接的数量
        map.put("db_connections", Integer.parseInt(baseMapper.getConnectionNum().getValue()));

        // 2024-12-16  18:21-获取当前指定ID的MySQL数据库的查询总数
        map.put("queries", Integer.parseInt(baseMapper.getQueryNum().getValue()));

        Statistics statistics1 = dockerClient.statsCmd(dockerContainerConstants.getPtpBackendMySql1ContainerId())
                .withNoStream(true) // 2024-12-19  20:11-如果不添加此配置 , 则执行会抛出 java.lang.IllegalStateException: Result has already been set 异常 , 虽然这个异常并不会影响到业务逻辑的进行 , 但挺碍眼的
                .exec(new InvocationBuilder.AsyncResultCallback<>())
                .awaitResult();

        Thread.sleep(1000L); // 2024-12-17  11:50-这里之所以需要休眠一秒然后再获取一次数据 , 是因为我们需要根据前后的网络累计接收/发送数据作差以求出网络接收/发送速率

        Statistics statistics2 = dockerClient.statsCmd(dockerContainerConstants.getPtpBackendMySql1ContainerId())
                .withNoStream(true) // 2024-12-19  20:12-如果不添加此配置 , 则执行会抛出 java.lang.IllegalStateException: Result has already been set 异常 , 虽然这个异常并不会影响到业务逻辑的进行 , 但挺碍眼的
                .exec(new InvocationBuilder.AsyncResultCallback<>())
                .awaitResult();

        if (statistics1 != null) {

            // 2024-12-16  18:14-获取当前指定ID的MySQL数据库的CPU占用情况(数值单位为Byte)
            CpuStatsConfig cpuStats = statistics1.getCpuStats();
            if (cpuStats != null) {

                Long onlineCpus = cpuStats.getOnlineCpus();
                CpuUsageConfig cpuUsageConfig = cpuStats.getCpuUsage();

                map.put("online_cpus", onlineCpus == null ? 0 : onlineCpus);
                map.put("per_cpu_usage", cpuUsageConfig == null || cpuUsageConfig.getPercpuUsage() == null ? new ArrayList<>() : cpuUsageConfig.getPercpuUsage());
                map.put("cpu_usage", cpuUsageConfig == null || cpuUsageConfig.getTotalUsage() == null ? 0 : cpuUsageConfig.getTotalUsage());
                map.put("system_cpu_usage", cpuStats.getSystemCpuUsage() == null ? 0 : cpuStats.getSystemCpuUsage());

            }

            // 2024-12-16  18:24-获取当前指定ID的MySQL数据库的内存占用情况(数值单位为Byte)
            MemoryStatsConfig memoryStats = statistics1.getMemoryStats();
            if (memoryStats != null) {

                map.put("memory_usage", memoryStats.getUsage() == null ? 0 : memoryStats.getUsage());
                map.put("memory_max_usage", memoryStats.getMaxUsage() == null ? 0 : memoryStats.getMaxUsage());
                map.put("memory_limit", memoryStats.getLimit() == null ? 0 : memoryStats.getLimit());

            }

            // 2024-12-16  19:59-获取当前指定ID的MySQL数据库的磁盘读/写速率(数值单位为Byte)
            BlkioStatsConfig blkioStats = statistics1.getBlkioStats();
            if (blkioStats != null) {

                Long readBytes = 0L;
                Long writeBytes = 0L;
                Long readOperations = 0L;
                Long writeOperations = 0L;

                if (blkioStats.getIoServiceBytesRecursive() != null && blkioStats.getIoServiceBytesRecursive().size() >= 2) {

                    readBytes = blkioStats.getIoServiceBytesRecursive().get(0).getValue();
                    writeBytes = blkioStats.getIoServiceBytesRecursive().get(1).getValue();

                }

                if (blkioStats.getIoServicedRecursive() != null && blkioStats.getIoServicedRecursive().size() >= 2) {

                    readOperations = blkioStats.getIoServicedRecursive().get(0).getValue();
                    writeOperations = blkioStats.getIoServicedRecursive().get(1).getValue();

                }

                map.put("disk_io_read_bytes", readBytes == null ? 0 : readBytes);
                map.put("disk_io_write_bytes", writeBytes == null ? 0 : writeBytes);
                map.put("disk_io_read_ops", readOperations == null ? 0 : readOperations);
                map.put("disk_io_write_ops", writeOperations == null ? 0 : writeOperations);

            }

            // 2024-12-16  18:09-获取当前指定ID的MySQL数据库的网络发送/接收速率(数值单位为Byte)
            if (statistics1.getNetworks() != null && !statistics1.getNetworks().isEmpty()) {

                long rxBytes1 = 0L;
                long txBytes1 = 0L;

                for (Map.Entry<String, StatisticNetworksConfig> entry : statistics1.getNetworks().entrySet()) {

                    if (entry.getValue() != null) {

                        StatisticNetworksConfig network = entry.getValue();

                        rxBytes1 += network.getRxBytes() == null ? 0L : network.getRxBytes();
                        txBytes1 += network.getTxBytes() == null ? 0L : network.getTxBytes();

                    }

                }

                long rxBytes2 = 0L;
                long txBytes2 = 0L;

                if (statistics2.getNetworks() != null && !statistics2.getNetworks().isEmpty()) {

                    for (Map.Entry<String, StatisticNetworksConfig> entry : statistics2.getNetworks().entrySet()) {

                        if (entry.getValue() != null) {

                            StatisticNetworksConfig network = entry.getValue();

                            rxBytes2 += network.getRxBytes() == null ? 0L : network.getRxBytes();
                            txBytes2 += network.getTxBytes() == null ? 0L : network.getTxBytes();

                        }

                    }

                }

                map.put("rx_bytes", rxBytes2 - rxBytes1);
                map.put("tx_bytes", txBytes2 - txBytes1);

            }

        }

        return map;

    }


    @Override
    public List<TableInfo> queryAllTableInfoInTargetDatabase(@Nonnull Long id, @Nonnull String database) {

        if (StringUtils.hasLength(database)) {

            List<TableInfo> tableInfos = baseMapper.getAllTableSizeInTargetDatabase(database);

            if (tableInfos != null && !tableInfos.isEmpty()) {

                // 2024-12-16  21:50-若列表不为空 , 则按照表的数据大小+索引数据大小的和的高低倒序排序该列表(方便前端直接使用该列表数据)
                tableInfos.sort(Comparator.comparing(t -> t.getDataLength() + t.getIndexLength(), Comparator.reverseOrder()));

                tableInfos.sort((t1, t2) -> {

                    int lenCompareRes = Long.compare(t2.getDataLength() + t2.getIndexLength(), t1.getDataLength() + t1.getIndexLength());

                    if (lenCompareRes == 0) {

                        return t2.getCreateTime().compareTo(t1.getCreateTime());

                    } else {

                        return lenCompareRes;

                    }

                });

            }

            return tableInfos;

        } else {

            return new ArrayList<>();

        }

    }


    @Override
    public Map<String, Long> queryAllDatabaseSizes(@Nonnull Long id, @Nonnull Long count) {

        if (count <= 0) {

            return new HashMap<>();

        }

        List<Map<String, Object>> databaseSizes = baseMapper.getAllDatabaseSizes(count);
        if (databaseSizes != null && !databaseSizes.isEmpty()) {

            Map<String, Long> resultMap = new LinkedHashMap<>();
            for (Map<String, Object> databaseSizeMap : databaseSizes) {

                String dbName = (String) databaseSizeMap.getOrDefault("db_name", "未知数据库");
                BigDecimal dbSize = (BigDecimal) databaseSizeMap.getOrDefault("db_size", new BigDecimal(0L));
                resultMap.put(dbName, dbSize.longValue());

            }
            return resultMap;

        } else {

            return new HashMap<>();

        }

    }


    @Override
    public Map<String, Long> queryAllTableSizes(@Nonnull Long id, @Nonnull String dbName, @Nonnull Long count) {

        if (count <= 0L) {

            return new HashMap<>();

        }

        // 2024-12-19  14:10-若数据库名称字段不为空 , 则查询该数据库下的表大小情况 ; 否则 , 则以全部数据库为查询范围展开全面的排名比较
        if (StringUtils.hasLength(dbName)) {

            List<Map<String, Object>> tableSizes = baseMapper.getAllTableSizesInTargetDatabase(dbName, count);
            if (tableSizes != null && !tableSizes.isEmpty()) {

                Map<String, Long> resultMap = new LinkedHashMap<>();
                for (Map<String, Object> tableSizeMap : tableSizes) {

                    String tableName = (String) tableSizeMap.getOrDefault("table_name", "未知表");
                    BigInteger tableSize = (BigInteger) tableSizeMap.getOrDefault("table_size", new BigDecimal(0L));
                    resultMap.put(tableName, tableSize.longValue());

                }
                return resultMap;

            } else {

                return new HashMap<>();

            }

        } else {

            List<Map<String, Object>> tableSizes = baseMapper.getAllTableSizesInAllDatabase(count);
            if (tableSizes != null && !tableSizes.isEmpty()) {

                Map<String, Long> resultMap = new LinkedHashMap<>();
                for (Map<String, Object> tableSizeMap : tableSizes) {

                    String tableName = (String) tableSizeMap.getOrDefault("table_name", "未知表");
                    BigInteger tableSize = (BigInteger) tableSizeMap.getOrDefault("table_size", new BigDecimal(0L));
                    resultMap.put(tableName, tableSize.longValue());

                }
                return resultMap;

            } else {

                return new HashMap<>();

            }

        }

    }


}