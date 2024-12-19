package com.fltv.web.service.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fltv.web.service.monitor.model.po.DatabaseStatus;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import com.fltv.web.service.monitor.model.po.TableInfo;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pfp.fltv.common.model.po.manage.Asset;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 3:44:14
 * @description MySQL数据库映射器(用于查询MySQL数据库的状态)
 * @apiNote 注意 : 这里选择Asset作为操作实体仅仅是MyBatisPlus编写代码所需 , 并无其他含义
 * @filename MySqlMapper.java
 */

@Mapper
public interface MySqlMapper extends BaseMapper<Asset> {


    /**
     * @return 指定ID的MySQL数据库的最大连接数
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 4:00:22
     * @version 1.0.0
     * @description 查看指定ID的MySQL数据库的最大连接数
     * @filename MySqlMapper.java
     */
    @Select("select @@global.max_connections")
    Integer getMaxConnections();


    /**
     * @return 指定ID的MySQL数据库的当前活跃连接数
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 4:42:02
     * @version 1.0.0
     * @description 查看指定ID的MySQL数据库的当前活跃连接数
     * @filename MySqlMapper.java
     */
    @Select("select COUNT(*) from information_schema.PROCESSLIST where COMMAND != 'Sleep'")
    Integer getActiveConnections();


    /**
     * @return 指定ID的MySQL数据库的活跃进程列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 5:03:55
     * @version 1.0.0
     * @description 查看指定ID的MySQL数据库的活跃进程列表
     * @filename MySqlMapper.java
     */
    @Select("show full processlist ")
    List<ProcessListEntry> getFullProcessList();


    /**
     * @return 指定ID的MySQL数据库的状态信息
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 8:09:16
     * @version 1.0.0
     * @description 查看指定ID的MySQL数据库的状态信息
     * @filename MySqlMapper.java
     */
    @Select("show status")
    List<DatabaseStatus> getStatus();


    /**
     * @return 指定ID的MySQL数据库的系统变量集合
     * @author Lenovo/LiGuanda
     * @date 2024/12/9 PM 8:56:09
     * @version 1.0.0
     * @description 查看指定ID的MySQL数据库的系统变量集合
     * @filename MySqlMapper.java
     */
    @Select("show variables")
    List<DatabaseStatus> getVariables();


    /**
     * @return 指定ID的MySQL数据库的连接数
     * @author Lenovo/LiGuanda
     * @date 2024/12/15 PM 10:39:55
     * @version 1.0.0
     * @description 获取指定ID的MySQL数据库的连接数
     * @filename MySqlMapper.java
     */
    @Select("show status where `Variable_name` = 'Threads_connected'")
    DatabaseStatus getConnectionNum();


    /**
     * @return 指定ID的MySQL数据库的查询数
     * @apiNote 该查询到的值为 自数据库启动以来执行的查询总数
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 6:18:46
     * @version 1.0.0
     * @description 获取指定ID的MySQL数据库的查询数
     * @filename MySqlMapper.java
     */
    @Select("show status like 'Questions'")
    DatabaseStatus getQueryNum();


    /**
     * @return 指定ID的MySQL数据库中2的指定数据库下的全部表的内存占用大小(单位为字节)
     * @author Lenovo/LiGuanda
     * @date 2024/12/16 PM 9:20:49
     * @version 1.0.0
     * @description 获取指定ID的MySQL数据库中2的指定数据库下的全部表的内存占用大小
     * @filename MySqlMapper.java
     */
    @Select("select * " +
            "from information_schema.TABLES " +
            "where table_schema = #{database}")
    List<TableInfo> getAllTableSizeInTargetDatabase(@Nonnull @Param("database") String database);


    /**
     * @return 当前指定ID的MySQL数据库中的全部表及其索引的总大小(单位是字节)
     * @author Lenovo/LiGuanda
     * @date 2024/12/17 下午 12:28:59
     * @version 1.0.0
     * @description 获取全部数据库的全部表及其索引的总大小
     * @filename MySqlMapper.java
     */
    @Select("select sum(DATA_LENGTH + INDEX_LENGTH) as total_size " +
            "from information_schema.TABLES")
    Long getAllTablesTotalSize();


    /**
     * @return 根据数据大小&索引数据大小之和倒序排序的以数据库名称&及其所占磁盘大小(单位是Byte)映射为元素的列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/19 下午 1:12:38
     * @version 1.0.0
     * @description 获取指定ID的MySQL数据库的各个数据库的占用磁盘空间大小
     * @filename MySqlMapper.java
     */
    @Select("select TABLE_SCHEMA as db_name, sum(DATA_LENGTH + INDEX_LENGTH) as db_size " +
            "from information_schema.TABLES " +
            "group by TABLE_SCHEMA " +
            "order by db_size desc " +
            "limit #{count} ")
    List<Map<String, Object>> getAllDatabaseSizes(@Nonnull @Param("count") Long count);


    /**
     * @param dbName 待查询的数据库名
     * @param count  所要查询的表的数量
     * @return 从指定数据库中查询到的根据数据大小&索引数据大小之和倒序排序的以表名&及其所占磁盘大小(单位是Byte)映射为元素的列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/19 下午 1:56:27
     * @version 1.0.0
     * @description 查询指定数据库下的指定数量表的占用磁盘空间大小
     * @filename MySqlMapper.java
     */
    @Select("select table_name                   as table_name, " +
            "       (data_length + index_length) as table_size " +
            "from information_schema.TABLES " +
            "where table_schema = #{dbName} " +
            "order by (data_length + index_length) desc " +
            "limit #{count} ")
    List<Map<String, Object>> getAllTableSizesInTargetDatabase(@Nonnull @Param("dbName") String dbName, @Nonnull @Param("count") Long count);


    /**
     * @param count 所要查询的表的数量
     * @return 从全部数据库中查询到的根据数据大小&索引数据大小之和倒序排序的以表名&及其所占磁盘大小(单位是Byte)映射为元素的列表
     * @author Lenovo/LiGuanda
     * @date 2024/12/19 下午 2:17:18
     * @version 1.0.0
     * @description 查询全部数据库下的指定数量表的占用磁盘空间大小
     * @filename MySqlMapper.java
     */
    @Select("select table_name                   as table_name, " +
            "       (data_length + index_length) as table_size " +
            "from information_schema.TABLES " +
            "order by (data_length + index_length) desc " +
            "limit #{count} ")
    List<Map<String, Object>> getAllTableSizesInAllDatabase(@Nonnull @Param("count") Long count);


}