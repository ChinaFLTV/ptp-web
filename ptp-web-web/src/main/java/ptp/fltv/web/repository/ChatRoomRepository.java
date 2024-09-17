package ptp.fltv.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pfp.fltv.common.model.po.ws.ChatRoom;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/9 PM 9:13:29
 * @description 用于对聊天室信息数据进行CRUD操作的仓储库接口
 * @filename ChatRoomRepository.java
 */

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, Long> {


}