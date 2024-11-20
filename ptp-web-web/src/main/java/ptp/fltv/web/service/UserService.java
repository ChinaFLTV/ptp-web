package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.enums.LoginClientType;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;
import pfp.fltv.common.model.vo.UserVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:11:44
 * @description 用户服务接口
 * @filename UserService.java
 */

public interface UserService extends IService<User> {


    /**
     * @param nickname 用户昵称
     * @return 获取到的用户数据，不存在则为null
     * @author Lenovo/LiGuanda
     * @date 2024/3/31 下午 7:50:05
     * @version 1.0.0
     * @description 根据用户昵称获取用户数据
     * @filename UserService.java
     */
    User getUserByNickname(@NonNull String nickname);


    /**
     * @param userLoginVo 用户登录的数据
     * @return 最终的登录结果，成功则为包含JWT的结果
     * @author Lenovo/LiGuanda
     * @date 2024/4/3 下午 8:26:09
     * @version 1.0.0
     * @description 执行用户登录成功后的业务逻辑
     * @implNote <pre>前端传过来的UserLoginVo中的LoginInfo中的DeviceInfo中的deviceID必须分Web端、移动端、PC端分别进行实现，
     * 且各端的实现方式必须统一，生成的机器码必须是唯一的且对于同一台机器所生成的机器码是固定的，deviceID格式为 {WEB/MOBILE/PC}:生成的随机序列号。
     * 注意1：你生成的设备码中只能含有a-bA-Z0-9这些字符，长度不能太长。
     * 注意2：生成的deviceID需要通过JWT进行签名后再以Cookie的形式发送过来、
     * 注意3：用户再调用该方法时需要：
     * (1)在请求体中添加登录用户的信息(必须包含UserLoginVo中的LoginInfo中的DeviceInfo中的deviceID)。
     * 注意4：后续的请求所需要携带的数据：
     * (1)登录后拿到的签名后的STORE_KEY数据(放在Header的Authorization字段中)。
     * (2)登录环境信息(用于控制单一物理设备登录)(放在Cookie中的login_client_info字段中)
     * </pre>
     * @filename UserService.java
     */
    Map<String, Object> loginByNicknameAndPassword(@Nonnull UserLoginVo userLoginVo) throws PtpException;


    /**
     * @param code 用户同意使用Github登录后返回的登录码
     * @return 获取到的用户的Github数据，不存在则为null
     * @author Lenovo/LiGuanda
     * @date 2024/4/7 下午 7:41:46
     * @version 1.0.0
     * @description 通过Github进行OAuth2登录
     * @filename UserService.java
     */
    String loginByGithub(@Nonnull String code);


    /**
     * @param addressInfo 用户当前位置信息数据包
     * @param id          用户ID
     * @return 成功更新当前用户的位置信息后得到的Redis的实时位置key中对应的成员(更新失败则为null)
     * @author Lenovo/LiGuanda
     * @date 2024/6/21 PM 10:29:45
     * @version 1.0.0
     * @implNote 这里我们仅向前端暴露这样的一个用于单独更新用户信息的API , 我们并不打算在后端负责用户位置的实时更新 ,
     * 还是那句话 , 一切交由客户端根据实际情况适时地去调用该API进行用户地理位置的修正更新 , 毕竟位置信息是敏感信息 , 必须要审慎对待
     * @description 更新当前用户的模糊的地理位置信息
     * @filename UserService.java
     */
    User refreshGeolocation(@Nonnull Long id, @Nonnull AddressInfo addressInfo);


    /**
     * @param id        当前用户ID
     * @param longitude 当前用户的所在经度
     * @param latitude  当前用户的所在纬度
     * @param radius    搜索半径(单位 : km)
     * @param limit     最大检索人数
     * @return 查询到的指定半径范围内的附近的人的信息集合(距离 = > 同距离的用户集合)
     * @author Lenovo/LiGuanda
     * @date 2024/6/21 PM 11:43:06
     * @version 1.0.0
     * @description 查找当前位置处指定半径内的附近的人
     * @filename UserService.java
     */
    Map<Integer, List<User>> findPeopleNearby(@Nonnull Long id, @Nonnull Double longitude, @Nonnull Double latitude, @Nonnull Double radius, @Nonnull Long limit);


