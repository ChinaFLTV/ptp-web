package com.fltv.web.service.monitor.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 5:20:11
 * @description 单个MySQL连接进程信息PO数据
 * @filename ProcessListEntry.java
 */

@Schema(description = "单个MySQL连接进程信息PO数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessListEntry {


    private Long id; // 2024-12-9  17:58-该进程程序登录MySQL时 , 系统分配的连接id , 即为connection_id
    private String user; // 2024-12-9  17:59-该进程程序连接MySQL的用户
    private String host; // 2024-12-9  17:59-该进程程序连接MySQL的IP
    private String db; // 2024-12-9  18:00-该进程程序连接MySQL的某个数据库
    private String command; // 2024-12-9  18:01-该进程程序执行的命令 , 取值为休眠(Sleep)/查询(Query)/连接(Connect)等
    private long time; // 2024-12-9  18:01-Command状态持续的时间 , 单位为秒
    private String state; // 2024-12-9  18:02-使用当前的sql语句的状态 , 如starting
    private String info; // 2024-12-9  18:02-显示sql语句 , 如当前执行了 show full processlist


}