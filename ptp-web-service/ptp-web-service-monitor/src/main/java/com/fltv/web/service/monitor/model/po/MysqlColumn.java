package com.fltv.web.service.monitor.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/20 下午 1:18:37
 * @description 单个MySQL数据库表的列信息PO数据
 * @filename MysqlColumn.java
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MysqlColumn implements Serializable {


    private String tableCatalog; // 2024-12-20  13:18-表目录
    private String tableSchema; // 2024-12-20  13:19-表所在的数据库名称
    private String tableName; // 2024-12-20  13:19-表名称
    private String columnName; // 2024-12-20  13:19-列名称
    private int ordinalPosition; // 2024-12-20  13:19-列的顺序位置
    private String columnDefault; // 2024-12-20  13:19-列的默认值
    private String isNullable; // 2024-12-20  13:19-列是否允许为空
    private String dataType; // 2024-12-20  13:19-列的数据类型
    private Long characterMaximumLength; // 2024-12-20  13:19-字符最大长度
    private Long characterOctetLength; // 2024-12-20  13:19-字符八位长度
    private Long numericPrecision; // 2024-12-20  13:19-数值精度
    private Long numericScale; // 2024-12-20  13:19-数值范围
    private Long datetimePrecision; // 2024-12-20  13:19-日期时间精度
    private String characterSetName; // 2024-12-20  13:19-字符集名称
    private String collationName; // 2024-12-20  13:19-校对规则名称
    private String columnType; // 2024-12-20  13:19-列类型
    private String columnKey; // 2024-12-20  13:19-列的键类型(如PRI、UNI、 MUL)
    private String extra; // 2024-12-20  13:19-额外信息(如auto_increment)
    private String privileges; // 2024-12-20  13:19-列的权限
    private String columnComment; // 2024-12-20  13:19-列的注释
    private String generationExpression; // 2024-12-20  13:19-生成的表达式


}