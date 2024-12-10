package com.fltv.web.service.monitor.service;

import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 9:26:49
 * @description Redis数据库监控服务接口
 * @filename RedisService.java
 */

public interface RedisService {


    /**
     * @param id 待查询的Redis数据库的ID
     * @return 指定ID的Redis数据库的状态信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 10:10:58
     * @version 1.0.0
     * @description 查询指定ID的Redis数据库的状态信息
     * @filename RedisService.java
     */
    Map<String, Object> queryInfoById(@Nonnull Long id);


    /**
     * @param id      待被执行的Redis数据库的ID
     * @param command 需要被执行的命令(格式要求 : 命令各部分必须且仅能以空格进行分隔(空格数量不限) , 命令结尾不需要添加任何符号)
     * @return 命令执行的结果
     * @author Lenovo/LiGuanda
     * @date 2024/12/10 AM 12:48:22
     * @version 1.0.0
     * @description 在ID的Redis数据库上执行指定的单条命令
     * @filename RedisService.java
     */
    Object runSingleCommand(@Nonnull Long id, @Nonnull String command);


    /**
     * @param id 待查询的Redis数据库的ID
     * @return 指定ID的Redis数据库的全部键值对数据
     * @author Lenovo/LiGuanda
     * @date 2024/12/10 PM 3:36:00
     * @apiNote 注意 : 由于该API需要一次性阻塞式获取指定Redis数据库中现存的全部key-value信息 , 因此执行会比较耗时 , 同时还会阻塞住目标Redis数据库一段时间
     * @version 1.0.0
     * @description 查询指定ID的Redis数据库的全部键值对数据
     * @filename RedisService.java
     */
    Map<String, Object> queryAllKeyValuePairsById(@Nonnull Long id);


}