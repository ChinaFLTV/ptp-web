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
import pfp.fltv.common.model.po.content.PassageComment;
import pfp.fltv.common.model.vo.PassageCommentVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.service.PassageCommentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:36:21
 * @description 文章评论控制器
 * @filename PassageCommentController.java
 */

@AllArgsConstructor
@Tag(name = "文章评论操作接口")
@RestController
@RequestMapping("/content/passage/comment")
public class PassageCommentController {


    private static final String ES_PREFIX_PASSAGE_COMMENT_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_PASSAGE_COMMENT_URL;
    private static final String ES_INSERT_PASSAGE_COMMENT_URL = ES_PREFIX_PASSAGE_COMMENT_URL + "/insert/single";
    private static final String ES_UPDATE_PASSAGE_COMMENT_URL = ES_PREFIX_PASSAGE_COMMENT_URL + "/update/single";
    private static final String ES_DELETE_PASSAGE_COMMENT_URL = ES_PREFIX_PASSAGE_COMMENT_URL + "/delete/single/{id}";


    private PassageCommentService passageCommentService;
    private RestTemplate restTemplate;


    @LogRecord(description = "根据ID查询单条文章评论数据")
    @SentinelResource("web-content-passage-comment-controller")
    @Operation(description = "根据ID查询单条文章评论数据")
    @GetMapping("/query/single/{id}")
    public Result<PassageComment> querySinglePassageComment(
            @Parameter(name = "id", description = "待查询的单条文章评论ID", in = ParameterIn.PATH)
            @PathVariable("id")
            Long id) {

        PassageComment passageComment = passageCommentService.getById(id);

        return (passageComment == null) ? Result.failure(null) : Result.success(passageComment);

    }


    @LogRecord(description = "批量(分页)查询多条文章评论数据")
    @SentinelResource("web-content-passage-comment-controller")
    @Operation(description = "批量(分页)查询多条文章评论数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<PassageCommentVo>> queryPassageCommentPage(
            @Parameter(name = "offset", description = "查询的一页文章评论数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章评论数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        Page<PassageComment> passageCommentPage = new Page<>(offset, limit);
        passageCommentPage = passageCommentService.page(passageCommentPage);

        List<PassageCommentVo> passageCommentVos = new ArrayList<>();
        for (PassageComment passageComment : passageCommentPage.getRecords()) {

            PassageCommentVo passageCommentVo = new PassageCommentVo();
            BeanUtils.copyProperties(passageComment, passageCommentVo);
            passageCommentVos.add(passageCommentVo);

        }

        return Result.success(passageCommentVos);

    }


    @LogRecord(description = "添加单条文章评论数据")
    @SentinelResource("web-content-passage-comment-controller")
    @Operation(description = "添加单条文章评论数据")
    @PostMapping("/insert/single")
    public Result<?> insertSinglePassageComment(
            @Parameter(name = "passageCommentVo", description = "待添加的单条文章评论数据VO")
            @RequestBody
            PassageCommentVo passageCommentVo) {

        PassageComment passageComment = new PassageComment();
        BeanUtils.copyProperties(passageCommentVo, passageComment);

        boolean isSaved = passageCommentService.save(passageComment);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_PASSAGE_COMMENT_URL, passageComment, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @LogRecord(description = "修改单条文章评论数据")
    @SentinelResource("web-content-passage-comment-controller")
    @Operation(description = "修改单条文章评论数据")
    @PutMapping("/update/single")
    public Result<?> updateSinglePassageComment(
            @Parameter(name = "passageCommentVo", description = "待修改的单条文章评论数据VO")
            @RequestBody
            PassageCommentVo passageCommentVo) {

        PassageComment passageComment = passageCommentService.getById(passageCommentVo.getId());
        BeanUtils.copyProperties(passageCommentVo, passageComment);

        boolean isUpdated = passageCommentService.updateById(passageComment);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_PASSAGE_COMMENT_URL, passageComment);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条文章评论数据")
    @SentinelResource("web-content-passage-comment-controller")
    @Operation(description = "删除单条文章评论数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePassageComment(
            @Parameter(name = "id", description = "待删除的单条文章评论ID", in = ParameterIn.PATH)
            @PathVariable("id")
            Long id) {

        boolean isDeleted = passageCommentService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_PASSAGE_COMMENT_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


}
