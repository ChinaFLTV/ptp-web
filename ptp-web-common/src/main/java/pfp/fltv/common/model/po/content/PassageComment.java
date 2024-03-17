package pfp.fltv.common.model.po.content;


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


    private int passageID;  // 文章序列号

    @Schema(description = "文章标题")
    private String passageTitle;

    private int sendUserID;  // 发布者序列号

    @Schema(description = "发布者头像URL")
    private String sendUserAvatar;

    @Schema(description = "发送者昵称")
    private String sendUserNickname;

    @Schema(description = "发送者等级")
    private double sendUserRank;

    @Schema(description = "发送者用户信息")
    private String sendUser;

    private int receiveUserID;  // 接受者序列号

    @Schema(description = "接受者头像URL")
    private String receiveUserAvatar;

    @Schema(description = "接受者昵称")
    private String receiveUserNickname;

    @Schema(description = "接受者等级")
    private double receiveUserRank;

    @Schema(description = "评论当前状态")
    private String status = "NORMAL";

    @Schema(description = "评论附加信息")
    private String elseInformation;

    @Schema(description = "记录文章评论发布时的IP属地")
    private String location;

    @Schema(description = "文章评论是否已被逻辑删除")
    @TableLogic
    private Integer is_deleted;


}