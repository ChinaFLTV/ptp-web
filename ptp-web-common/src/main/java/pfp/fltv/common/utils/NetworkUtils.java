package pfp.fltv.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/8/24 PM 6:05:30
 * @description 与网络状态相关的工具类库
 * @filename NetworkUtils.java
 */

@Slf4j
public class NetworkUtils {


    /**
     * @return 本机的真实物理IP(非私有IP)
     * @author Lenovo/LiGuanda
     * @date 2024/8/24 PM 6:06:28
     * @version 1.0.0
     * @apiNote <a href="https://blog.csdn.net/weixin_42500220/article/details/106599416">方法来源</a>
     * @description 获取本机的外网IP
     * @filename NetworkUtils.java
     */
    public static String getRealIP() {

        try {

            //获取到所有的网卡
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {

                NetworkInterface netInterface = allNetInterfaces.nextElement();

                // 去除回环接口127.0.0.1，子接口，未运行的接口
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {

                    continue;

                }

                //获取名称中是否包含 Intel Realtek 的网卡
                if (!netInterface.getDisplayName().contains("Intel") && !netInterface.getDisplayName().contains("Realtek") && !netInterface.getDisplayName().contains("Atheros") && !netInterface.getDisplayName().contains("Broadcom")) {

                    continue;

                }

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                // System.out.println(netInterface.getDisplayName());

                while (addresses.hasMoreElements()) {

                    InetAddress ip = addresses.nextElement();

                    if (ip != null) {

                        if (ip instanceof Inet4Address) {

                            // System.out.println(ip.getHostName());
                            return ip.getHostAddress();

                        }

                    }

                }

                break;

            }

        } catch (SocketException ex) {

            log.error("[{}] : {} occurred : {}", "ptp-web-web", ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());

        }

        return null;

    }


}