package com.fltv.web.service.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fltv.web.service.monitor.mapper.MySqlMapper;
import com.fltv.web.service.monitor.model.po.DatabaseStatus;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.service.MySqlService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.manage.Asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 4:08:47
 * @description MySQL数据库监控服务接口实现
 * @filename MySqlServiceImpl.java
 */

@Service
public class MySqlServiceImpl extends ServiceImpl<MySqlMapper, Asset> implements MySqlService {


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


}