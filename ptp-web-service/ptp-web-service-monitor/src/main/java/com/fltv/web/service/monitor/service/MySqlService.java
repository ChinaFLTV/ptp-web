package com.fltv.web.service.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fltv.web.service.monitor.model.po.MysqlColumn;
import com.fltv.web.service.monitor.model.po.MysqlTableStatus;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.model.po.TableInfo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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


    /**
     * @param id        待查询的MySQL数据库的ID
     * @param tableName 待查询的数据表名
     * @return 查询到的指定数据表的全部列的完整信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/20 下午 1:41:19
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的单个表的结构信息
     * @filename MySqlService.java
     */
    List<MysqlColumn> querySingleTableStructure(@Nonnull Long id, @Nonnull String tableName);


    /**
     * @param id        待查询的MySQL数据库的ID
     * @param tableName 待查询的数据表名
     * @return 查询到的指定数据表的状态信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/20 下午 1:45:05
     * @version 1.0.0
     * @description 查询指定ID的MySQL数据库的单个表的状态信息
     * @filename MySqlService.java
     */
    MysqlTableStatus querySingleTableStatus(@Nonnull Long id, @Nonnull String tableName);


    /**
     * @param id           待查询的MySQL数据库的ID
     * @param databaseName 待查询的数据库名
     * @param tableName    待查询的数据表名
     * @param sortField    用于排序的字段名(不排序请留空)
     * @param asc          是否按照给定的排序字段升序排序(若没有提供排序字段 , 则此参数无效)
     * @param offset       数据截取的起始偏移量
     * @param count        数据截取的数量
     * @return 查询到的符合条件的指定实体的映射构成的列表集合
     * @author Lenovo/LiGuanda
     * @date 2024/12/20 下午 4:43:30
     * @version 1.0.0
     * @description 按照指定的排序规则批量查询指定ID的MySQL数据库的指定表的指定区域的数据
     * @filename MySqlService.java
     */
    List<Map<String, Object>> querySingleTableData(@Nonnull Long id, @Nonnull String databaseName, @Nonnull String tableName, @Nullable String sortField, @Nullable Boolean asc, @Nonnull Long offset, @Nonnull Long count);


    /**
     * @param id           待查询的MySQL数据库的ID
     * @param databaseName 待操作的数据库名
     * @param tableName    待操作的数据表名
     * @param modelId      待更新的条目的主键ID(请确保该ID能唯一标识一个条目)
     * @param fieldName    待更新的字段名
     * @param fieldValue   待更新的字段值(可为空值)
     * @return 是否成功更新了指定条目的指定字段为指定值
     * @author Lenovo/LiGuanda
     * @date 2024/12/20 下午 10:25:48
     * @version 1.0.0
     * @description 更新指定ID的MySQL数据库的指定表的指定单个条目的单个字段的数据
     * @filename MySqlService.java
     */
    boolean updateSingleTableFieldData(@Nonnull Long id, @Nonnull String databaseName, @Nonnull String tableName, @Nonnull Long modelId, @Nonnull String fieldName, @Nullable String fieldValue);


    /**
     * @param id           待操作的MySQL数据库的ID
     * @param databaseName 待操作的数据库名
     * @param tableName    待操作的数据表名
     * @param modelId      待删除的条目的主键ID(请确保该ID能唯一标识一个条目)
     * @return 是否成功删除了指定条目
     * @author Lenovo/LiGuanda
     * @date 2024/12/20 下午 11:05:59
     * @version 1.0.0
     * @description 删除指定ID的MySQL数据库的指定表的指定单个条目
     * @filename MySqlService.java
     */
    boolean deleteSingleTableData(@Nonnull Long id, @Nonnull String databaseName, @Nonnull String tableName, @Nonnull Long modelId);


}