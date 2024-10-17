package ptp.fltv.web.dto;

import jakarta.annotation.Nonnull;
import pfp.fltv.common.model.po.manage.Report;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/17 PM 5:59:01
 * @description 举报DTO接口
 * @filename ReportDto.java
 */

public interface ReportDto {


    /**
     * @param report 需要保存的评论
     * @return 保存结果
     * @author Lenovo/LiGuanda
     * @date 2024/10/17 PM 6:52:18
     * @version 1.0.0
     * @apiNote 保存成功后将同时在传入的report对象中设置ID为云端插入后生成的ID
     * @description 保存单条评论数据
     * @filename ReportDto.java
     */
    boolean save(@Nonnull Report report);


}