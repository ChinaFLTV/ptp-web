package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    int updateCommodity(@Nonnull @Param("commodity") Commodity commodity);


    /**
     * @param commodity 新的商品数据
     * @return 更新成功的行数
     * @author Lenovo/LiGuanda
     * @date 2024/5/22 PM 9:43:50
     * @version 1.0.0
     * @description 更新单个商品(只包含商品详情部分)
     * @filename CommodityMapper.java
     */
    int updateCommodityDetails(@Nonnull @Param("commodity") Commodity commodity);


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


}