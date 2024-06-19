package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.vo.AnnouncementVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.service.AnnouncementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:10:30
 * @description 公告控制器
 * @filename AnnouncementController.java
 */

@AllArgsConstructor
@Tag(name = "公告操作接口")
@RestController
// @PreAuthorize("@pc.hasAnyPermission('content:announcement:add','content:announcement:remove','content:announcement:list','content:announcement:update')") // 2024-5-3  20:55-权限控制将委托给spring cloud gateway 处理
@RequestMapping("/content/announcement")
public class AnnouncementController {


    private static final String ES_PREFIX_ANNOUNCEMENT_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_ANNOUNCEMENT_URL;
    private static final String ES_INSERT_ANNOUNCEMENT_URL = ES_PREFIX_ANNOUNCEMENT_URL + "/insert/single";
    private static final String ES_UPDATE_ANNOUNCEMENT_URL = ES_PREFIX_ANNOUNCEMENT_URL + "/update/single";
    private static final String ES_DELETE_ANNOUNCEMENT_URL = ES_PREFIX_ANNOUNCEMENT_URL + "/delete/single/{id}";


    private AnnouncementService announcementService;
    private RestTemplate restTemplate;


    @LogRecord(description = "根据ID查询单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "根据ID查询单条公告数据")
    @GetMapping("/query/single/{id}")
    public Result<Announcement> querySingleAnnouncement(@Parameter(name = "id", description = "待查询的单条公告ID", in = ParameterIn.PATH) @PathVariable("id") Long id) {

        Announcement announcement = announcementService.getById(id);

        return (announcement == null) ? Result.failure(null) : Result.success(announcement);

    }


    @LogRecord(description = "批量(分页)查询多条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "批量(分页)查询多条公告数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<AnnouncementVo>> queryAnnouncementPage(@Parameter(name = "offset", description = "查询的一页公告数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset, @Parameter(name = "limit", description = "查询的这一页公告数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        Page<Announcement> announcementPage = new Page<>(offset, limit);
        announcementPage = announcementService.page(announcementPage);

        List<AnnouncementVo> announcementVos = new ArrayList<>();
        for (Announcement announcement : announcementPage.getRecords()) {

            AnnouncementVo announcementVo = new AnnouncementVo();
            BeanUtils.copyProperties(announcement, announcementVo);
            announcementVos.add(announcementVo);

        }

        return Result.success(announcementVos);

    }


    @LogRecord(description = "添加单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "添加单条公告数据")
    @PostMapping("/insert/single")
    public Result<?> insertSingleAnnouncement(@Parameter(name = "announcementVo", description = "待添加的单条公告数据VO") @RequestBody AnnouncementVo announcementVo) {

        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementVo, announcement);

        boolean isSaved = announcementService.save(announcement);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_ANNOUNCEMENT_URL, announcement, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @LogRecord(description = "修改单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "修改单条公告数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleAnnouncement(@Parameter(name = "announcementVo", description = "待修改的单条公告数据VO") @RequestBody AnnouncementVo announcementVo) {

        Announcement announcement = announcementService.getById(announcementVo.getId());
        BeanUtils.copyProperties(announcementVo, announcement);

        boolean isUpdated = announcementService.updateById(announcement);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_ANNOUNCEMENT_URL, announcement);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "删除单条公告数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleAnnouncement(@Parameter(name = "id", description = "待删除的单条公告ID", in = ParameterIn.PATH) @PathVariable("id") Long id) {

        boolean isDeleted = announcementService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_ANNOUNCEMENT_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


}
