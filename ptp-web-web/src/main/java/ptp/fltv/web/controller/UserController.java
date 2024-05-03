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
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserVo;
import pfp.fltv.common.response.Result;
import ptp.fltv.web.constants.WebConstants;
import ptp.fltv.web.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:13:46
 * @description 用户控制器
 * @filename UserController.java
 */

@Tag(name = "用户操作接口")
@RestController
@RequestMapping("/content/user")
public class UserController {


    private static final String ES_PREFIX_USER_URL = WebConstants.ES_BASE_URL + WebConstants.ES_CONTEXT_URL + WebConstants.ES_BASE_USER_URL;
    private static final String ES_INSERT_USER_URL = ES_PREFIX_USER_URL + "/insert/single";
    private static final String ES_UPDATE_USER_URL = ES_PREFIX_USER_URL + "/update/single";
    private static final String ES_DELETE_USER_URL = ES_PREFIX_USER_URL + "/delete/single/{id}";


    @Resource
    private UserService userService;
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;


    @Operation(description = "根据ID查询用户信息")
    @GetMapping("/query/{userId}")
    public Result<User> queryUserById(
            @Parameter(name = "userId", description = "待查询的用户ID", in = ParameterIn.PATH)
            @PathVariable("userId")
            Long userId) {

        User user = userService.getById(userId);

        return (user == null) ? Result.failure(null) : Result.success(user);

    }


    @Operation(description = "批量(分页)查询多条用户数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<UserVo>> queryPassagePage(@Parameter(name = "offset", description = "查询的一页用户数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
                                                 @Parameter(name = "limit", description = "查询的这一页用户数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit) {

        Page<User> userPage = new Page<>(offset, limit);
        userPage = userService.page(userPage);

        List<UserVo> userVos = new ArrayList<>();
        for (User user : userPage.getRecords()) {

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVos.add(userVo);

        }

        return Result.success(userVos);

    }


    @Operation(description = "查询所有用户信息")
    @GetMapping("/query/all")
    public Result<List<User>> queryAllUsers() {

        List<User> users = userService.list();
        return Result.success(users);

    }


    @Operation(description = "添加用户信息")
    @PostMapping("/insert")
    public Result<?> insertUser(
            @Parameter(name = "user", description = "待添加的用户信息")
            @RequestBody
            User user) {

        // 2024-5-3  20:58-TODO 必须进行用户密码加密
        // 2024-4-1  21:28-使用Spring Security配置中配置的密码加密验证器对用户原始密码进行加密
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean isSaved = userService.save(user);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isSaved", isSaved);
        map.put("mysql_result", mysqlResult);

        if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_USER_URL, user, Result.class);
            map.put("es_result", result);

        }

        return Result.neutral(map);

    }


    @Operation(description = "修改用户信息")
    @PutMapping("/update")
    public Result<?> updateUser(
            @Parameter(name = "user", description = "待修改的用户信息")
            @RequestBody
            User user) {

        boolean isUpdated = userService.updateById(user);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isUpdated", isUpdated);
        map.put("mysql_result", mysqlResult);

        if (isUpdated) {

            restTemplate.put(ES_UPDATE_USER_URL, user);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


    @Operation(description = "删除用户信息")
    @DeleteMapping("/delete/{userId}")
    public Result<?> deleteUser(
            @Parameter(name = "userId", description = "待删除的用户ID", in = ParameterIn.PATH)
            @PathVariable("userId")
            Long userId) {

        boolean isDeleted = userService.removeById(userId);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mysqlResult = new HashMap<>();
        mysqlResult.put("isDeleted", isDeleted);
        map.put("mysql_result", mysqlResult);

        if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", userId);
            restTemplate.delete(ES_DELETE_USER_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }

        return Result.neutral(map);

    }


}
