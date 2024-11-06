package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pfp.fltv.common.model.po.info.UpdateInfo;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/6 PM 11:40:46
 * @description 更新信息Mapper
 * @filename UpdateInfoMapper.java
 */

@Mapper
public interface UpdateInfoMapper extends BaseMapper<UpdateInfo> {


    /**
     *
     * @author Lenovo/LiGuanda
     * @date 2024/11/7 AM 1:22:38
     * @version 1.0.0
     * @return 查询到的最新的单条软件更新信息 , 如果更新列表为空 , 则返回null
     * @description 查询最新的单条更新信息数据
     * @filename UpdateInfoMapper.java
     *
     */
    @Select("select * from ptp_web.update_info order by id desc limit 1")
    UpdateInfo queryLatestSingleUpdateInfo();


}