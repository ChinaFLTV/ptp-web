package com.fltv.web.service.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.model.po.TableInfo;
import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Asset;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 4:07:55
 * @description MySQL数据库监控服务接口
 * @filename MySqlService.java
 */

public interface MySqlService extends IService<Asset> {


    /**
     * @param id 待查询的MySQL数据库的ID
     * @return 指定ID的MySQL数据库的连接状态信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 4:11:44
     * @version 1.0.0
     * @description 获取指定ID的MySQL数据库的连接状态信息
     * @filename MySqlService.java
     */
    Map<String, Integer> getConnectionsStatistics(@Nonnull Long id);


    /**
     * @param id 待查询的MySQL数据库的ID
     * @return 指定ID的MySQL数据库的活跃进程列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 7:34:32
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的当前活跃的连接的详细状态信息列表
     * @filename MySqlService.java
     */
    List<ProcessListEntry> getFullProcessListById(@Nonnull Long id);


    /**
     * @param id 待查询的MySQL数据库的ID
     * @return 指定ID的MySQL数据库的状态信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 8:12:21
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的状态信息
     * @filename MySqlService.java
     */
    Map<String, Object> getStatusById(@Nonnull Long id);


    /**
     * @param id 待查询的MySQL数据库的ID
     * @return 指定ID的MySQL数据库的系统变量信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 9:03:15
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的系统变量信息
     * @filename MySqlService.java
     */
    Map<String, Object> getVariablesById(@Nonnull Long id);


    /**
     * @param id 待查询的MySQL数据库的ID
     * @return 指定ID的MySQL数据库的基本状态信息
     * @throws InterruptedException 当前线程休眠过程中发生错误时将抛出该异常
     * @apiNote 注意 : 该方法需要休眠1s , 因此该方法会比较耗时 , 请前端酌情请求
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 1:01:42
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的基本状态信息
     * @filename MySqlService.java
     */
    Map<String, Object> getBaseStatusById(@Nonnull Long id) throws InterruptedException;


    /**
     * @param id       待查询的MySQL数据库的ID
     * @param database 待查询的数据库名
     * @return 查询到的指定数据库下的全部表的信息构成的集合
     * @apiNote 该API返回的列表默认按照表数据大小&索引数据大小之和进行倒序排序的(若相等 , 则继续按照表创建时间倒序排序)
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 9:43:24
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的指定数据库的全部表的信息
     * @filename MySqlService.java
     */
    List<TableInfo> queryAllTableInfoInTargetDatabase(@Nonnull Long id, @Nonnull String database);


    /**
     * @param id    待查询的MySQL数据库的ID
     * @param count 所要查询的数据库的数量
     * @return 按照占用磁盘空间大小倒序排序后的数据库名与其所占磁盘空间大小(单位是Byte)的映射
     * @author Lenovo/LiGuanda
     * @date 2024/12/19 下午 12:57:03
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的各个数据库的占用磁盘空间大小
     * @filename MySqlService.java
     */
    Map<String, Long> queryAllDatabaseSizes(@Nonnull Long id, @Nonnull Long count);


    /**
     * @param id     待查询的MySQL数据库的ID
     * @param dbName 待查询的数据库名(若想查询全部数据库的表 , 则请将此字段置空)
     * @param count  所要查询的表的数量
     * @return 指定ID的MySQL数据库下的指定/全部数据库的指定数量的表按照占用磁盘空间大小倒序排序后的表名与其所占磁盘空间大小(单位是Byte)的映射
     * @author Lenovo/LiGuanda
     * @date 2024/12/19 下午 1:54:31
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的指定/全部数据库下的各张表的占用磁盘空间大小
     * @filename MySqlService.java
     */
    Map<String, Long> queryAllTableSizes(@Nonnull Long id, @Nonnull String dbName, @Nonnull Long count);


}