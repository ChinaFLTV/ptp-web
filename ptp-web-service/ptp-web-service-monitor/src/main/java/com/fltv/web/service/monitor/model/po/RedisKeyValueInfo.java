package com.fltv.web.service.monitor.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/14 PM 3:14:29
 * @description 存储单个Redis键值对的信息数据包(PO对象)
 * @filename RedisKeyValueInfo.java
 */

@Schema(description = "存储单个Redis键值对的信息数据包(PO对象)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisKeyValueInfo {


    private String key; // 2024-12-14  15:15-键名
    private Object value; // 2024-12-14  15:15-值内容
    private String type; // 2024-12-14  15:15-值类型
    private long ttl; // 2024-12-14  15:15-过期时间(单位为s , 数值为-1则表示永不过期 , 数值为-2则表示值不存在)
    private long count; // 2024-12-14  15:16-值的长度 , 主要对于集合而言 , 标识集合的长度
    private long size; // 2024-12-14  15:16-值内容的大小 , 主要对于字符串类型的value而言 , 单位是Byte
    private boolean exist; // 2024-12-14  15:18-键值对是否存在


}