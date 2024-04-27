package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.vo.AnnouncementVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 8:01:59
 * @description 公告控制器
 * @filename AnnouncementController.java
 */

@Tag(name = "公告操作接口(ES)")
@RestController
@RequestMapping("/content/announcement")
public class AnnouncementController {


    @Resource
    private EsSearchService esSearchService;


    @Operation(description = "根据给定的关键词分页查询符合条件的公告数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<AnnouncementVo>> fuzzyQueryAnnouncementPage(
            @Parameter(name = "keywords", description = "查询公告数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页公告数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页公告数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        List<Announcement> announcements = esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Announcement.class);

        List<AnnouncementVo> announcementVos = new ArrayList<>();
        for (Announcement announcement : announcements) {

            AnnouncementVo announcementVo = new AnnouncementVo();
            BeanUtils.copyProperties(announcement, announcementVo);
            announcementVos.add(announcementVo);

        }

        return Result.success(announcementVos);

    }


}