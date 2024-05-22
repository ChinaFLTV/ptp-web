package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import ptp.fltv.web.mapper.CommodityMapper;
import ptp.fltv.web.service.CommodityService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/22 PM 9:46:45
 * @description 商品服务接口的实现类
 * @filename CommodityServiceImpl.java
 */

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {


    @Override
    public Commodity getOneById(@Nonnull Long id) {

        return baseMapper.getOneById(id);

    }


    @Override
    public List<Commodity> getListByRange(@Nonnull Long offset, @Nonnull Long length, @Nonnull Boolean isASC) {

        return baseMapper.getListByRange(offset, length, isASC);

    }


    @Transactional
    @Override
    public int insertOne(@Nonnull Commodity commodity) throws PtpException {

        int isInsertCommoditySuccessfully = baseMapper.insertCommodity(commodity);
        int isInsertCommodityDetailsSuccessfully = baseMapper.insertCommodityDetails(commodity);

        if (isInsertCommoditySuccessfully != isInsertCommodityDetailsSuccessfully) {

            throw new PtpException(806);

        }

        return isInsertCommodityDetailsSuccessfully;

    }


    @Transactional // 2024-5-22  21:58-要加载具体方法上，不要给不使用事务的方法增加响应时间，同时方法可见性必须为public，其他可见性事务将不会奏效，还有一点它只能回滚非受检异常
    @Override
    public int updateOne(@Nonnull Commodity commodity) throws PtpException {

        int isUpdateCommoditySuccessfully = baseMapper.updateCommodity(commodity);
        int isUpdateCommodityDetailsSuccessfully = baseMapper.updateCommodityDetails(commodity);

        // 2024-5-22  21:52-两表更新数据条目数不一致，极有可能出现异常(因为我们都是按照商品ID进行索引并更新的，按道理讲，前后数目必须一致才可以)
        if (isUpdateCommoditySuccessfully != isUpdateCommodityDetailsSuccessfully) {

            throw new PtpException(806);

        }

        return isUpdateCommodityDetailsSuccessfully;

    }


    @Override
    public int deleteOne(@Nonnull Long id) {

        return baseMapper.deleteOne(id);

    }


}