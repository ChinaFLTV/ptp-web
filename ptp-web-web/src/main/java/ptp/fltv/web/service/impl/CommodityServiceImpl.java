package ptp.fltv.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.enums.CommodityStatus;
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


    @Override
    public List<Commodity> list() {

        return baseMapper.list();

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


    @Override
    public boolean replenishOne(@Nonnull Long id, @Nonnull Integer count) {

        Commodity commodity = getOneById(id);
        if (commodity != null) {

            commodity.setStockQuantity(commodity.getStockQuantity() + count);

            // 2024-5-23  22:57-这里之所以要通过直接获取本类的代理对象调用事务方法，是因为，在类中某个方法内部去调用类内其他的方法(不管是事务还是不是事务方法)
            // 都将不走代理调用，而是直接走原生对象调用，因而使得事务方法的事务失效，为了避免这个问题，有两个选择：
            // 1.将那个被调用的事务方法抽离到类外
            // 2.通过获取代理对象来直接调用
            // 这里我们选择第二种，不过要在配置类上添加 @EnableAspectJAutoProxy(exposeProxy = true) 注解
            int updateItemCount = ((CommodityService) AopContext.currentProxy()).updateOne(commodity);
            return updateItemCount > 0;

        }

        return false;

    }


    @Override
    public boolean seckillOne(@Nonnull Long id, @Nonnull Integer count) {

        Commodity commodity = getOneById(id);
        if (commodity != null && commodity.getStockQuantity() >= count) {

            commodity.setStockQuantity(commodity.getStockQuantity() - count);
            // 2024-5-2  22:40-秒杀后检查库存是否有剩余，若无，则置商品状态为售罄，也节省了一次单独更新的开销
            if (commodity.getStockQuantity() <= 0) {

                commodity.setStatus(CommodityStatus.SOLD_OUT);

            }
            int updateItemCount = ((CommodityService) AopContext.currentProxy()).updateOne(commodity);
            return updateItemCount > 0;

        }
        return false;

    }


}