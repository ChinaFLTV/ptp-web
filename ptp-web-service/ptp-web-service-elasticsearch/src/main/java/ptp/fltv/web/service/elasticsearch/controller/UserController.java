package ptp.fltv.web.service.elasticsearch.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserVo;
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
 * @date 2024/4/27 PM 8:05:31
 * @description 用户控制器
 * @filename UserController.java
 */

@AllArgsConstructor
@Tag(name = "用户操作接口(ES)")
@RestController
@RequestMapping("/content/user")
public class UserController {


    private EsSearchService esSearchService;


    @LogRecord(description = "根据给定的关键词分页查询符合条件的用户数据")
    @SentinelResource("service-elasticsearch-content-user-controller")
    @Operation(description = "根据给定的关键词分页查询符合条件的用户数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<UserVo>> fuzzyQueryUserPage(

            @Parameter(name = "keywords", description = "查询用户数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页用户数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页用户数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        List<User> users = esSearchService.pagingQueryByKeywords(keywords, "nickname", offset, limit, User.class);

        List<UserVo> userVos = new ArrayList<>();
        for (User user : users) {

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVos.add(userVo);

        }

        return Result.success(userVos);

    }


    @LogRecord(description = "修改单条用户数据(ES)")
    @SentinelResource("service-elasticsearch-content-user-controller")
    @Operation(description = "修改单条用户数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSingleUser(

            @Parameter(name = "user", description = "待修改的单条用户数据") @RequestBody User user

    ) {

        UpdateResponse response = esSearchService.updateEntity(user, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @LogRecord(description = "添加单条用户数据(ES)")
    @SentinelResource("service-elasticsearch-content-user-controller")
    @Operation(description = "添加单条用户数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSingleUser(

            @Parameter(name = "user", description = "待添加的单条用户数据") @RequestBody User user

    ) {

        User savedUser = esSearchService.insertEntity(user, null);
        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedUser);
        return Result.neutral(map);

    }


    @LogRecord(description = "删除单条用户数据(ES)")
    @SentinelResource("service-elasticsearch-content-user-controller")
    @Operation(description = "删除单条用户数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleUser(

            @Parameter(name = "id", description = "待删除的单条用户ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        ByQueryResponse response = esSearchService.deleteEntityById(id, User.class, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}