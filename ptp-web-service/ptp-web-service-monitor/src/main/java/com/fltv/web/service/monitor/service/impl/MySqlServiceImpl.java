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
    public Map<String, Object> getBaseStatusById(@Nonnull Long id) {

        Map<String, Object> map = new HashMap<>();

        // 2024-12-16  18:06-获取当前指定ID的MySQL数据库的数据库磁盘占用数据(数值单位为Byte)
        InspectContainerResponse response = dockerClient.inspectContainerCmd(dockerContainerConstants.getPtpBackendMySql1ContainerId()).exec();
        if (response != null) {

            map.put("db_disk_usage", response.getSizeRw() == null ? 0 : response.getSizeRw());

        }

        // 2024-12-16  18:06-获取当前指定ID的MySQL数据库的当前活跃的连接的数量
        map.put("db_connections", Integer.parseInt(baseMapper.getConnectionNum().getValue()));

        // 2024-12-16  18:21-获取当前指定ID的MySQL数据库的查询总数
        map.put("queries", Integer.parseInt(baseMapper.getQueryNum().getValue()));

        Statistics statistics = dockerClient.statsCmd(dockerContainerConstants.getPtpBackendMySql1ContainerId())
                .exec(new InvocationBuilder.AsyncResultCallback<>())
                .awaitResult();
        if (statistics != null) {

            // 2024-12-16  18:14-获取当前指定ID的MySQL数据库的CPU占用情况(数值单位为Byte)
            if (statistics.getCpuStats() != null) {

                Long onlineCpus = statistics.getCpuStats().getOnlineCpus();
                CpuUsageConfig cpuUsageConfig = statistics.getCpuStats().getCpuUsage();

                map.put("online_cpus", onlineCpus == null ? 0 : onlineCpus);
                map.put("per_cpu_usage", cpuUsageConfig == null || cpuUsageConfig.getPercpuUsage() == null ? new ArrayList<>() : cpuUsageConfig.getPercpuUsage());
                map.put("cpu_usage", cpuUsageConfig == null || cpuUsageConfig.getTotalUsage() == null ? 0 : cpuUsageConfig.getTotalUsage());

            }

            // 2024-12-16  18:24-获取当前指定ID的MySQL数据库的内存占用情况(数值单位为Byte)
            MemoryStatsConfig memoryStats = statistics.getMemoryStats();
            if (memoryStats != null) {

                map.put("memory_usage", memoryStats.getUsage() == null ? 0 : memoryStats.getUsage());
                map.put("memory_max_usage", memoryStats.getMaxUsage() == null ? 0 : memoryStats.getMaxUsage());
                map.put("memory_limit", memoryStats.getLimit() == null ? 0 : memoryStats.getLimit());

            }

            // 2024-12-16  19:59-获取当前指定ID的MySQL数据库的磁盘读/写速率(数值单位为Byte)
            BlkioStatsConfig blkioStats = statistics.getBlkioStats();
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
            if (statistics.getNetworks() != null && !statistics.getNetworks().isEmpty()) {

                long rxBytes = 0L;
                long txBytes = 0L;

                for (Map.Entry<String, StatisticNetworksConfig> entry : statistics.getNetworks().entrySet()) {

                    if (entry.getValue() != null) {

                        StatisticNetworksConfig network = entry.getValue();

                        rxBytes += network.getRxBytes() == null ? 0L : network.getRxBytes();
                        txBytes += network.getTxBytes() == null ? 0L : network.getTxBytes();

                    }

                }

                map.put("rx_bytes", rxBytes);
                map.put("tx_bytes", txBytes);

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


}