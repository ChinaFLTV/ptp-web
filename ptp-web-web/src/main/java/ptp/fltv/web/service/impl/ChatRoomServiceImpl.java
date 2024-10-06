package ptp.fltv.web.service.impl;

import com.alibaba.fastjson2.JSON;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.annotation.Nonnull;
import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.enums.ChatType;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.ws.ChatRoom;
import pfp.fltv.common.model.po.ws.GroupMessage;
import pfp.fltv.common.model.vo.ChatVo;
import ptp.fltv.web.constants.CosConstants;
import ptp.fltv.web.repository.ChatRoomRepository;
import ptp.fltv.web.service.ChatRoomService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/23 PM 10:51:30
 * @description 聊天室服务接口的具体实现类
 * @filename ChatRoomServiceImpl.java
 */

@Slf4j
@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {


    private TransferManager transferManager;
    private ChatRoomRepository chatRoomRepository;
    private MongoTemplate mongoTemplate;

    private static final Map<Long, Map<String, Session>> chatRoomId2SessionsMap = new ConcurrentHashMap<>(); // 2024-6-23  23:46-存储房间号以及其当前持有的SESSION的映射 : roomId -> sessionId --> session
    private static final Map<Long, Map<Long, String>> chatRoomId2SessionIdsMap = new ConcurrentHashMap<>(); // 2024-6-25  9:11-存储用户ID与会话ID的映射 , 方便后续根据用户ID查找对应的会话 : roomId -> userId --> sessionId
    // private static final Map<Long, ChatRoom> chatRoomId2ChatRoomsMap = new ConcurrentHashMap<>(); // 2024-8-23  12:41-存储房间号与房间信息的映射 : roomId -> roomInfo
    private static final Long DEFAULT_CHAT_ROOM_ID = 666L; // 2024-8-23  20:42-默认的群聊房间ID
    private static final Set<Long> PUBLIC_CHAT_ROOM_IDS = new HashSet<>(); // 2024-10-2  22:43-当前处于公开状态的聊天室的ID集合


    static {

        // 2024-8-23  13:34-默认自动设置一个公告的聊天房间
        /*ChatRoom defaultChatRoom = ChatRoom.builder()
                .id(DEFAULT_CHAT_ROOM_ID)
                .name("达达利亚和他的朋友们")
                .avatarUrl("https://m.qqkw.com/d/tx/titlepic/c263a882a7ed7f099e6b48961af58b0b.jpg")
                .rank(6.0)
                .onlineUsers(chatRoomId2SessionIdsMap.getOrDefault(DEFAULT_CHAT_ROOM_ID, new ConcurrentHashMap<>()).keySet())
                .build();
        chatRoomId2ChatRoomsMap.put(DEFAULT_CHAT_ROOM_ID, defaultChatRoom);*/

        PUBLIC_CHAT_ROOM_IDS.add(DEFAULT_CHAT_ROOM_ID);

    }


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/9/9 PM 9:53:53
     * @version 1.0.0
     * @description 该方法主要替换原来地写死方式注入默认房间信息而转向通过MongoDB拉取默认房间信息数据
     * @filename ChatRoomServiceImpl.java
     */
    /*@PostConstruct
    public void init() {

        Optional<ChatRoom> defaultChatRoom = chatRoomRepository.findById(DEFAULT_CHAT_ROOM_ID);
        defaultChatRoom.ifPresent(cp -> chatRoomId2ChatRoomsMap.put(DEFAULT_CHAT_ROOM_ID, cp));

    }*/
    @Override
    public ChatRoom getSingleRoomInfo(@Nonnull Long roomId) {

        ChatRoom chatRoom = querySingleChatRoomById(roomId);

        Set<Long> liveUserIds = chatRoomId2SessionIdsMap.getOrDefault(roomId, new ConcurrentHashMap<>()).keySet();
        chatRoom.setOnlineUsers(liveUserIds);

        return chatRoom;

    }


    @Override
    public boolean joinChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (!chatRoomId2SessionsMap.containsKey(roomId)) {

            chatRoomId2SessionsMap.put(roomId, new HashMap<>());

        }
        if (!chatRoomId2SessionIdsMap.containsKey(roomId)) {

            chatRoomId2SessionIdsMap.put(roomId, new HashMap<>());

        }

        // 2024-6-24  10:27-如果先前已经存在了同ID的会话 , 则释放掉先前存在的旧会话 , 以节约服务器资源
        if (chatRoomId2SessionsMap.get(roomId).containsKey(session.getId())) {

            Session oldSession = chatRoomId2SessionsMap.get(roomId).get(session.getId());
            if (oldSession != null && oldSession.isOpen()) {

                oldSession.close();

            }

        }

        chatRoomId2SessionsMap.get(roomId).put(session.getId(), session);
        chatRoomId2SessionIdsMap.get(roomId).put(userId, session.getId());

        return true;

    }


    @Override
    public boolean sendGroupChatMsg(@Nonnull Long roomId, @Nonnull User user, @Nonnull GroupMessage groupChatMessage) throws IOException {

        String content = groupChatMessage.getContent();

        if (chatRoomId2SessionsMap.containsKey(roomId) && StringUtils.hasLength(content)) {

            Map<String, Session> sessionsMap = chatRoomId2SessionsMap.get(roomId);

            if (sessionsMap != null && !sessionsMap.isEmpty()) {

                Map<String, Object> wrappedMsgDataMap = new HashMap<>();
                wrappedMsgDataMap.put("room-id", roomId);
                wrappedMsgDataMap.put("user", JSON.toJSONString(user));
                wrappedMsgDataMap.put("message", JSON.toJSONString(groupChatMessage));

                for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {

                    // entry.getValue().getBasicRemote().sendText(String.format("[%s] : %s", groupChatMessage.getType() == GroupMessage.MessageType.SYSTEM ? "系统消息" : String.format("用户消息(%s)", user.getNickname()), content));
                    entry.getValue().getBasicRemote().sendText(JSON.toJSONString(wrappedMsgDataMap));

                }

                return true;

            }

        }

        return false;

    }


    @Override
    public boolean sendPrivateChatMsg(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull GroupMessage groupChatMessage) throws IOException {

        String sessionId = chatRoomId2SessionIdsMap.get(roomId).get(userId);
        Session session = chatRoomId2SessionsMap.get(roomId).get(sessionId);

        int messageTypeCode = groupChatMessage.getMessageType().getCode();

        session.getBasicRemote().sendText(String.format("[%s] : %s", messageTypeCode >= 1703 && messageTypeCode <= 1706 ? "系统消息" : "用户消息", groupChatMessage.getContent()));
        return false;

    }


    @Override
    public void leaveChatRoom(@Nonnull Long roomId, @Nonnull Long userId, @Nonnull Session session) throws IOException {

        if (session.isOpen()) {

            session.close();

        }
        Map<String, Session> sessionsMap = chatRoomId2SessionsMap.get(roomId);
        chatRoomId2SessionsMap.get(roomId).remove(session.getId());
        chatRoomId2SessionIdsMap.get(roomId).remove(userId);

    }


    @Override
    public String uploadMediaFile(@Nonnull Long messageId, @Nonnull GroupMessage.ContentType contentType, @Nonnull MultipartFile file) {

        try {

            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            // 2024-10-6  21:03-获取到文件的拓展名 , 并将其作为云端资源的拓展名 , 注意 , 这里前端方面必须确保用户提交的资源的实际类型与所提交到的分类必须一致 , 后端不会对文件拓展名进行合法性校验
            String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            // 2024-9-5  20:00-腾讯云COS支持文件名称最长为100个字符 , 因此这里在已有的限制条件上进一步进行限制 , 最多支持80个字符长度的文件名称 , 超出则进行截断处理
            String key = CosConstants.BUCKET_CHAT_ROOM_FILE + "/" + messageId + "-" + (fileName.length() > 80 ? fileName.substring(0, 80) : fileName);

            String bucketName = CosConstants.BUCKET_CHAT_ROOM;

            switch (contentType) {

                case PHOTO -> key = CosConstants.BUCKET_CHAT_ROOM_PHOTO + "/" + messageId + "." + extensionName;
                case VIDEO -> key = CosConstants.BUCKET_CHAT_ROOM_VIDEO + "/" + messageId + "." + extensionName;
                case AUDIO -> key = CosConstants.BUCKET_CHAT_ROOM_AUDIO + "/" + messageId + "." + extensionName;
                case VOICE -> key = CosConstants.BUCKET_CHAT_ROOM_VOICE + "/" + messageId + "." + extensionName;

            }

            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), null);

            // 2024-9-2  21:19-高级接口会返回一个异步结果Upload , 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            UploadResult uploadResult = transferManager.upload(request).waitForUploadResult();

            return String.format(CosConstants.PUBLIC_REQUEST_URL_PREFIX, bucketName, key);

        } catch (CosClientException | InterruptedException | IOException ex) {

            log.error("上传聊天室产生的多媒体文件到云端COS中失败 : {}", ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
            return null;

        }

    }


    @Override
    public ChatRoom querySingleChatRoomById(@Nonnull Long id) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);

        return optionalChatRoom.orElse(null);

    }


    @Override
    public List<ChatVo> queryAllPublicChatRoom() {

        List<ChatRoom> chatRooms = chatRoomRepository.findAllById(PUBLIC_CHAT_ROOM_IDS);

        List<ChatVo> chatVos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {

            ChatVo chatVo = ChatVo.fromChatRoom(chatRoom);
            chatVo.setChatType(ChatType.GROUP_CHAT);

            Query query = new Query(

                    Criteria.where("chatRoomId")
                            .is(chatRoom.getId())
                            .and("messageType")
                            .is("GROUP_CHAT")
                    // .and("contentType")
                    // .is("TEXT")

            ).with(Sort.by(Sort.Direction.DESC, "id", "dateTime"))
                    .limit(1);

            GroupMessage groupMessage = mongoTemplate.findOne(query, GroupMessage.class);

            // 2024-10-7  1:46-这里允许向用户展示指定聊天室中的最新的非文本类型的群聊消息预览 , 这里对消息内容作二次加工以改进用户体验
            switch (Objects.requireNonNull(groupMessage).getContentType()) {

                case PHOTO -> groupMessage.setContent("[图片]");
                case VIDEO -> groupMessage.setContent("[视频]");
                case AUDIO -> groupMessage.setContent("[音频]");
                case VOICE -> groupMessage.setContent("[语音]");
                case FILE -> groupMessage.setContent("[文件]");

            }

            // 2024-10-2  23:29-可能存在当前聊天室还没有人发过消息或者房间内的消息已经被清理过了 , 这种情况是存在的 , 因此需要判断一下
            chatVo.setLatestMsgSendUserId(groupMessage.getSenderId());
            chatVo.setLatestMsgSendUserNickname(groupMessage.getSenderNickname());
            chatVo.setLatestMsgSendUserAvatarUrl(groupMessage.getSenderAvatarUrl());
            chatVo.setLatestMsgContent(groupMessage.getContent());
            chatVo.setLatestMsgPubdate(groupMessage.getDateTime());

            chatVos.add(chatVo);

        }

        return chatVos;

    }


}