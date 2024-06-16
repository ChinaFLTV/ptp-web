package ptp.fltv.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import pfp.fltv.common.exceptions.PtpException;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/6/9 PM 11:32:29
 * @description 通过AOP的方式对方法的执行时的运行时状态进行检测
 * @filename RuntimeCheckAspect.java
 */


@Slf4j
@Aspect
@Component
public class RuntimeCheckAspect {


    @Pointcut("@annotation(pfp.fltv.common.annotation.CheckCostTime)")
    public void costTimeMethods() {


    }


    /*private AtomicLong totalCost = new AtomicLong(0);
    private AtomicInteger totalTimes = new AtomicInteger(0);
    private AtomicLong maxCost = new AtomicLong(0);
    private AtomicLong minCost = new AtomicLong(Long.MAX_VALUE);
    private AtomicInteger gt2000count = new AtomicInteger(0);
    private AtomicInteger gt1000count = new AtomicInteger(0);
    private AtomicInteger gt600count = new AtomicInteger(0);*/


    @Around("costTimeMethods()")
    public Object calculateCostTime(ProceedingJoinPoint joinPoint) {

        // 2024-6-9  23:47-先获取到执行方法的详细信息
        Object[] methodArgs = joinPoint.getArgs();// 被代理的方法的装填的参数
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();// 被代理的方法的名称

        String className = joinPoint.getTarget().getClass().getName();// 被代理的方法所附属的类的信息

        long start = 0L;
        try {

            // 2024-6-9  23:48-尽量让方法开始/结束事件的统计靠近方法的执行，以得到尽可能准确的耗时估计
            start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();

            /*long newTotalCost = totalCost.addAndGet(end - start);
            int newTotalTimes = totalTimes.incrementAndGet();
            maxCost.set(Long.max(maxCost.get(), end - start));
            minCost.set(Long.min(minCost.get(), end - start));
            if ((end - start) >= 2000) {

                int gt2000 = gt2000count.incrementAndGet();

            } else if ((end - start) >= 1000) {

                int gt1000 = gt1000count.incrementAndGet();

            } else if ((end - start) >= 600) {

                int gt600 = gt600count.incrementAndGet();

            }
            log.info("平均耗时：{} ms", newTotalCost / newTotalTimes);
            log.info("最小耗时：{} ms", minCost.get());
            log.info("最大耗时：{} ms", maxCost.get());
            log.info("耗时超过2000ms的个数：{}", gt2000count.get());
            log.info("耗时超过1000ms的个数：{}", gt1000count.get());
            log.info("耗时超过600ms的个数：{}", gt600count.get());*/

            log.info("[{}] 类的 [{}] 方法执行耗时 : {} ms", className, methodName, end - start);
            log.info("[{}] 类的 [{}] 方法执行结果 : {}", className, methodName, result);
            return result;

        } catch (Throwable e) {

            long end = System.currentTimeMillis();
            log.warn("[{}] 类的 [{}] 方法执行耗时 : {} ms", className, methodName, end - start + "");
            log.warn("[{}] 类的 [{}] 方法执行出错 : {}", className, methodName, e.getLocalizedMessage());

            /*long newTotalCost = totalCost.addAndGet(end - start);
            int newTotalTimes = totalTimes.incrementAndGet();
            maxCost.set(Long.max(maxCost.get(), end - start));
            minCost.set(Long.min(minCost.get(), end - start));
            if ((end - start) >= 2000) {

                int gt2000 = gt2000count.incrementAndGet();

            } else if ((end - start) >= 1000) {

                int gt1000 = gt1000count.incrementAndGet();

            } else if ((end - start) >= 600) {

                int gt600 = gt600count.incrementAndGet();

            }
            log.info("平均耗时：{} ms", newTotalCost / newTotalTimes);
            log.info("最小耗时：{} ms", minCost.get());
            log.info("最大耗时：{} ms", maxCost.get());
            log.info("耗时超过2000ms的个数：{}", gt2000count.get());
            log.info("耗时超过1000ms的个数：{}", gt1000count.get());
            log.info("耗时超过600ms的个数：{}", gt600count.get());*/

            throw new PtpException(813, e.getLocalizedMessage());

        }

    }


}


