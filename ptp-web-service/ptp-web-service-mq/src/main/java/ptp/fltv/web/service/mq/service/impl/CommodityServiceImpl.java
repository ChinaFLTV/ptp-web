package ptp.fltv.web.service.mq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfp.fltv.common.enums.CommodityStatus;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;
import ptp.fltv.web.service.mq.mapper.CommodityMapper;
import ptp.fltv.web.service.mq.service.CommodityService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/22 PM 9:46:45
 * @description 商品服务接口的实现类
 * @filename CommodityServiceImpl.java
 */

@Slf4j
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
    public Commodity replenishOne(@Nonnull Long id, @Nonnull Integer count) {

        Commodity commodity = getOneById(id);
        if (commodity != null) {

            commodity.setStockQuantity(commodity.getStockQuantity() + count);

            // 2024-5-23  22:57-这里之所以要通过直接获取本类的代理对象调用事务方法，是因为，在类中某个方法内部去调用类内其他的方法(不管是事务还是不是事务方法)
            // 都将不走代理调用，而是直接走原生对象调用，因而使得事务方法的事务失效，为了避免这个问题，有两个选择：
            // 1.将那个被调用的事务方法抽离到类外
            // 2.通过获取代理对象来直接调用
            // 这里我们选择第二种，不过要在配置类上添加 @EnableAspectJAutoProxy(exposeProxy = true) 注解
            // int updateItemCount = ((CommodityService) AopContext.currentProxy()).updateOne(commodity);
            // 2024-6-12  23:26-改用CAS形式进行商品库存数量的更新，以避免数据不一致的问题
            int updateItemCount = ((CommodityMapper) ((CommodityService) AopContext.currentProxy()).getBaseMapper()).updateStockQuantityByIdAndVersion(id, count, commodity.getVersion2());
            return updateItemCount > 0 ? commodity : null;

        }

        return null;

    }


    @Override
    public Commodity seckillOne(@Nonnull Long id, @Nonnull Integer count) throws PtpException {

        Commodity commodity = getOneById(id);
        if (commodity != null && commodity.getStockQuantity() >= count) {

            // 2024-5-25  21:49-若商品库存数量已经为空，则直接抛异常，比直接返回null还优雅
            if (commodity.getStockQuantity() <= 0) {

                // 2024-5-25  21:54-这里不能直接修正商品状态为售罄，有可能商品处于其他不可变状态！
                throw new PtpException(808, "The stock of goods is empty !");

            }

            commodity.setStockQuantity(commodity.getStockQuantity() - count);
            // 2024-5-2  22:40-秒杀后检查库存是否有剩余，若无，则置商品状态为售罄，也节省了一次单独更新的开销
            if (commodity.getStockQuantity() <= 0) {

                commodity.setStatus(CommodityStatus.SOLD_OUT);

            }

            int updateItemCount = ((CommodityMapper) ((CommodityService) AopContext.currentProxy()).getBaseMapper()).decreaseStockQuantityByIdAndVersion(id, count, commodity.getVersion2());
            return updateItemCount > 0 ? commodity : null;

        }

        return null;

    }


}