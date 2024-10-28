package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pfp.fltv.common.model.po.system.EventRecord;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/29 AM 2:02:16
 * @description 事件记录映射器
 * @filename EventRecordMapper.java
 */

@Mapper
public interface EventRecordMapper extends BaseMapper<EventRecord> {


}