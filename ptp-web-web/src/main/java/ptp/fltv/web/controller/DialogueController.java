package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentQuerySortType;
import pfp.fltv.common.enums.ContentRankType;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.model.vo.DialogueVo;
import ptp.fltv.web.mq.ContentRankMqService;
import ptp.fltv.web.service.DialogueService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/22 下午 6:13:27
 * @description 对话控制器
 * @filename DialogueController.java
 */

@AllArgsConstructor
@Tag(name = "对话操作接口")
@RestController
@RequestMapping("/content/dialogue")
public class DialogueController {


    private DialogueService dialogueService;
    private RestTemplate restTemplate;
    private ContentRankMqService contentRankMqService;


    @LogRecord(description = "根据ID查询单条对话数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "根据ID查询单条对话数据")
    @GetMapping("/query/single/{id}")
    public Result<DialogueVo> querySingleDialogue(

            @Parameter(name = "id", description = "待查询的单条对话ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        DialogueVo dialogueVo = dialogueService.querySingleDialogue(id);

        return (dialogueVo == null) ? Result.failure(null) : Result.success(dialogueVo);

    }


    @LogRecord(description = "批量(分页)查询多条对话VO数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "批量(分页)查询多条对话VO数据")
    @GetMapping("/query/page/vo")
    public Result<List<DialogueVo>> queryDialogueVoPage(

            @Parameter(name = "pageNum", description = "查询的一页对话数据的数据页页码", required = true) @RequestParam("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页对话数据的数量", required = true) @RequestParam("pageSize") Long pageSize

    ) {

        Page<Dialogue> dialoguePage = new Page<>(pageNum, pageSize);
        dialoguePage = dialogueService.page(dialoguePage);

        List<DialogueVo> dialogueVos = new ArrayList<>();
        for (Dialogue dialogue : dialoguePage.getRecords()) {

            DialogueVo dialogueVo = new DialogueVo();
            BeanUtils.copyProperties(dialogue, dialogueVo);
            dialogueVos.add(dialogueVo);

        }

        return Result.success(dialogueVos);

    }


    @LogRecord(description = "根据指定排序类型批量(分页)查询多条对话VO数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "根据指定排序类型批量(分页)查询多条对话VO数据")
    @GetMapping("/queryDialogueVoPageWithSorting")
    public Result<List<DialogueVo>> queryDialogueVoPageWithSorting(

            @Parameter(name = "sortType", description = "排序规则", required = true) @RequestParam("sortType") ContentQuerySortType sortType,
            @Parameter(name = "pageNum", description = "查询的一页对话数据的数据页页码", required = true) @RequestParam("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页对话数据的数量", required = true) @RequestParam("pageSize") Long pageSize

    ) {

        return Result.success(dialogueService.queryDialogueVoPageWithSorting(sortType, pageNum, pageSize));

    }


    @GlobalTransactional(name = "insert-single-dialogue", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条对话数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "添加单条对话数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleDialogue(

            @Parameter(name = "dialogue", description = "待添加的单条对话数据", required = true) @RequestBody Dialogue dialogue

    ) {

        boolean isSaved = dialogueService.insertSingleDialogue(dialogue);

        // 2024-10-15  13:23-非Passage实体将不再同步数据到ES中
        /*if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_DIALOGUE_URL, dialogue, Result.class);
            map.put("es_result", result);

        }*/

        return isSaved ? Result.success(dialogue.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-dialogue", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条对话数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "修改单条对话数据")
    @PutMapping("/update/single")
    public Result<?> updateSingleDialogue(

            @Parameter(name = "dialogue", description = "待修改的单条对话数据", required = true) @RequestBody Dialogue dialogue

    ) {

        boolean isUpdated = dialogueService.updateById(dialogue);

        // 2024-10-15  13:35-非Passage实体将不再同步数据到ES中
       /* if (isUpdated) {

            restTemplate.put(ES_UPDATE_DIALOGUE_URL, dialogue);
            map.put("es_result", Result.BLANK);

            // 2024-6-19  23:34-每次内容实体更新都需要重新计算一次得分，以避免上一次计算失误，尽快恢复内容实体的正常得分
            contentRankMqService.sendIndexChangeMsg(dialogue, BaseEntity.ContentType.DIALOGUE);

        }*/

        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-dialogue", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条对话数据")
    @SentinelResource("web-content-dialogue-controller")
    @Operation(description = "删除单条对话数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleDialogue(

            @Parameter(name = "id", description = "待删除的单条对话ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        boolean isDeleted = dialogueService.removeById(id);

        // 2024-10-15  13:36-非Passage实体将不再同步数据到ES中
        /*if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", id);
            restTemplate.delete(ES_DELETE_DIALOGUE_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }*/

        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "分页获取指定类型的排行榜的对话数据")
    @SentinelResource("web-content-announcement-controller")
    @Operation(description = "分页获取指定类型的排行榜的对话数据")
    @DeleteMapping("/query/rank/page")
    public Result<List<Dialogue>> queryAnnouncementRankPage(

            @Parameter(name = "offset", description = "查询的一页排行榜对话数据的起始偏移量", required = true) @RequestParam("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页排行榜对话数据的数量", required = true) @RequestParam("limit") Long limit,
            @Parameter(name = "rankType", description = "排行榜的类型", required = true) @RequestParam("rankType") ContentRankType rankType

    ) {

        return Result.success(dialogueService.getRankListByPage(rankType, offset, limit));

    }


}
