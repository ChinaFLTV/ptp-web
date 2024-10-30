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
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.TaskStatus;
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.po.system.EventRecord;
import ptp.fltv.web.service.EventRecordService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/10/29 AM 2:04:55
 * @description 事件记录控制器
 * @filename EventRecordController.java
 */

@AllArgsConstructor
@Tag(name = "事件记录操作接口")
@RestController
@RequestMapping("/manage/event/record")
public class EventRecordController {


    private EventRecordService eventRecordService;


    @LogRecord(description = "根据ID查询单条事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "根据ID查询单条事件记录数据")
    @GetMapping("/query/single/{id}")
    public Result<EventRecord> querySingleEventRecord(

            @Parameter(name = "id", description = "待查询的单条事件记录ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        EventRecord eventRecord = eventRecordService.getById(id);

        return (eventRecord == null) ? Result.failure(null) : Result.success(eventRecord);

    }


    @LogRecord(description = "查询单条内容事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "查询单条内容事件记录数据")
    @GetMapping("/querySingleContentEventRecord")
    public Result<EventRecord> querySingleContentEventRecord(

            @Parameter(name = "eventType", description = "内容事件的类型", required = true) @RequestParam("eventType") EventRecord.EventType eventType,
            @Parameter(name = "contentType", description = "内容事件的目标内容实体的类型", required = true) @RequestParam("contentType") Comment.BelongType contentType,
            @Parameter(name = "contentId", description = "内容事件的目标内容实体的ID", required = true) @RequestParam("contentId") Long contentId,
            @Parameter(name = "uid", description = "内容事件的发出者ID", required = true) @RequestParam("uid") Long uid

    ) {

        EventRecord eventRecord = eventRecordService.querySingleContentEventRecord(eventType, contentType, contentId, uid);
        return (eventRecord == null) ? Result.failure(null) : Result.success(eventRecord);

    }


    @LogRecord(description = "批量(分页)查询多条事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "批量(分页)查询多条事件记录数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<EventRecord>> queryEventRecordPage(

            @Parameter(name = "offset", description = "查询的一页事件记录数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页事件记录数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<EventRecord> eventRecordPage = new Page<>(offset, limit);
        eventRecordPage = eventRecordService.page(eventRecordPage);

        return Result.success(eventRecordPage.getRecords() == null ? new ArrayList<>() : eventRecordPage.getRecords());

    }


    @LogRecord(description = "查询全部可见的事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "查询全部可见的事件记录数据")
    @GetMapping("/query/all/available")
    public Result<List<EventRecord>> queryAllEventRecords() {

        QueryWrapper<EventRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", TaskStatus.NORMAL.getCode());
        List<EventRecord> eventRecords = eventRecordService.list(queryWrapper);
        return Result.success(eventRecords);

    }


    @GlobalTransactional(name = "insert-single-event-record", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "添加单条事件记录数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleEventRecord(

            @Parameter(name = "eventRecord", description = "待添加的单条事件记录数据VO", required = true) @RequestBody EventRecord eventRecord

    ) {

        boolean isSaved = eventRecordService.save(eventRecord);
        return isSaved ? Result.success(eventRecord.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "insert-single-event-record", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条内容实体相关的事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "添加单条内容实体相关的事件记录数据")
    @PostMapping("/insertSingleContentEventRecord")
    public Result<Long> insertSingleContentEventRecord(

            @Parameter(name = "eventRecord", description = "待添加的单条内容事件记录数据VO", required = true) @RequestBody EventRecord eventRecord

    ) {

        boolean isSaved = eventRecordService.insertSingleContentEventRecord(eventRecord);
        return isSaved ? Result.success(eventRecord.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-event-record", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "修改单条事件记录数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleEventRecord(

            @Parameter(name = "eventRecord", description = "待修改的单条事件记录数据VO", required = true) @RequestBody EventRecord eventRecord

    ) {

        boolean isUpdated = eventRecordService.updateById(eventRecord);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-event-record", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "删除单条事件记录数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleEventRecord(

            @Parameter(name = "id", description = "待删除的单条事件记录ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = eventRecordService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-event-record", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条内容实体相关的事件记录数据")
    @SentinelResource("web-content-event-record-controller")
    @Operation(description = "删除单条内容实体相关的事件记录数据")
    @DeleteMapping("/deleteSingleContentEventRecord")
    public Result<?> deleteSingleContentEventRecord(

            @Parameter(name = "eventType", description = "内容事件的类型", required = true) @RequestParam("eventType") EventRecord.EventType eventType,
            @Parameter(name = "contentType", description = "内容事件的目标内容实体的类型", required = true) @RequestParam("contentType") Comment.BelongType contentType,
            @Parameter(name = "contentId", description = "内容事件的目标内容实体的ID", required = true) @RequestParam("contentId") Long contentId,
            @Parameter(name = "uid", description = "内容事件的发出者ID", required = true) @RequestParam("uid") Long uid

    ) {

        boolean isDeleted = eventRecordService.deleteSingleContentEventRecord(eventType, contentType, contentId, uid);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}