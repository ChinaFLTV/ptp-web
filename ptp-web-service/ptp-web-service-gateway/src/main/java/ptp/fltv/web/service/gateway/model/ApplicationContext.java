package ptp.fltv.web.service.gateway.model;

import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.manage.User;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/5 PM 8:42:07
 * @description 用于存储单个请求的上下文信息，以便同一线程内的后续组件共享使用
 * @filename ApplicationContext.java
 */

public class ApplicationContext {


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/5 PM 8:49:43
     * @version 1.0.0
     * @description 存储请求所属的用户信息
     * @filename ApplicationContext.java
     */
    public static final ThreadLocal<User> USER = new ThreadLocal<>();
    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/5 PM 8:50:38
     * @version 1.0.0
     * @description 存储请求所属的角色信息
     * @filename ApplicationContext.java
     */
    public static final ThreadLocal<Role> ROLE = new ThreadLocal<>();


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/12 PM 10:28:22
     * @version 1.0.0
     * @description 当前状态是否为直接无条件放行状态，如果是，这将导致后序的安全处理器全部逻辑失效
     * @filename ApplicationContext.java
     */
    public static final ThreadLocal<Boolean> IS_PERMITTED_DIRECTLY = ThreadLocal.withInitial(() -> false);


    /**
     * @author Lenovo/LiGuanda
     * @date 2024/5/5 PM 9:02:25
     * @version 1.0.0
     * @description 清空当前所在线程缓存的上下文信息(建议响应返回时调用)
     * @filename ApplicationContext.java
     */
    public static void clear() {

        // 2024-5-5  22:29 TODO 别忘了在全局过滤器最终返回响应时进行该方法的调用!!!
        USER.remove();
        ROLE.remove();
        IS_PERMITTED_DIRECTLY.remove();

    }


}
