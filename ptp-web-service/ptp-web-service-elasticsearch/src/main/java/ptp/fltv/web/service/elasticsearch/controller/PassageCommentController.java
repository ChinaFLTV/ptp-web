package ptp.fltv.web.service.elasticsearch.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.content.PassageComment;
import pfp.fltv.common.model.vo.PassageCommentVo;
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
 * @date 2024/4/27 PM 8:02:55
 * @description 文章评论控制器
 * @filename PassageCommentController.java
 */

@Tag(name = "文章评论操作接口(ES)")
@RestController
@RequestMapping("/content/passage/comment")
public class PassageCommentController {

    @Resource
    private EsSearchService esSearchService;


    @LogRecord(description = "根据给定的关键词分页查询符合条件的文章评论数据")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "根据给定的关键词分页查询符合条件的文章评论数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<PassageCommentVo>> fuzzyQueryPassageCommentPage(

            @Parameter(name = "keywords", description = "查询文章评论数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页文章评论数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章评论数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        List<PassageComment> passageComments = esSearchService.pagingQueryByKeywords(keywords, "content", offset, limit, PassageComment.class);

        List<PassageCommentVo> passageCommentVos = new ArrayList<>();
        for (PassageComment passageComment : passageComments) {

            PassageCommentVo passageCommentVo = new PassageCommentVo();
            BeanUtils.copyProperties(passageComment, passageCommentVo);
            passageCommentVos.add(passageCommentVo);

        }

        return Result.success(passageCommentVos);

    }


    @LogRecord(description = "修改单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "修改单条文章评论数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSinglePassageComment(

            @Parameter(name = "passageComment", description = "待修改的单条文章评论数据") @RequestBody PassageComment passageComment

    ) {

        UpdateResponse response = esSearchService.updateEntity(passageComment, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @LogRecord(description = "添加单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "添加单条文章评论数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSinglePassageComment(

            @Parameter(name = "passageComment", description = "待添加的单条文章评论数据") @RequestBody PassageComment passageComment

    ) {

        PassageComment savedPassageComment = esSearchService.insertEntity(passageComment, null);
        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedPassageComment);
        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条文章评论数据(ES)")
    @SentinelResource("service-elasticsearch-content-passage-comment-controller")
    @Operation(description = "删除单条文章评论数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePassageComment(

            @Parameter(name = "id", description = "待删除的单条文章评论ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        ByQueryResponse response = esSearchService.deleteEntityById(id, PassageComment.class, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}