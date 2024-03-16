package pfp.fltv.common.model.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @date 2024/3/14 下午 9:31:59
 * @description 文章评论的实体类(PO)
 * @filename PassageComment.java
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassageComment implements Serializable {


    private String commentDateTime;  // 评论发布时间

    private String commentContent;  // 评论内容

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "评论序列号")
    private int commentID;

    private int passageID;  // 文章序列号

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章标题")
    private String passageTitle;

    private int sendUserID;  // 发布者序列号

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发布者头像URL")
    private String sendUserAvatar;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发送者昵称")
    private String sendUserNickname;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发送者等级")
    private double sendUserRank;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "发送者用户信息")
    private String sendUser;

    private int receiveUserID;  // 接受者序列号

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "接受者头像URL")
    private String receiveUserAvatar;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "接受者昵称")
    private String receiveUserNickname;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "接受者等级")
    private double receiveUserRank;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "评论当前状态")
    private String status = "NORMAL";

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "评论附加信息")
    private String elseInformation;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "记录文章评论发布时的IP属地")
    private String location;

    @Schema(accessMode = Schema.AccessMode.READ_WRITE, description = "文章评论是否已被逻辑删除")
    @TableLogic
    private Integer is_deleted;


}