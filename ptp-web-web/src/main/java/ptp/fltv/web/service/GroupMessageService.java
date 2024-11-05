package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.ws.GroupMessage;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/10 PM 10:02:44
 * @description 聊天室消息服务接口
 * @filename GroupMessageService.java
 */

public interface GroupMessageService {


    /**
     * @param pageNumber 当前的页码(即将请求的页码数 , 从0开始)
     * @param count      查询的数量
     * @param chatRoomId 群聊消息所属的房间号ID
     * @return 指定数据页上的群聊数据列表
     * @author Lenovo/LiGuanda
     * @date 2024/9/11 PM 8:52:20
     * @version 1.0.0
     * @description 根据偏移量和数量查询指定的群聊消息页
     * @apiNote 由于前后端采用的雪花ID生成器不是同一个实例 , 因此双方为系统消息、群聊消息分别生成的ID可能会存在相同甚至时序相反的意外情况 , 因此大多数情况下 , 次方法将只拉取群聊类型的消息而忽略系统消息
     * @filename GroupMessageService.java
     */
    List<GroupMessage> queryGroupMessagePage(@Nonnull Long chatRoomId, @Nonnull Long pageNumber, @Nonnull Long count);


    /**
     * @param groupMessage 待插入的群聊消息
     * @author Lenovo/LiGuanda
     * @date 2024/9/10 PM 10:20:18
     * @version 1.0.0
     * @description 插入一条群聊消息
     * @filename GroupMessageService.java
     */
    void insertOne(@Nonnull GroupMessage groupMessage);


}