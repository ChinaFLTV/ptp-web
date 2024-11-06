package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.info.UpdateInfo;
import ptp.fltv.web.mapper.UpdateInfoMapper;
import ptp.fltv.web.service.UpdateInfoService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/6 PM 11:43:28
 * @description 更新信息服务接口实现
 * @filename UpdateInfoServiceImpl.java
 */

@Service
public class UpdateInfoServiceImpl extends ServiceImpl<UpdateInfoMapper, UpdateInfo> implements UpdateInfoService {


    @Override
    public UpdateInfo queryLatestSingleUpdateInfo() {

        QueryWrapper<UpdateInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id")
                .last("LIMIT 1");

        return baseMapper.selectOne(queryWrapper);

    }


}