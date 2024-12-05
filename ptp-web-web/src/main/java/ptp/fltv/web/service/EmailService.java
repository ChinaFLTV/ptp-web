package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/4 PM 10:45:58
 * @description 邮箱服务接口
 * @filename EmailService.java
 */

public interface EmailService {


    /**
     * @param email 待发送到的邮箱地址
     * @return 如果邮件发送成功 , 则返回生成的验证码(前端校验用户提供的验证码是否正确所需) , 否则 , 若生成或发送失败 , 则返回null
     * @apiNote 注意 : 服务端建议验证码的有效期为5分钟 , 超时则作废 , 但这一切的有效性判断都应由前端来处理 , 即可能会出现验证码已严重超时 , 但用户意外地成功发送表单到服务侧且服务端还验证通过的意外情况
     * @author Lenovo/LiGuanda
     * @date 2024/12/4 PM 11:03:06
     * @version 1.0.0
     * @description 发送一条随机一次性验证码邮件到指定邮箱
     * @filename EmailService.java
     */
    String sendSingleEmailWithVerificationCode(@Nonnull String email);


}