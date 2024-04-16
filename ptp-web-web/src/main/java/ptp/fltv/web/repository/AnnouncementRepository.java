package ptp.fltv.web.repository;

import org.springframework.data.repository.Repository;
import pfp.fltv.common.model.po.content.Announcement;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/16 PM 10:41:35
 * @description 定义公告实体对应的存储库，便于在ElasticSearch中进行数据操作
 * @filename AnnouncementRepository.java
 */

public interface AnnouncementRepository extends Repository<Announcement, Long> {


}
