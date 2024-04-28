package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.vo.DialogueVo;
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
    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Operation(description = "根据给定的关键词分页查询符合条件的对话数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<DialogueVo>> fuzzyQueryDialoguePage(

            @Parameter(name = "keywords", description = "查询对话数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页对话数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页对话数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        List<Dialogue> dialogues = esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Dialogue.class);

        List<DialogueVo> dialogueVos = new ArrayList<>();
        for (Dialogue dialogue : dialogues) {

            DialogueVo dialogueVo = new DialogueVo();
            BeanUtils.copyProperties(dialogue, dialogueVo);
            dialogueVos.add(dialogueVo);

        }

        return Result.success(dialogueVos);

    }


    @Operation(description = "修改单条对话数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSingleDialogue(

            @Parameter(name = "dialogue", description = "待修改的单条对话数据") @RequestBody Dialogue dialogue

    ) {

        UpdateResponse response = elasticsearchOperations.update(dialogue);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @Operation(description = "添加单条对话数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSingleDialogue(

            @Parameter(name = "dialogue", description = "待添加的单条对话数据") @RequestBody Dialogue dialogue

    ) {

        Dialogue savedDialogue = elasticsearchOperations.save(dialogue);
        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedDialogue);
        return Result.neutral(map);

    }


    @Operation(description = "删除单条对话数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleDialogue(

            @Parameter(name = "id", description = "待删除的单条对话ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        Criteria criteria = new Criteria("id").is(id);
        ByQueryResponse response = elasticsearchOperations.delete(new CriteriaQuery(criteria), Dialogue.class);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}
