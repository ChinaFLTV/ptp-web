package ptp.fltv.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.model.po.content.Dialogue;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.DialogueService;

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


    @Resource
    private DialogueService dialogueService;


    @Operation(description = "根据ID查询单条对话数据")
    @GetMapping("/query/single/{id}")
    public Result<Dialogue> querySingleDialogue(@PathVariable("id") Long id) {

        Dialogue dialogue = dialogueService.getById(id);

        return (dialogue == null) ? Result.failure(null) : Result.success(dialogue);

    }



}
