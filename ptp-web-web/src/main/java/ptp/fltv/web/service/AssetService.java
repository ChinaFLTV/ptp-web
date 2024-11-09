package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Asset;
import pfp.fltv.common.model.po.system.EventRecord;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:00:36
 * @description 财产服务接口
 * @filename AssetService.java
 */

public interface AssetService extends IService<Asset> {


    /**
     * @param assetId   待修改的财产的ID
     * @param userId    动作发起者的用户的ID
     * @param eventType 财产余额的变动类型
     * @param quantity  财产余额的变动类型
     * @param remark    财产余额的变动的简短备注
     * @return 是否更新财产余额成功
     * @author Lenovo/LiGuanda
     * @date 2024/11/9 PM 5:25:01
     * @version 1.0.0
     * @description 修改单个财产的余额信息
     * @filename AssetService.java
     */
    boolean changeSingleAssetBalance(@Nonnull Long assetId, @Nonnull Long userId, @Nonnull EventRecord.EventType eventType, @Nonnull Double quantity, @Nonnull String remark);


}