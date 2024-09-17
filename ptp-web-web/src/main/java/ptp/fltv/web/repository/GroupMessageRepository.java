package ptp.fltv.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pfp.fltv.common.model.po.ws.GroupMessage;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/9/9 PM 9:16:24
 * @description 用于对聊天室消息数据进行CRUD操作的仓储库接口
 * @filename GroupMessageRepository.java
 */

@Repository
public interface GroupMessageRepository extends MongoRepository<GroupMessage, Long> {


}