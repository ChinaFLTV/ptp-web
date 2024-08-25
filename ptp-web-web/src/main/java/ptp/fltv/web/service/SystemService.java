package ptp.fltv.web.service;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/25 PM 9:30:49
 * @description 系统服务接口
 * @filename SystemService.java
 */

public interface SystemService {


    /**
     * @return 服务器的公网IP地址
     * @author Lenovo/LiGuanda
     * @date 2024/8/25 PM 9:31:08
     * @version 1.0.0
     * @description 获取服务器的真实物理IP地址
     * @implNote 开发环境使用
     * @filename SystemService.java
     */
    String getServerIp();


}