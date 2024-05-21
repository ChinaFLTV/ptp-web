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
     * @param commodity 新的商品数据
     * @return 是否更新成功
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 10:54:41
     * @version 1.0.0
     * @description 更新单个商品
     * @filename CommodityMapper.java
     */
    boolean updateOne(@Nonnull @Param("commodity") Commodity commodity);


    /**
     * @param id 待删除的商品ID
     * @return 是否删除成功
     * @author Lenovo/LiGuanda
     * @date 2024/5/21 PM 10:55:50
     * @version 1.0.0
     * @description 根据ID删除单个商品
     * @filename CommodityMapper.java
     */
    boolean deleteOne(@Nonnull @Param("id") Long id);


}