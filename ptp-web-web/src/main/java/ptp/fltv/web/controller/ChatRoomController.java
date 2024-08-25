package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.po.ws.ChatRoom;
import ptp.fltv.web.service.ChatRoomService;

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


    @LogRecord(description = "获取聊天室的人数信息")
    @SentinelResource("web-content-user-chat-controller")
    @Operation(description = "获取聊天室的人数信息")
    @GetMapping("/query/population/{id}")
    public Result<ChatRoom> querySingleChatRoom(

            @Parameter(name = "id", description = "待查询的房间号ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        return Result.success(chatRoomService.getSingleRoomInfo(id));

    }


}
