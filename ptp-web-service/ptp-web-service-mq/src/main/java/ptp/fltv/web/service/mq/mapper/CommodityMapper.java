package ptp.fltv.web.service.mq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import pfp.fltv.common.model.po.finance.Commodity;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/21 PM 9:07:28
 * @description 商品Mapper
 * @filename CommodityMapper.java
 */

@Mapper
public interface CommodityMapper extends BaseMapper<Commodity> {


    /**
     * @param id 商品ID
     * @return 查询出的指定商品
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 9:09:46
     * @version 1.0.0
     * @description 根据商品ID获取单个商品
     * @filename CommodityMapper.java
     */
    Commodity getOneById(@Nonnull @Param("id") Long id);


    /**
     * @param offset 偏移量
     * @param length 列表长度
     * @param isASC  是否升序排列
     * @return 查询到的指定位置的商品列表
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 10:16:39
     * @version 1.0.0
     * @description 批量查询指定偏移量处之后的指定长度的商品列表
     * @filename CommodityMapper.java
     */
    List<Commodity> getListByRange(@Nonnull @Param("offset") Long offset, @Nonnull @Param("length") Long length, @Nonnull @Param("isASC") Boolean isASC);


    /**
     * @return 全部的商品数据
     * @author Lenovo/LiGuanda
     * @date 2024/5/23 PM 9:51:56
     * @version 1.0.0
     * @description 获取全部的商品数据
     * @filename CommodityMapper.java
     */
    List<Commodity> list();


    /**
     * @param commodity 待插入的商品数据
     * @return 插入成功所影响的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:43:03
     * @version 1.0.0
     * @description 添加一条商品数据(不含商品详情)
     * @filename CommodityMapper.java
     */
    int insertCommodity(@Nonnull Commodity commodity);


    /**
     * @param commodity 待插入的商品数据
     * @return 插入成功所影响的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 10:50:17
     * @version 1.0.0
     * @description 添加一条商品数据(只含商品详情)
     * @filename CommodityMapper.java
     */
    int insertCommodityDetails(@Nonnull Commodity commodity);


    /**
     * @param commodity 新的商品数据
     * @return 更新成功的行数
     * @implNote 尽量不要更改返回值为布尔类型，因为整型不仅可以告诉我们是否更新成功，还可以告诉我们成功更新了几条数据
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 10:54:41
     * @version 1.0.0
     * @description 更新单个商品(不包含商品详情部分)
     * @filename CommodityMapper.java
     */
    int updateCommodity(@Nonnull Commodity commodity);


    /**
     * @param commodity 新的商品数据
     * @return 更新成功的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 9:43:50
     * @version 1.0.0
     * @description 更新单个商品(只包含商品详情部分)
     * @filename CommodityMapper.java
     */
    int updateCommodityDetails(@Nonnull Commodity commodity);


    /**
     * @param id 待删除的商品ID
     * @return 删除成功的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 10:55:50
     * @version 1.0.0
     * @description 根据ID删除单个商品
     * @filename CommodityMapper.java
     */
    int deleteOne(@Nonnull @Param("id") Long id);


    /**
     * @param id       待操作的商品ID
     * @param version2 该商品的当前的版本号
     * @param count    下调商品数量
     * @return 该商品库存更新操作所影响的行数
     * @author Lenovo/LiGuanda
     * @date 2024/6/3 PM 10:10:40
     * @version 1.0.0
     * @description 以乐观锁的形式更新商品的库存容量(本次以注解的形式编写 ， 没必要转到mapper.xml中实现 ， 那样太麻烦了)
     * @filename CommodityMapper.java
     */
    @Update("UPDATE commodity_details SET stock_quantity = stock_quantity - #{count} , version = version + 1 WHERE commodity_id = #{id} AND version = #{version2}")
    int decreaseStockQuantityByIdAndVersion(@Nonnull @Param("id") Long id, @Nonnull @Param("count") Integer count, @Nonnull @Param("version2") Integer version2);


    /**
     * @param id       待操作的商品ID
     * @param version2 该商品的当前的版本号
     * @param delta    商品变化数量(可以是正增长，也可以是逆下降)
     * @return 该商品库存更新操作所影响的行数
     * @author Lenovo/LiGuanda
     * @date 2024/6/12 PM 11:23:08
     * @version 1.0.0
     * @description 以乐观锁的形式更新(添加 / 删除)商品的库存容量
     * @filename CommodityMapper.java
     */
    @Update("UPDATE commodity_details SET stock_quantity = stock_quantity + #{delta} , version = version + 1 WHERE commodity_id = #{id} AND version = #{version2}")
    int updateStockQuantityByIdAndVersion(@Nonnull @Param("id") Long id, @Nonnull @Param("delta") Integer delta, @Nonnull @Param("version2") Integer version2);


}