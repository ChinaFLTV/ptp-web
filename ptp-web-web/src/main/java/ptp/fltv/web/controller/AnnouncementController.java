package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.mq.ContentRankMqService;
import ptp.fltv.web.service.AnnouncementService;

import java.util.ArrayList;
import java.util.List;

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


    private AnnouncementService announcementService;
    private RestTemplate restTemplate;
    private ContentRankMqService contentRankMqService;


    @LogRecord(description = "根据ID查询单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "根据ID查询单条公告数据")
    @GetMapping("/query/single/{id}")
    public Result<Announcement> querySingleAnnouncement(

            @Parameter(name = "id", description = "待查询的单条公告ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Announcement announcement = announcementService.getById(id);

        return (announcement == null) ? Result.failure(null) : Result.success(announcement);

    }


    @LogRecord(description = "批量(分页)查询多条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "批量(分页)查询多条公告数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Announcement>> queryAnnouncementPage(

            @Parameter(name = "offset", description = "查询的一页公告数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页公告数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Announcement> announcementPage = new Page<>(offset, limit);
        announcementPage = announcementService.page(announcementPage);

        return Result.success(announcementPage.getRecords() == null ? new ArrayList<>() : announcementPage.getRecords());

    }


    @LogRecord(description = "查询全部可见的公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "查询全部可见的公告数据")
    @GetMapping("/query/all/available")
    public Result<List<Announcement>> queryAllAnnouncements() {

        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ContentStatus.NORMAL.getCode());
        List<Announcement> announcements = announcementService.list(queryWrapper);
        return Result.success(announcements);

    }


    @GlobalTransactional(name = "insert-single-announcement", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "添加单条公告数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleAnnouncement(

            @Parameter(name = "announcement", description = "待添加的单条公告数据VO", required = true) @RequestBody Announcement announcement

    ) {

        boolean isSaved = announcementService.save(announcement);

        // 2024-10-15  13:38-非Passage实体将不再同步数据到ES中
        /*if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_ANNOUNCEMENT_URL, announcement, Result.class);
            map.put("es_result", result);

        }*/

        return isSaved ? Result.success(announcement.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-announcement", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "修改单条公告数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleAnnouncement(

            @Parameter(name = "announcement", description = "待修改的单条公告数据VO", required = true) @RequestBody Announcement announcement

    ) {

        boolean isUpdated = announcementService.updateById(announcement);

        // 2024-10-15  13:39-非Passage实体将不再同步数据到ES中
        /*if (isUpdated) {

            restTemplate.put(ES_UPDATE_ANNOUNCEMENT_URL, announcement);
            map.put("es_result", Result.BLANK);

            // 2024-6-19  23:33-每次内容实体更新都需要重新计算一次得分，以避免上一次计算失误，尽快恢复内容实体的正常得分
            contentRankMqService.sendIndexChangeMsg(announcement, BaseEntity.ContentType.ANNOUNCEMENT);

        }*/

        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-announcement", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "删除单条公告数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleAnnouncement(

            @Parameter(name = "id", description = "待删除的单条公告ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = announcementService.removeById(id);

        // 2024-10-15  13:47-非Passage实体将不再同步数据到ES中
        /*if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_ANNOUNCEMENT_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }*/

        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "分页获取指定类型的排行榜的公告数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "分页获取指定类型的排行榜的公告数据")
    @DeleteMapping("/query/rank/page")
    public Result<List<Announcement>> queryAnnouncementRankPage(

            @Parameter(name = "offset", description = "查询的一页排行榜公告数据的起始偏移量", required = true) @RequestParam("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页排行榜公告数据的数量", required = true) @RequestParam("limit") Long limit,
            @Parameter(name = "rankType", description = "排行榜的类型", required = true) @RequestParam("rankType") ContentRankType rankType

    ) {

        return Result.success(announcementService.getRankListByPage(rankType, offset, limit));

    }


}
