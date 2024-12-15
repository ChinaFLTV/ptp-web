package com.fltv.web.service.monitor.service;

import com.fltv.web.service.monitor.model.po.RedisKeyValueInfo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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


    /**
     * @param id  待查询的Redis数据库的ID
     * @param key 待查询的单个Redis键
     * @return 查询到的指定key-value键值对的详细信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/14 PM 2:07:37
     * @version 1.0.0
     * @description 查询指定ID的Redis数据库的单个键值对的详细信息
     * @apiNote 这里返回的映射的主要构成 : key(键名) , value(值内容) , ttl(过期时间) , count(值的长度 , 主要对于集合而言 , 标识集合的长度) , size(值内容的大小 , 主要对于字符串类型的value而言 , 单位是Byte) , type(值类型)
     * @filename RedisService.java
     */
    RedisKeyValueInfo querySingleKeyValuePairInfo(@Nonnull Long id, @Nonnull String key);


    /**
     * @param id   待操作的Redis数据库的ID
     * @param info 添加后的单个键值对信息数据包(key、value、ttl字段必须填写)
     * @return 是否添加键值对成功
     * @author Lenovo/LiGuanda
     * @date 2024/12/14 PM 5:01:11
     * @version 1.0.0
     * @description 添加指定ID的Redis数据库指定的单个键值对
     * @filename RedisService.java
     */
    boolean insertSingleKeyValuePair(Long id, RedisKeyValueInfo info);


    /**
     * @param id     待操作的Redis数据库的ID
     * @param oldKey 待更新的单个Redis键(旧键名)(该字段可为空 , 主要为了后面搭建 添加键值对 API接口复用此API所需)
     * @param info   更新后的单个键值对信息数据包(key、value、ttl字段必须填写)
     * @return 是否更新键值对成功
     * @author Lenovo/LiGuanda
     * @date 2024/12/14 PM 3:58:51
     * @version 1.0.0
     * @description 更新指定ID的Redis数据库指定的单个键值对
     * @filename RedisService.java
     */
    boolean updateSingleKeyValuePair(@Nonnull Long id, @Nullable String oldKey, @Nonnull RedisKeyValueInfo info);


    /**
     * @param id  待操作的Redis数据库的ID
     * @param key 待删除的单个Redis键
     * @return 被删除的键值对的数量
     * @author Lenovo/LiGuanda
     * @date 2024/12/14 PM 3:47:22
     * @version 1.0.0
     * @description 删除指定ID的Redis数据库指定的单个键值对
     * @filename RedisService.java
     */
    long deleteSingleKeyValuePair(@Nonnull Long id, @Nonnull String key);


    /**
     * @param id 待执行清空操作的Redis数据库的ID(若想清空全部 , 则请置此字段的值为-1)
     * @return 是否成功清空指定数据库/全部数据库的全部键值对
     * @author Lenovo/LiGuanda
     * @date 2024/12/15 PM 2:22:20
     * @version 1.0.0
     * @description 删除指定ID/或全部ID的Redis数据库的全部键值对
     * @filename RedisService.java
     */
    String flushDB(@Nonnull Long id);


}