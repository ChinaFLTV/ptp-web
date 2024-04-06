package ptp.fltv.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.Announcement;
import pfp.fltv.common.model.vo.AnnouncementVo;
import pfp.fltv.common.response.Result;
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

@Tag(name = "公告操作接口")
@RestController
@PreAuthorize("@pc.hasAnyPermission('content:announcement:add','content:announcement:remove','content:announcement:list','content:announcement:update')")
@RequestMapping("/content/announcement")
public class AnnouncementController {


    @Resource
    private AnnouncementService announcementService;


    @Operation(description = "根据ID查询单条公告数据")
    @GetMapping("/query/single/{id}")
    public Result<Announcement> querySingleAnnouncement(
            @Parameter(name = "id", description = "待查询的单条公告ID", in = ParameterIn.PATH)
            @PathVariable("id")
            Long id) {

        Announcement announcement = announcementService.getById(id);

        return (announcement == null) ? Result.failure(null) : Result.success(announcement);

    }


    @Operation(description = "批量(分页)查询多条公告数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<AnnouncementVo>> queryAnnouncementPage(
            @Parameter(name = "offset", description = "查询的一页公告数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页公告数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

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


    @Operation(description = "添加单条公告数据")
    @PostMapping("/insert/single")
    public Result<?> insertSingleAnnouncement(
            @Parameter(name = "announcementVo", description = "待添加的单条公告数据VO")
            @RequestBody
            AnnouncementVo announcementVo) {

        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementVo, announcement);

        boolean isSaved = announcementService.save(announcement);

        Map<String, Object> map = new HashMap<>();
        map.put("isSaved", isSaved);
        return isSaved ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "修改单条公告数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleAnnouncement(
            @Parameter(name = "announcementVo", description = "待修改的单条公告数据VO")
            @RequestBody
            AnnouncementVo announcementVo) {

        Announcement announcement = announcementService.getById(announcementVo.getId());
        BeanUtils.copyProperties(announcementVo, announcement);

        boolean isUpdated = announcementService.updateById(announcement);

        Map<String, Object> map = new HashMap<>();
        map.put("isUpdated", isUpdated);
        return isUpdated ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "删除单条公告数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleAnnouncement(
            @Parameter(name = "id", description = "待删除的单条公告ID", in = ParameterIn.PATH)
            @PathVariable("id")
            Long id) {

        boolean isDeleted = announcementService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("isDeleted", isDeleted);
        return isDeleted ? Result.success(map) : Result.failure(map);

    }


}
