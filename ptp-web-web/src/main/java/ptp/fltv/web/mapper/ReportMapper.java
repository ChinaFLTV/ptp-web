package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pfp.fltv.common.model.po.manage.Report;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/17 PM 8:40:03
 * @description 举报映射器
 * @filename ReportMapper.java
 */

@Mapper
public interface ReportMapper extends BaseMapper<Report> {


}