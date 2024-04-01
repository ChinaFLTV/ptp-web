package ptp.fltv.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import ptp.fltv.web.service.PassageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 5:04:17
 * @description 文章控制器
 * @filename PassageController.java
 */

@Tag(name = "文章操作接口")
@RestController
@RequestMapping("/content/passage")
public class PassageController {


    @Resource
    private PassageService passageService;


    @Operation(description = "根据ID查询单条文章数据")
    @GetMapping("/query/single/{id}")
    public Result<Passage> querySinglePassage(@Parameter(name = "id", description = "待查询的单条文章ID", in = ParameterIn.PATH)
                                              @PathVariable("id")
                                              Long id) {

        Passage passage = passageService.getById(id);

        return (passage == null) ? Result.failure(null) : Result.success(passage);

    }


    @Operation(description = "批量(分页)查询多条文章数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<PassageVo>> queryPassagePage(@Parameter(name = "offset", description = "查询的一页文章数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
                                                    @Parameter(name = "limit", description = "查询的这一页文章数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        Page<Passage> passagePage = new Page<>(offset, limit);
        passagePage = passageService.page(passagePage);

        List<PassageVo> passageVos = new ArrayList<>();
        for (Passage passage : passagePage.getRecords()) {

            PassageVo passageVo = new PassageVo();
            BeanUtils.copyProperties(passage, passageVo);
            passageVos.add(passageVo);

        }

        return Result.success(passageVos);

    }


    @Operation(description = "添加单条文章数据")
    @PostMapping("/insert/single")
    public Result<?> insertSinglePassage(@Parameter(name = "dialogueVo", description = "待添加的单条文章数据VO")
                                         @RequestBody
                                         PassageVo passageVo) {

        Passage passage = new Passage();
        BeanUtils.copyProperties(passageVo, passage);

        boolean isSaved = passageService.save(passage);

        Map<String, Object> map = new HashMap<>();
        map.put("isSaved", isSaved);
        return isSaved ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "修改单条文章数据")
    @PutMapping("/update/single")
    public Result<?> updateSinglePassage(
            @Parameter(name = "dialogueVo", description = "待修改的单条文章数据VO")
            @RequestBody
            PassageVo passageVo) {

        Passage passage = passageService.getById(passageVo.getId());
        BeanUtils.copyProperties(passageVo, passage);

        boolean isUpdated = passageService.updateById(passage);

        Map<String, Object> map = new HashMap<>();
        map.put("isUpdated", isUpdated);
        return isUpdated ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "删除单条文章数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePassage(@Parameter(name = "id", description = "待删除的单条文章ID", in = ParameterIn.PATH)
                                         @PathVariable("id")
                                         Long id) {

        boolean isDeleted = passageService.removeById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("isDeleted", isDeleted);
        return isDeleted ? Result.success(map) : Result.failure(map);

    }


}