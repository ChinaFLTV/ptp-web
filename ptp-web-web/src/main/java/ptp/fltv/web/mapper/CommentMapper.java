package ptp.fltv.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pfp.fltv.common.model.po.content.Comment;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:40:21
 * @description 文章评论Mapper
 * @filename CommentMapper.java
 */

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {


}
