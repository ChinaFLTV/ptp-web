package ptp.fltv.web.service.impl;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pfp.fltv.common.model.po.ws.GroupMessage;
import ptp.fltv.web.repository.GroupMessageRepository;
import ptp.fltv.web.service.GroupMessageService;

import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/10 PM 10:03:28
 * @description 聊天室消息服务接口实现
 * @filename GroupMessageServiceImpl.java
 */

@AllArgsConstructor
@Service
public class GroupMessageServiceImpl implements GroupMessageService {


    private GroupMessageRepository groupMessageRepository;


    @Override
    public List<GroupMessage> queryGroupMessagePage(@Nonnull Long chatRoomId, @Nonnull Long pageNumber, @Nonnull Long count) {

        return groupMessageRepository.findAll(PageRequest.of(Math.toIntExact(pageNumber), Math.toIntExact(count), Sort.by(Sort.Direction.DESC, "dateTime"))).getContent();

    }


    @Override
    public void insertOne(@Nonnull GroupMessage groupMessage) {

        groupMessageRepository.insert(groupMessage);

    }


}