package ptp.fltv.web.service.job.worker;

import jakarta.annotation.Nonnull;
import pfp.fltv.common.exceptions.PtpException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:32:02
 * @description 用于进行ID相关数据操作的工人
 * @implNote 调用与实现相分离，以按照依赖倒置原则对服务调用与服务具体实现解耦(高层模块不应该依赖于低层模块，两者都应该依赖于抽象接口，即抽象不应该依赖于细节，而细节应该依赖于抽象)
 * @filename IdWorker.java
 */

public interface IdWorker {


    /**
     * @param infix ID所属key的中缀(位置介于goods:kill前缀之后&日期后缀之前)
     * @return 商品秒杀的流水号
     * @author Lenovo/LiGuanda
     * @date 2024/5/19 PM 9:55:06
     * @version 1.0.0
     * @description 生成商品秒杀的流水号
     * @filename IdWorker.java
     */
    long generateGoodsKillId(@Nonnull String infix) throws PtpException;


}