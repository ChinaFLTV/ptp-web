package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Param;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.finance.Commodity;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/22 PM 9:45:56
 * @description 商品服务接口
 * @filename CommodityService.java
 */

public interface CommodityService extends IService<Commodity> {


    /**
     * @param id 商品ID
     * @return 查询出的指定商品
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:40:54
     * @version 1.0.0
     * @description 根据商品ID获取单个商品
     * @filename CommodityService.java
     */
    Commodity getOneById(@Nonnull @Param("id") Long id);


    /**
     * @param offset 偏移量
     * @param length 列表长度
     * @param isASC  是否升序排列
     * @return 查询到的指定位置的商品列表
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:40:19
     * @version 1.0.0
     * @description 批量查询指定偏移量处之后的指定长度的商品列表
     * @filename CommodityService.java
     */
    List<Commodity> getListByRange(@Nonnull Long offset, @Nonnull Long length, @Nonnull Boolean isASC);


    /**
     * @return 全部的商品数据
     * @author Lenovo/LiGuanda
     * @date 2024/5/23 PM 9:55:13
     * @version 1.0.0
     * @description 获取全部的商品数据
     * @filename CommodityService.java
     */
    @Override
    List<Commodity> list();


    /**
     * @param commodity 待插入的商品数据
     * @return 插入成功所影响的行数
     * @throws PtpException 两次内部的插入操作部分或全部出现异常
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:51:18
     * @version 1.0.0
     * @description 添加一条商品数据
     * @filename CommodityService.java
     */
    int insertOne(@Nonnull Commodity commodity) throws PtpException;


    /**
     * @param commodity 新的商品数据
     * @return 更新成功的行数
     * @throws PtpException 两次内部的更新操作部分或全部出现异常
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 9:47:46
     * @version 1.0.0
     * @description 更新单个商品
     * @filename CommodityService.java
     */
    int updateOne(@Nonnull Commodity commodity) throws PtpException;


    /**
     * @param id 待删除的商品ID
     * @return 删除成功的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:39:40
     * @version 1.0.0
     * @description 根据ID删除单个商品
     * @filename CommodityService.java
     */
    int deleteOne(@Nonnull Long id);


    /**
     * @param count 补充的数量
     * @param id    补充的商品ID
     * @return 补货成功则返回修改后的商品数据，否则返回null
     * @author Lenovo/LiGuanda
     * @date 2024/5/23 PM 10:43:31
     * @version 1.0.0
     * @description 根据ID补充单种商品的n个
     * @filename CommodityService.java
     */
    Commodity replenishOne(@Nonnull Long id, @Nonnull Integer count);


    /**
     * @param count 秒杀的数量
     * @param id    秒杀的商品ID
     * @return 秒杀成功则返回修改后的商品数据，否则返回null
     * @implNote 这样做也方便后续ELasticSearch更新操作无需再去查询一遍，直接进行更新即可，节省了一次查询开销
     * @author Lenovo/LiGuanda
     * @date 2024/5/23 PM 10:36:14
     * @version 1.0.0
     * @description 根据ID秒杀单种商品的n个
     * @filename CommodityService.java
     */
    Commodity seckillOne(@Nonnull Long id, @Nonnull Integer count);


}