package ptp.fltv.web.service.impl;

import org.springframework.stereotype.Service;
import pfp.fltv.common.utils.NetworkUtils;
import ptp.fltv.web.service.SystemService;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/25 PM 9:31:57
 * @description 系统服务接口实现
 * @filename SystemServiceImpl.java
 */

@Service
public class SystemServiceImpl implements SystemService {


    @Override
    public String getServerIp() {

        return NetworkUtils.getRealIP();

    }


}
