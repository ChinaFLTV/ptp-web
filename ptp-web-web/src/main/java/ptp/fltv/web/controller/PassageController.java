package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
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
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.base.content.BaseEntity;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.mq.ContentRankMqService;
import ptp.fltv.web.service.PassageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 5:04:17
 * @description 文章控制器
 * @filename PassageController.java
 */

@AllArgsConstructor
@Tag(name = "文章操作接口")
@RestController
@RequestMapping("/content/passage")
public class PassageController {


    private static final String ES_PREFIX_PASSAGE_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_PASSAGE_URL;
    private static final String ES_INSERT_PASSAGE_URL = ES_PREFIX_PASSAGE_URL + "/insert/single";
    private static final String ES_UPDATE_PASSAGE_URL = ES_PREFIX_PASSAGE_URL + "/update/single";
    private static final String ES_DELETE_PASSAGE_URL = ES_PREFIX_PASSAGE_URL + "/delete/single/{id}";


    private PassageService passageService;
    private RestTemplate restTemplate;
    private ContentRankMqService contentRankMqService;


    @LogRecord(description = "根据ID查询单条文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "根据ID查询单条文章数据")
    @GetMapping("/query/single/{id}")
    public Result<Passage> querySinglePassage(

            @Parameter(name = "id", description = "待查询的单条文章ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Passage passage = passageService.getById(id);
        return (passage == null) ? Result.failure(null) : Result.success(passage);

    }


    @LogRecord(description = "批量(分页)查询多条文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "批量(分页)查询多条文章数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Passage>> queryPassagePage(

            @Parameter(name = "offset", description = "查询的一页文章数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Passage> passagePage = new Page<>(offset, limit);
        passagePage = passageService.page(passagePage);

        return Result.success(passagePage.getRecords() == null ? new ArrayList<>() : passagePage.getRecords());

    }


    @LogRecord(description = "根据指定排序类型批量(分页)查询多条文章数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "根据指定排序类型批量(分页)查询多条文章数据")
    @GetMapping("/query/page")
    public Result<List<Passage>> queryPassagePageWithSorting(

            @Parameter(name = "sortType", description = "排序规则", required = true) @RequestParam("sortType") ContentQuerySortType sortType,
            @Parameter(name = "pageNum", description = "查询的一页文章数据的数据页页码", required = true) @RequestParam("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页文章数据的数量", required = true) @RequestParam("pageSize") Long pageSize,
            @Parameter(name = "uid", description = "当前请求发起用户的ID(非必需)(仅在排序类型为订阅类型下生效)") @RequestParam(name = "uid", required = false) Long uid

    ) {

        return Result.success(passageService.queryPassagePageWithSorting(sortType, pageNum, pageSize, uid));

    }


    @GlobalTransactional(name = "insert-single-passage", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "添加单条文章数据")
    @PostMapping("/insert/single")
    public Result<?> insertSinglePassage(

            @Parameter(name = "passage", description = "待添加的单条文章数据VO", required = true) @RequestBody Passage passage

    ) {

        boolean isSaved = passageService.save(passage);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_PASSAGE_URL, passage, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @GlobalTransactional(name = "update-single-passage", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "修改单条文章数据")
    @PutMapping("/update/single")
    public Result<?> updateSinglePassage(

            @Parameter(name = "dialogue", description = "待修改的单条文章数据", required = true) @RequestBody Passage passage

    ) {

        boolean isUpdated = passageService.updateById(passage);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_PASSAGE_URL, passage);
            map.put("es_result", Result.BLANK);

            // 2024-6-19  22:36-每次内容实体更新都需要重新计算一次得分，以避免上一次计算失误，尽快恢复内容实体的正常得分
            contentRankMqService.sendIndexChangeMsg(passage, BaseEntity.ContentType.PASSAGE);

        }

        return Result.neutral(map);

    }


    @GlobalTransactional(name = "delete-single-passage", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "删除单条文章数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePassage(

            @Parameter(name = "id", description = "待删除的单条文章ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = passageService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_PASSAGE_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @LogRecord(description = "分页获取指定类型的排行榜的文章数据")
    @SentinelResource("web-content-passage-controller")
    @Operation(description = "分页获取指定类型的排行榜的文章数据")
    @DeleteMapping("/query/rank/page")
    public Result<List<Passage>> queryPassageRankPage(

            @Parameter(name = "offset", description = "查询的一页排行榜文章数据的起始偏移量", required = true) @RequestParam("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页排行榜文章数据的数量", required = true) @RequestParam("limit") Long limit,
            @Parameter(name = "rankType", description = "排行榜的类型", required = true) @RequestParam("rankType") ContentRankType rankType

    ) {

        return Result.success(passageService.getRankListByPage(rankType, offset, limit));

    }


}
