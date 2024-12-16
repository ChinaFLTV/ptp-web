package com.fltv.web.service.monitor.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/16 PM 9:39:07
 * @description 单个数据库表的信息PO数据
 * @filename TableInfo.java
 */

@Schema(description = "单个数据库表的信息PO数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableInfo implements Serializable {


    private String tableCatalog;
    private String tableName; // 2024-12-16  21:57-表名称
    private String tableSchema; // 2024-12-16  21:57-表所在的数据库名称
    private String tableType; // 2024-12-16  21:57-表的类型（例如：BASE TABLE, VIEW等）
    private String engine; // 2024-12-16  21:57-表使用的存储引擎
    private long version; // 2024-12-16  21:57-表的版本号
    private String rowFormat; // 2024-12-16  21:57-行格式(例如 : Dynamic/Fixed/Compressed等)
    private long tableRows; // 2024-12-16  21:57-估计的行数
    private long avgRowLength; // 2024-12-16  21:57-平均行长度
    private long dataLength; // 2024-12-16  21:57-数据大小
    private long maxDataLength; // 2024-12-16  21:57-最大数据大小
    private long indexLength; // 2024-12-16  21:57-索引大小
    private long dataFree; // 2024-12-16  21:57-空闲空间大小
    private long autoIncrement; // 2024-12-16  21:57-自增列的下一个值
    private LocalDateTime createTime; // 2024-12-16  21:57-表的创建时间
    private LocalDateTime updateTime; // 2024-12-16  21:57-表的最后更新时间
    private LocalDateTime checkTime; // 2024-12-16  21:57-表的最后检查时间
    private String tableCollation; // 2024-12-16  21:57-表的默认字符集和校对规则
    private String checksum; // 2024-12-16  21:57-表的校验和
    private String createOptions; // 2024-12-16  21:57-创建表时的选项
    private String tableComment; // 2024-12-16  21:57-表的注释

}