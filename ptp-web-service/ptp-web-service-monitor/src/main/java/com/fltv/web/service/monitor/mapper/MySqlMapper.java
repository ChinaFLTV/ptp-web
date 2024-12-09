package com.fltv.web.service.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fltv.web.service.monitor.model.po.DatabaseStatus;
import com.fltv.web.service.monitor.model.po.ProcessListEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pfp.fltv.common.model.po.manage.Asset;

import java.util.List;

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


}