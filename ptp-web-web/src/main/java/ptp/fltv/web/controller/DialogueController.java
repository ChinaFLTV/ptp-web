package ptp.fltv.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.vo.DialogueVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.service.DialogueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:13:27
 * @description 对话控制器
 * @filename DialogueController.java
 */

@Tag(name = "对话操作接口")
@RestController
@RequestMapping("/content/dialogue")
public class DialogueController {


    private static final String ES_PREFIX_DIALOGUE_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_DIALOGUE_URL;
    private static final String ES_INSERT_DIALOGUE_URL = ES_PREFIX_DIALOGUE_URL + "/insert/single";
    private static final String ES_UPDATE_DIALOGUE_URL = ES_PREFIX_DIALOGUE_URL + "/update/single";
    private static final String ES_DELETE_DIALOGUE_URL = ES_PREFIX_DIALOGUE_URL + "/delete/single/{id}";


    @Resource
    private DialogueService dialogueService;
    @Autowired
    private RestTemplate restTemplate;


    @Operation(description = "根据ID查询单条对话数据")
    @GetMapping("/query/single/{id}")
    public Result<Dialogue> querySingleDialogue(@Parameter(name = "id", description = "待查询的单条对话ID", in = ParameterIn.PATH)
                                                @PathVariable("id")
                                                Long id) {

        Dialogue dialogue = dialogueService.getById(id);

        return (dialogue == null) ? Result.failure(null) : Result.success(dialogue);

    }


    @Operation(description = "批量(分页)查询多条对话数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<DialogueVo>> queryDialoguePage(@Parameter(name = "offset", description = "查询的一页对话数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
                                                      @Parameter(name = "limit", description = "查询的这一页对话数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        Page<Dialogue> dialoguePage = new Page<>(offset, limit);
        dialoguePage = dialogueService.page(dialoguePage);

        List<DialogueVo> dialogueVos = new ArrayList<>();
        for (Dialogue dialogue : dialoguePage.getRecords()) {

            DialogueVo dialogueVo = new DialogueVo();
            BeanUtils.copyProperties(dialogue, dialogueVo);
            dialogueVos.add(dialogueVo);

        }

        return Result.success(dialogueVos);

    }


    @Operation(description = "添加单条对话数据")
    @PostMapping("/insert/single")
    public Result<?> insertSingleDialogue(@Parameter(name = "dialogueVo", description = "待添加的单条对话数据VO")
                                          @RequestBody
                                          DialogueVo dialogueVo) {

        Dialogue dialogue = new Dialogue();
        BeanUtils.copyProperties(dialogueVo, dialogue);

        boolean isSaved = dialogueService.save(dialogue);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_DIALOGUE_URL, dialogue, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @Operation(description = "修改单条对话数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleDialogue(@Parameter(name = "dialogueVo", description = "待修改的单条对话数据VO")
                                          @RequestBody
                                          DialogueVo dialogueVo) {

        Dialogue dialogue = dialogueService.getById(dialogueVo.getId());
        BeanUtils.copyProperties(dialogueVo, dialogue);

        boolean isUpdated = dialogueService.updateById(dialogue);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_DIALOGUE_URL, dialogue);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @Operation(description = "删除单条对话数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleDialogue(@Parameter(name = "id", description = "待删除的单条对话ID", in = ParameterIn.PATH)
                                          @PathVariable("id")
                                          Long id) {

        boolean isDeleted = dialogueService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_DIALOGUE_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


}
