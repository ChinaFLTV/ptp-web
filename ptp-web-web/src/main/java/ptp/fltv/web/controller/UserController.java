package ptp.fltv.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.vo.UserVo;
import pfp.fltv.common.response.Result;
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
@PreAuthorize("@pc.hasAnyPermission('user:add','user:remove','user:list','user:update')")
@RequestMapping("/content/user")
public class UserController {


    @Resource
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private ElasticsearchOperations elasticsearchOperations;


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

        // 2024-4-1  21:28-使用Spring Security配置中配置的密码加密验证器对用户原始密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean isSaved = userService.save(user);
        if (isSaved) {

            elasticsearchOperations.save(user);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("isSaved", isSaved);
        return isSaved ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "修改用户信息")
    @PutMapping("/update")
    public Result<?> updateUser(
            @Parameter(name = "user", description = "待修改的用户信息")
            @RequestBody
            User user) {

        boolean isUpdated = userService.updateById(user);
        if (isUpdated) {

            elasticsearchOperations.update(user);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("isUpdated", isUpdated);
        return isUpdated ? Result.success(map) : Result.failure(map);

    }


    @Operation(description = "删除用户信息")
    @DeleteMapping("/delete/{userId}")
    public Result<?> deleteUser(
            @Parameter(name = "userId", description = "待删除的用户ID", in = ParameterIn.PATH)
            @PathVariable("userId")
            Long userId) {

        boolean isDeleted = userService.removeById(userId);
        if (isDeleted) {

            Criteria criteria = new Criteria("id").is(userId);
            elasticsearchOperations.delete(new CriteriaQuery(criteria), User.class);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("isDeleted", isDeleted);
        return isDeleted ? Result.success(map) : Result.failure(map);

    }


}
