package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.vo.DialogueVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 7:59:52
 * @description 对话控制器
 * @filename DialogueController.java
 */

@Tag(name = "对话操作接口(ES)")
@RestController
@RequestMapping("/content/dialogue")
public class DialogueController {


    @Resource
    private EsSearchService esSearchService;


    @Operation(description = "根据给定的关键词分页查询符合条件的对话数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<DialogueVo>> fuzzyQueryDialoguePage(
            @Parameter(name = "keywords", description = "查询对话数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页对话数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页对话数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        List<Dialogue> dialogues = esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Dialogue.class);

        List<DialogueVo> dialogueVos = new ArrayList<>();
        for (Dialogue dialogue : dialogues) {

            DialogueVo dialogueVo = new DialogueVo();
            BeanUtils.copyProperties(dialogue, dialogueVo);
            dialogueVos.add(dialogueVo);

        }

        return Result.success(dialogueVos);

    }


}
