package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.agriculture.AgricultureEnvironment;
import ptp.fltv.web.mapper.AgricultureEnvironmentMapper;
import ptp.fltv.web.service.AgricultureEnvironmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/29 PM 8:53:02
 * @description 农业生产环境数据服务接口实现
 * @filename AgricultureEnvironmentServiceImpl.java
 */

@Service
public class AgricultureEnvironmentServiceImpl extends ServiceImpl<AgricultureEnvironmentMapper, AgricultureEnvironment> implements AgricultureEnvironmentService {


    @Override
    public AgricultureEnvironment queryLatestSingleAgricultureEnvironmentByNodeId(@Nonnull Long nodeId) {

        QueryWrapper<AgricultureEnvironment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id") // 2024-11-29  21:42-由于这里农业生产环境数据的变化趋势与ID的变化趋势相吻合 , 效果相同 , 因此为了提升查询效率 , 这里直接采用ID进行倒序查询
                .eq("node_id", nodeId)
                .last("LIMIT 1");

        List<AgricultureEnvironment> agricultureEnvironments = list(queryWrapper);

        if (agricultureEnvironments == null || agricultureEnvironments.isEmpty()) {

            return null;

        } else {

            return agricultureEnvironments.get(0);

        }

    }


    @Override
    public List<AgricultureEnvironment> queryAgricultureEnvironmentPageByNodeIdWithSorting(@Nonnull Long nodeId, @Nonnull AgricultureEnvironment.SortType sortType, @Nonnull Long pageNum, @Nonnull Long pageSize) {

        QueryWrapper<AgricultureEnvironment> queryWrapper = new QueryWrapper<>();
        if (nodeId > 0) {

            queryWrapper.eq("node_id", nodeId);

        }

        switch (sortType) {

            case LIGHT_ASC -> queryWrapper.orderByAsc("light", "id");
            case LIGHT_DESC -> queryWrapper.orderByDesc("light", "id");
            case CO2_CONCENTRATION_ASC -> queryWrapper.orderByAsc("co2_concentration", "id");
            case CO2_CONCENTRATION_DESC -> queryWrapper.orderByDesc("co2_concentration", "id");
            case TEMPERATURE_ASC -> queryWrapper.orderByAsc("temperature", "id");
            case TEMPERATURE_DESC -> queryWrapper.orderByDesc("temperature", "id");
            case HUMIDITY_ASC -> queryWrapper.orderByAsc("humidity", "id");
            case HUMIDITY_DESC -> queryWrapper.orderByDesc("humidity", "id");
            case DEFAULT_ASC -> queryWrapper.orderByAsc("id");
            case DEFAULT_DESC -> queryWrapper.orderByDesc("id");

        }

        Page<AgricultureEnvironment> agricultureEnvironmentPage = new Page<>(pageNum, pageSize);
        agricultureEnvironmentPage = page(agricultureEnvironmentPage, queryWrapper);

        return agricultureEnvironmentPage.getRecords() == null ? new ArrayList<>() : agricultureEnvironmentPage.getRecords();

    }


}