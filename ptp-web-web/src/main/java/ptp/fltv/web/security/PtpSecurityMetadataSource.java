package ptp.fltv.web.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/5 下午 10:28:49
 * @description 生成自定义的权限校验规则
 * @filename PtpSecurityMetadataSource.java
 */

@Component
public class PtpSecurityMetadataSource implements SecurityMetadataSource {


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getRequest();


        return null;

    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        return null;

    }


    @Override
    public boolean supports(Class<?> clazz) {

        return true;

    }


}
