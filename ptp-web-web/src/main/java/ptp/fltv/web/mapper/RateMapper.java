package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pfp.fltv.common.model.po.manage.Rate;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/26 AM 12:18:28
 * @description 评分映射器
 * @filename RateMapper.java
 */

@Mapper
public interface RateMapper extends BaseMapper<Rate> {


}