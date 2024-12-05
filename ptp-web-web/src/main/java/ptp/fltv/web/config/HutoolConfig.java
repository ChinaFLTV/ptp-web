package ptp.fltv.web.config;

import cn.hutool.extra.mail.MailAccount;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/4 PM 11:51:51
 * @description Hutool相关的自定义配置类
 * @filename HutoolConfig.java
 */

@Configuration
public class HutoolConfig {


    @Value("${hutool.email.host}")
    private String host;
    @Value("${hutool.email.port}")
    private Integer port;
    @Value("${hutool.email.auth}")
    private Boolean auth;
    @Value("${hutool.email.ssl-enable}")
    private Boolean sslEnable;
    @Value("${hutool.email.starttls-enable}")
    private Boolean starttlsEnable;
    @Value("${hutool.email.from}")
    private String from;
    @Value("${hutool.email.user}")
    private String user;
    @Value("${hutool.email.pass}")
    private String pass;


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/12/4 PM 11:52:36
     * @version 1.0.0
     * @description 自定义的邮件服务器信息Bean实体
     * @filename HutoolConfig.java
     */
    @Bean
    public MailAccount mailAccount() throws GeneralSecurityException {

        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(host);
        mailAccount.setPort(port);
        mailAccount.setAuth(auth);
        mailAccount.setStarttlsEnable(starttlsEnable);
        mailAccount.setSslEnable(sslEnable); // 2024-12-5  13:02-在使用QQ或Gmail邮箱时 , 需要强制开启SSL支持
        mailAccount.setFrom(from);
        mailAccount.setUser(user);
        mailAccount.setPass(pass);

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);
        mailAccount.setCustomProperty("mail.smtp.ssl.protocols", "TLSv1.2"); // 2024-12-5  17:18-解决出现 Could not convert socket to TLS 的问题

        return mailAccount;

    }


}