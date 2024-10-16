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
import pfp.fltv.common.model.po.content.Comment;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.mq.ContentRankMqService;
import ptp.fltv.web.service.CommentService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 6:36:21
 * @description 内容评论控制器
 * @filename CommentController.java
 */

@AllArgsConstructor
@Tag(name = "内容评论操作接口")
@RestController
@RequestMapping("/content/comment")
public class CommentController {


    private static final String ES_PREFIX_COMMENT_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_PASSAGE_COMMENT_URL;
    private static final String ES_INSERT_COMMENT_URL = ES_PREFIX_COMMENT_URL + "/insert/single";
    private static final String ES_UPDATE_COMMENT_URL = ES_PREFIX_COMMENT_URL + "/update/single";
    private static final String ES_DELETE_COMMENT_URL = ES_PREFIX_COMMENT_URL + "/delete/single/{id}";


    private CommentService commentService;
    private RestTemplate restTemplate;
    private ContentRankMqService contentRankMqService;


    @LogRecord(description = "根据ID查询单条内容评论数据")
    @SentinelResource("web-content-comment-controller")
    @Operation(description = "根据ID查询单条内容评论数据")
    @GetMapping("/query/single/{id}")
    public Result<Comment> querySingleComment(

            @Parameter(name = "id", description = "待查询的单条内容评论ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Comment comment = commentService.getById(id);

        return (comment == null) ? Result.failure(null) : Result.success(comment);

    }


    @LogRecord(description = "批量(分页)查询多条内容评论数据")
    @SentinelResource("web-content-comment-controller")
    @Operation(description = "批量(分页)查询多条内容评论数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Comment>> queryCommentPage(

            @Parameter(name = "offset", description = "查询的一页内容评论数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页内容评论数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Comment> commentPage = new Page<>(offset, limit);
        commentPage = commentService.page(commentPage);

        return commentPage.getRecords() == null ? Result.failure(new ArrayList<>()) : Result.success(commentPage.getRecords());

    }


    @LogRecord(description = "根据指定排序类型批量(分页)查询多条内容评论数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "根据指定排序类型批量(分页)查询多条内容评论数据")
    @GetMapping("/query/page")
    public Result<List<Comment>> queryCommentPageWithSorting(

            @Parameter(name = "sortType", description = "排序规则", required = true) @RequestParam("sortType") ContentQuerySortType sortType,
            @Parameter(name = "belongType", description = "评论归属的实体类型", required = true) @RequestParam("belongType") Comment.BelongType belongType,
            @Parameter(name = "contentId", description = "评论归属的实体ID(值为-1则该参数失效)", required = true) @RequestParam("contentId") Long contentId,
            @Parameter(name = "uid", description = "内容发布者的ID(非必需)(仅在排序类型为拥有者类型下生效)") @RequestParam(name = "uid", required = false) Long uid,
            @Parameter(name = "pageNum", description = "查询的一页内容评论数据的数据页页码", required = true) @RequestParam("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页内容评论数据的数量", required = true) @RequestParam("pageSize") Long pageSize

    ) {

        return Result.success(commentService.queryCommentPageWithSorting(sortType, belongType, contentId, uid, pageNum, pageSize));

    }


    @GlobalTransactional(name = "insert-single-passage-comment", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条内容评论数据")
    @SentinelResource("web-content-comment-controller")
    @Operation(description = "添加单条内容评论数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleComment(

            @Parameter(name = "comment", description = "待添加的单条内容评论数据", required = true) @RequestBody Comment comment

    ) {

        boolean isSaved = commentService.save(comment);

        // 2024-10-15  13:58-非Passage实体将不再同步数据到ES中
        /*if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_COMMENT_URL, comment, Result.class);
            map.put("es_result", result);

        }*/

        return isSaved ? Result.success(comment.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-passage-comment", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条内容评论数据")
    @SentinelResource("web-content-comment-controller")
    @Operation(description = "修改单条内容评论数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleComment(

            @Parameter(name = "comment", description = "待修改的单条内容评论数据", required = true) @RequestBody Comment comment

    ) {

        boolean isUpdated = commentService.updateById(comment);

        // 2024-10-15  13:59-非Passage实体将不再同步数据到ES中
        /*if (isUpdated) {

            restTemplate.put(ES_UPDATE_COMMENT_URL, comment);
            map.put("es_result", Result.BLANK);

            // 2024-6-19  23:33-由于内容评论比较特殊，它并没有继承于content包下的BaseEntity，因此需要先转换成一个BaseEntity，以骗过方法入参检查，这个操作并不会产生副作用
            BaseEntity parodyComment = new BaseEntity();
            BeanUtils.copyProperties(comment, parodyComment);
            contentRankMqService.sendIndexChangeMsg(parodyComment, BaseEntity.ContentType.PASSAGE_COMMENT);

        }*/

        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-passage-comment", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条内容评论数据")
    @SentinelResource("web-content-comment-controller")
    @Operation(description = "删除单条内容评论数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleComment(

            @Parameter(name = "id", description = "待删除的单条内容评论ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = commentService.deleteSingleComment(id);

        // 2024-10-15  14:01-非Passage实体将不再同步数据到ES中
        /*if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_COMMENT_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }*/

        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}