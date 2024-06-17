package ptp.fltv.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserLoginVo;

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
     * @implNote 前端传过来的UserLoginVo中的LoginInfo中的DeviceInfo中的deviceID必须分Web端、移动端、PC端分别进行实现，
     * 且各端的实现方式必须统一，生成的机器码必须是唯一的且对于同一台机器所生成的机器码是固定的，deviceID格式为 {WEB/MOBILE/PC}:生成的随机序列号。
     * 注意1：你生成的设备码中只能含有a-bA-Z0-9这些字符，长度不能太长。
     * 注意2：生成的deviceID需要通过JWT进行签名后再以Cookie的形式发送过来、
     * @filename UserService.java
     */
    Map<String, Object> login(@Nonnull UserLoginVo userLoginVo) throws PtpException;


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


}