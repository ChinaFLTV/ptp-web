package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.po.ws.ChatRoom;
import pfp.fltv.common.model.po.ws.GroupMessage;
import pfp.fltv.common.model.vo.ChatVo;
import ptp.fltv.web.service.ChatRoomService;
import ptp.fltv.web.service.GroupMessageService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/22 PM 11:44:50
 * @description 群聊控制器
 * @filename ChatRoomController.java
 */

@AllArgsConstructor
@Tag(name = "群聊操作接口")
@RestController
@RequestMapping("/content/user/chat")
public class ChatRoomController {


    private ChatRoomService chatRoomService;
    private GroupMessageService groupMessageService;


    @LogRecord(description = "获取聊天室的人数信息")
    @SentinelResource("web-content-user-chat-controller")
    @Operation(description = "获取聊天室的人数信息")
    @GetMapping("/query/population/{id}")
    public Result<ChatRoom> querySingleChatRoom(

            @Parameter(name = "id", description = "待查询的房间号ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        return Result.success(chatRoomService.getSingleRoomInfo(id));

    }


    @LogRecord(description = "分页获取聊天室的聊天消息")
    @SentinelResource("web-content-user-chat-controller")
    @Operation(description = "分页获取聊天室的聊天消息")
    @GetMapping("/query/groupMessage/page")
    public Result<List<GroupMessage>> queryGroupMessagePage(

            @Parameter(name = "chatRoomId", description = "待查询的房间号ID", required = true) @RequestParam("chatRoomId") Long chatRoomId,
            @Parameter(name = "pageNumber", description = "当前的页码(即将请求的页码数)", required = true) @RequestParam("pageNumber") Long pageNumber,
            @Parameter(name = "count", description = "查询的数量", required = true) @RequestParam("count") Long count

    ) {

        return Result.success(groupMessageService.queryGroupMessagePage(chatRoomId, pageNumber, count));

    }


    @LogRecord(description = "获取全部的公共聊天室信息")
    @SentinelResource("web-content-user-chat-controller")
    @Operation(description = "获取全部的公共聊天室信息")
    @GetMapping("/query/chatRoom/public/all")
    public Result<List<ChatVo>> queryAllPublicChatRoom() {

        return Result.success(chatRoomService.queryAllPublicChatRoom());

    }


    @LogRecord(description = "上传聊天消息中引用到的媒体数据")
    @SentinelResource("web-content-user-chat-controller")
    @Operation(description = "上传聊天消息中引用到的媒体数据")
    @PostMapping("/upload/media")
    public Result<String> uploadMediaFile(

            @Parameter(name = "userId", description = "当前的用户ID", required = true) @RequestParam("userId") Long userId,
            @Parameter(name = "messageId", description = "消息ID", required = true) @RequestParam("messageId") Long messageId,
            @Parameter(name = "contentType", description = "消息内容类型", required = true) @RequestParam("contentType") GroupMessage.ContentType contentType,
            @Parameter(name = "mediaFile", description = "多媒体文件", required = true) @RequestParam("mediaFile") MultipartFile mediaFile

    ) {

        return Result.success(chatRoomService.uploadMediaFile(messageId, contentType, mediaFile));

    }


}
