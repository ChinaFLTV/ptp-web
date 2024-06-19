package ptp.fltv.web.service.elasticsearch.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.vo.AnnouncementVo;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 8:01:59
 * @description 公告控制器
 * @filename AnnouncementController.java
 */

@AllArgsConstructor
@Tag(name = "公告操作接口(ES)")
@RestController
@RequestMapping("/content/announcement")
public class AnnouncementController {


    private EsSearchService esSearchService;


    @LogRecord(description = "根据给定的关键词分页查询符合条件的公告数据")
    @SentinelResource("service-elasticsearch-content-announcement-controller")
    @Operation(description = "根据给定的关键词分页查询符合条件的公告数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<AnnouncementVo>> fuzzyQueryAnnouncementPage(

            @Parameter(name = "keywords", description = "查询公告数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页公告数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页公告数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        List<Announcement> announcements = esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Announcement.class);

        List<AnnouncementVo> announcementVos = new ArrayList<>();
        for (Announcement announcement : announcements) {

            AnnouncementVo announcementVo = new AnnouncementVo();
            BeanUtils.copyProperties(announcement, announcementVo);
            announcementVos.add(announcementVo);

        }

        return Result.success(announcementVos);

    }


    @LogRecord(description = "修改单条公告数据(ES)")
    @SentinelResource("service-elasticsearch-content-announcement-controller")
    @Operation(description = "修改单条公告数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSingleAnnouncement(

            @Parameter(name = "announcement", description = "待修改的单条公告数据") @RequestBody Announcement announcement

    ) {

        UpdateResponse response = esSearchService.updateEntity(announcement, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @LogRecord(description = "添加单条公告数据(ES)")
    @SentinelResource("service-elasticsearch-content-announcement-controller")
    @Operation(description = "添加单条公告数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSingleAnnouncement(

            @Parameter(name = "announcement", description = "待添加的单条公告数据") @RequestBody Announcement announcement

    ) {

        Announcement savedAnnouncement = esSearchService.insertEntity(announcement, null);

        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedAnnouncement);
        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条公告数据(ES)")
    @SentinelResource("service-elasticsearch-content-announcement-controller")
    @Operation(description = "删除单条公告数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleAnnouncement(

            @Parameter(name = "id", description = "待删除的单条公告ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        ByQueryResponse response = esSearchService.deleteEntityById(id, Announcement.class, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}