package com.fltv.web.service.monitor.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/9 PM 8:35:36
 * @description 数据库状态PO数据
 * @filename DatabaseStatus.java
 */

@Schema(description = "数据库状态PO数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseStatus implements Serializable {


    private String variableName;
    private String value;


}