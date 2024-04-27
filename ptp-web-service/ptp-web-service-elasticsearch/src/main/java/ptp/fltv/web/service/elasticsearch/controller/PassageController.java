package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.Passage;
import pfp.fltv.common.model.vo.PassageVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 8:04:23
 * @description 文章控制器
 * @filename PassageController.java
 */

@Tag(name = "文章操作接口(ES)")
@RestController
@RequestMapping("/content/passage")
public class PassageController {


    @Resource
    private EsSearchService esSearchService;


    @Operation(description = "根据给定的关键词分页查询符合条件的文章数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<PassageVo>> fuzzyQueryPassagePage(
            @Parameter(name = "keywords", description = "查询文章数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页文章数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页文章数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        List<Passage> passages = esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Passage.class);

        List<PassageVo> passageVos = new ArrayList<>();
        for (Passage passage : passages) {

            PassageVo passageVo = new PassageVo();
            BeanUtils.copyProperties(passage, passageVo);
            passageVos.add(passageVo);

        }

        return Result.success(passageVos);

    }


}