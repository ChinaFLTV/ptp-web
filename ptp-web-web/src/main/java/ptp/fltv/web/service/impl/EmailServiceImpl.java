package ptp.fltv.web.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ptp.fltv.web.service.EmailService;
import ptp.fltv.web.utils.RandomUtils;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/4 PM 10:47:49
 * @description 邮箱服务接口的实现类
 * @filename EmailServiceImpl.java
 */

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {


    private final MailAccount mailAccount;
    private final TemplateEngine templateEngine;


    @Override
    public String sendSingleEmailWithVerificationCode(@Nonnull String email) {

        String verificationCode = RandomUtils.generateRandomVerificationCode();

        Context context = new Context();
        context.setVariable("operateType", "修改邮箱");
        context.setVariable("verificationCode", verificationCode.split(""));

        String processedEmailHtmlContent = templateEngine.process("verification_code", context);

        String messageId = MailUtil.send(mailAccount, email, "邮箱验证码", processedEmailHtmlContent, true);
        if (StringUtils.hasLength(messageId)) {

            return verificationCode;

        } else {

            return null;

        }

    }


}