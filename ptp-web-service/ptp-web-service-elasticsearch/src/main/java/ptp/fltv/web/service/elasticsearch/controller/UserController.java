package ptp.fltv.web.service.elasticsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/4/27 PM 8:05:31
 * @description 用户控制器
 * @filename UserController.java
 */

@Tag(name = "用户操作接口(ES)")
@RestController
@RequestMapping("/content/user")
public class UserController {


    @Resource
    private EsSearchService esSearchService;


    @Operation(description = "根据给定的关键词分页查询符合条件的用户数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<UserVo>> fuzzyQueryUserPage(
            @Parameter(name = "keywords", description = "查询用户数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页用户数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页用户数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        List<User> users = esSearchService.pagingQueryByKeywords(keywords, "nickname", offset, limit, User.class);

        List<UserVo> userVos = new ArrayList<>();
        for (User user : users) {

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVos.add(userVo);

        }

        return Result.success(userVos);

    }


}