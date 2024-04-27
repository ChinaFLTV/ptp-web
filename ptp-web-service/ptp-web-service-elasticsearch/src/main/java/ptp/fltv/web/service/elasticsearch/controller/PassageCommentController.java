package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.PassageComment;
import pfp.fltv.common.model.vo.PassageCommentVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;


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


    @Operation(description = "根据给定的关键词分页查询符合条件的文章评论数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<PassageCommentVo>> fuzzyQueryPassageCommentPage(
            @Parameter(name = "keywords", description = "查询文章评论数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页文章评论数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章评论数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        List<PassageComment> passageComments = esSearchService.pagingQueryByKeywords(keywords, "content", offset, limit, PassageComment.class);

        List<PassageCommentVo> passageCommentVos = new ArrayList<>();
        for (PassageComment passageComment : passageComments) {

            PassageCommentVo passageCommentVo = new PassageCommentVo();
            BeanUtils.copyProperties(passageComment, passageCommentVo);
            passageCommentVos.add(passageCommentVo);

        }

        return Result.success(passageCommentVos);

    }


}