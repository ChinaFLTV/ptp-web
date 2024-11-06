package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pfp.fltv.common.model.po.info.UpdateInfo;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/6 PM 11:42:23
 * @description 更新信息服务接口
 * @filename UpdateInfoService.java
 */

public interface UpdateInfoService extends IService<UpdateInfo> {


    /**
     * @return 查询到的最新的单条软件更新信息 , 如果更新列表为空 , 则返回null
     * @author Lenovo/LiGuanda
     * @date 2024/11/7 AM 1:16:39
     * @version 1.0.0
     * @description 查询最新的单条更新信息数据
     * @filename UpdateInfoService.java
     */
    UpdateInfo queryLatestSingleUpdateInfo();


}