package com.fltv.web.service.monitor.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/20 下午 1:33:33
 * @description MySQL数据库表状态信息PO数据
 * @filename MysqlTableStatus.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MysqlTableStatus implements Serializable {

    private String name; // 2024-12-20  13:33-表名
    private String engine; // 2024-12-20  13:33-存储引擎
    private String version; // 2024-12-20  13:33-版本
    private String rowFormat; // 2024-12-20  13:33-行格式
    private Long rows; // 2024-12-20  13:33-行数
    private Long avgRowLength; // 2024-12-20  13:34-平均行长度
    private Long dataLength; // 2024-12-20  13:34-数据长度
    private Long maxDataLength; // 2024-12-20  13:34-最大数据长度
    private Long indexLength; // 2024-12-20  13:34-索引长度
    private Long dataFree; // 2024-12-20  13:34-空闲空间
    private Long autoIncrement; // 2024-12-20  13:34-自增列的下一个值
    private LocalDateTime createTime; // 2024-12-20  13:34-创建时间
    private LocalDateTime updateTime; // 2024-12-20  13:34-更新时间
    private LocalDateTime checkTime; // 2024-12-20  13:34-检查时间
    private String collation; // 2024-12-20  13:34-字符集和校对规则
    private String checksum; // 2024-12-20  13:34-校验和
    private String createOptions; // 2024-12-20  13:34-创建选项
    private String comment; // 2024-12-20  13:34-注释

}