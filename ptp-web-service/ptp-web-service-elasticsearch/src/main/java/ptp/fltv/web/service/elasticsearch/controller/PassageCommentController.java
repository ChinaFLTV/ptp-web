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
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.vo.CommentVo;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 8:02:55
 * @description 文章评论控制器
 * @filename PassageCommentController.java
 */

@AllArgsConstructor
@Tag(name = "文章评论操作接口(ES)")
@RestController
@RequestMapping("/content/passage/comment")
public class PassageCommentController {


    private EsSearchService esSearchService;


    @LogRecord(description = "根据给定的关键词分页查询符合条件的文章评论数据")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "根据给定的关键词分页查询符合条件的文章评论数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<CommentVo>> fuzzyQueryPassageCommentPage(

            @Parameter(name = "keywords", description = "查询文章评论数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页文章评论数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章评论数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        List<Comment> comments = esSearchService.pagingQueryByKeywords(keywords, "content", offset, limit, Comment.class);

        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {

            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            commentVos.add(commentVo);

        }

        return Result.success(commentVos);

    }


    @LogRecord(description = "修改单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "修改单条文章评论数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSinglePassageComment(

            @Parameter(name = "passageComment", description = "待修改的单条文章评论数据") @RequestBody Comment comment

    ) {

        UpdateResponse response = esSearchService.updateEntity(comment, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @LogRecord(description = "添加单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "添加单条文章评论数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSinglePassageComment(

            @Parameter(name = "passageComment", description = "待添加的单条文章评论数据") @RequestBody Comment comment

    ) {

        Comment savedComment = esSearchService.insertEntity(comment, null);
        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedComment);
        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "删除单条文章评论数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePassageComment(

            @Parameter(name = "id", description = "待删除的单条文章评论ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        ByQueryResponse response = esSearchService.deleteEntityById(id, Comment.class, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}