    /**
     * @param userId     用户ID
     * @param clientType 当前需要进行登出操作的客户端类型(保证单端单登录/登出)
     * @param response   HTTP响应
     * @author Lenovo/LiGuanda
     * @date 2024/8/7 PM 3:03:31
     * @version 1.0.0
     * @description 登出用户并移除用户云端数据信息
     * @filename UserService.java
     */
    void logout(@Nonnull LoginClientType clientType, @Nonnull Long userId, @Nonnull HttpServletResponse response);


    /**
     * @param userId        用户ID
     * @param newAvatarFile 用户新头像的文件
     * @return 上传头像文件成功后的云文件的URI
     * @throws IOException          将用户传输上来的文件保存至服务端内部指定路径时失败可能会抛出此异常
     * @throws InterruptedException 将头像图片文件上传到腾讯云对象存储COS的过程中遭到意外阻断时将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/8/11 PM 5:23:38
     * @version 1.0.0
     * @description 更新用户的头像
     * @filename UserService.java
     */
    String updateAvatar(@Nonnull Long userId, @Nonnull MultipartFile newAvatarFile) throws IOException, InterruptedException;


    /**
     * @param userId   当前的用户ID
     * @param pageNum  查询的一页用户数据的起始偏移量
     * @param pageSize 查询的这一页用户数据的数量
     * @return 查询到的指定数据页上的粉丝列表数据
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 PM 8:51:11
     * @version 1.0.0
     * @description 批量(分页)查询某个用户的多条粉丝数据
     * @filename UserService.java
     */
    List<User> queryFollowerPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize);


    /**
     * @param userId   当前的用户ID
     * @param pageNum  查询的一页用户数据的起始偏移量
     * @param pageSize 查询的这一页用户数据的数量
     * @return 查询到的指定数据页上的粉丝VO列表数据
     * @author Lenovo/LiGuanda
     * @date 2024/11/4 PM 4:32:56
     * @version 1.0.0
     * @description 批量(分页)查询某个用户的多条粉丝VO数据
     * @filename UserService.java
     */
    List<UserVo> queryFollowerVoPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize);


    /**
     * @param userId   当前的用户ID
     * @param pageNum  查询的一页用户数据的起始偏移量
     * @param pageSize 查询的这一页用户数据的数量
     * @return 查询到的指定数据页上的被关注者列表数据
     * @author Lenovo/LiGuanda
     * @date 2024/10/31 PM 8:58:02
     * @version 1.0.0
     * @description 批量(分页)查询某个用户关注的多条用户数据
     * @filename UserService.java
     */
    List<User> queryFolloweePage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize);


    /**
     * @param userId   当前的用户ID
     * @param pageNum  查询的一页用户数据的起始偏移量
     * @param pageSize 查询的这一页用户数据的数量
     * @return 查询到的指定数据页上的被关注者VO列表数据
     * @author Lenovo/LiGuanda
     * @date 2024/11/4 PM 4:33:54
     * @version 1.0.0
     * @description 批量(分页)查询某个用户关注的多条用户VO数据
     * @filename UserService.java
     */
    List<UserVo> queryFolloweeVoPage(@Nonnull Long userId, @Nonnull Long pageNum, @Nonnull Long pageSize);


    /**
     * @param userId     待修改信息的用户ID
     * @param fieldName  待修改信息的所属字段
     * @param fieldValue 修改后的信息的字段值
     * @return 如果修改用户成功 , 则返回修改后的用户数据实体 ; 否则 , 修改失败则返回null
     * @author Lenovo/LiGuanda
     * @apiNote 注意 : 这里的字段名必须填写为数据库中的字段名称而非JavaBean的字段名 , 字段值请尽可能地以字段类型对应的格式完成入参
     * @apiNote 注意 : 如果单独修改用户的出生日期信息的话 , 将会同步更新用户的年龄信息
     * @date 2024/11/18 AM 1:04:15
     * @version 1.0.0
     * @description 修改用户单个字段的信息
     * @filename UserService.java
     */
    User updateSingleUserField(@Nonnull Long userId, @Nonnull String fieldName, @Nonnull Object fieldValue);


}