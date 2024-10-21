package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.info.AddressInfo;
import pfp.fltv.common.model.po.manage.User;
import pfp.fltv.common.model.po.response.Result;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.UserService;

import java.io.IOException;
import java.util.*;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:13:46
 * @description 用户控制器
 * @filename UserController.java
 */

@AllArgsConstructor
@Tag(name = "用户操作接口")
@RestController
@RequestMapping("/content/user")
public class UserController {


    private UserService userService;
    // private PasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;


    @LogRecord(description = "根据ID查询用户信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "根据ID查询用户信息")
    @GetMapping("/query/single/{userId}")
    public Result<User> querySingleUser(

            @Parameter(name = "userId", description = "待查询的用户ID", in = ParameterIn.PATH, required = true) @PathVariable("userId") Long userId

    ) {

        User user = userService.getById(userId);

        return (user == null) ? Result.failure(null) : Result.success(user);

    }


    @LogRecord(description = "批量(分页)查询多条用户数据")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "批量(分页)查询多条用户数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<User>> queryUserPage(

            @Parameter(name = "offset", description = "查询的一页用户数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页用户数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<User> userPage = new Page<>(offset, limit);
        userPage = userService.page(userPage);

        return Result.success(userPage.getRecords() == null ? new ArrayList<>() : userPage.getRecords());

    }


    @LogRecord(description = "根据ID集合查询多条用户数据")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "根据ID集合查询多条用户数据")
    @GetMapping("/query/byIds")
    public Result<List<User>> queryUsersByIds(

            @Parameter(name = "ids", description = "所要查询的用户ID集合", in = ParameterIn.QUERY, required = true) @RequestParam("ids") Set<Long> ids

    ) {

        List<User> users = userService.getBaseMapper().selectBatchIds(ids);

        return Result.success(users == null ? new ArrayList<>() : users);

    }


    @LogRecord(description = "查询所有用户信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "查询所有用户信息")
    @GetMapping("/query/all")
    public Result<List<User>> queryAllUsers() {

        List<User> users = userService.list();
        return Result.success(users);

    }


    @GlobalTransactional(name = "insert-single-user", rollbackFor = Exception.class)
    @LogRecord(description = "添加用户信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "添加用户信息")
    @PostMapping("/insert/single")
    public Result<Long> insertSingleUser(

            @Parameter(name = "user", description = "待添加的用户信息", required = true) @RequestBody User user

    ) {

        // 2024-5-3  20:58-TODO 必须进行用户密码加密
        // 2024-4-1  21:28-使用Spring Security配置中配置的密码加密验证器对用户原始密码进行加密
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean isSaved = userService.save(user);

        // 2024-10-15  14:04-非Passage实体将不再同步数据到ES中
        /*if (isSaved) {

            Result<?> result = restTemplate.postForObject(ES_INSERT_USER_URL, user, Result.class);
            map.put("es_result", result);

        }*/

        return isSaved ? Result.success(user.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-user", rollbackFor = Exception.class)
    @LogRecord(description = "修改用户信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "修改用户信息")
    @PutMapping("/update/single")
    public Result<?> updateSingleUser(

            @Parameter(name = "user", description = "待修改的用户信息", required = true) @RequestBody User user

    ) {

        boolean isUpdated = userService.updateById(user);

        // 2024-10-15  14:05-非Passage实体将不再同步数据到ES中
        /*if (isUpdated) {

            restTemplate.put(ES_UPDATE_USER_URL, user);
            map.put("es_result", Result.BLANK);

        }*/

        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-user", rollbackFor = Exception.class)
    @LogRecord(description = "删除用户信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "删除用户信息")
    @DeleteMapping("/delete/single/{userId}")
    public Result<?> deleteSingleUser(

            @Parameter(name = "userId", description = "当前用户ID", in = ParameterIn.PATH, required = true) @PathVariable("userId") Long userId

    ) {

        boolean isDeleted = userService.removeById(userId);

        // 2024-10-15  14:29-非Passage实体将不再同步数据到ES中
        /*if (isDeleted) {

            Map<String, Object> urlValues = new HashMap<>();
            urlValues.put("id", userId);
            restTemplate.delete(ES_DELETE_USER_URL, urlValues);
            map.put("es_result", Result.BLANK);

        }*/

        return isDeleted ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "更新用户当前的地理位置信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "更新用户当前的地理位置信息")
    @PostMapping("/refresh/geolocation/{userId}")
    public Result<Map<String, Object>> refreshGeolocation(

            @Parameter(name = "userId", description = "当前用户ID", in = ParameterIn.PATH, required = true) @PathVariable("userId") Long userId,
            @RequestBody AddressInfo addressInfo

    ) {

        User refreshedUser = userService.refreshGeolocation(userId, addressInfo);

        Map<String, Object> map = new HashMap<>();

        if (refreshedUser != null) {

            // 2024-6-24  17:01-将无数据的字段去除 , 以降低通信数据大小
            Map<String, Object> strippedUserMap = ReflectUtils.bean2Map(refreshedUser, false);
            map.put("refreshed-user", strippedUserMap);

            map.put("msg", "成功更新用户当前的地理位置信息");
            return Result.success(map);

        } else {

            map.put("msg", "更新用户当前的地理位置信息失败");
            return Result.failure(map);

        }

    }


    @LogRecord(description = "更新用户当前的地理位置信息")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "更新用户当前的地理位置信息")
    @PostMapping("/query/nearby")
    public Result<Map<Integer, List<Map<String, Object>>>> findPeopleNearby(

            @Parameter(name = "userId", description = "当前的用户ID", required = true) @RequestParam("userId") Long userId,
            @Parameter(name = "radius", description = "所要查询的半径范围(单位 : km)", required = true) @RequestParam("radius") Double radius,
            @Parameter(name = "limit", description = "所要查询的指定半径范围内的附近的人的最大人数", required = true) @RequestParam("limit") Long limit,
            @RequestBody AddressInfo addressInfo

    ) {

        Map<Integer, List<User>> peoplesNearby = userService.findPeopleNearby(userId, addressInfo.getLongitude(), addressInfo.getLatitude(), radius, limit);

        // 2024-6-24  17:12-给拉取到的 附近的人 数据瘦瘦身 , 移除掉没有意义的数据字段 , 降低通信数据体积
        Map<Integer, List<Map<String, Object>>> strippedPeoplesNearby = new HashMap<>();

        for (Map.Entry<Integer, List<User>> entry : peoplesNearby.entrySet()) {

            Integer estimatedDistance = entry.getKey();
            List<User> subPeoplesNearby = entry.getValue();

            strippedPeoplesNearby.put(estimatedDistance, new ArrayList<>());

            for (User user : subPeoplesNearby) {

                Map<String, Object> strippedUser = ReflectUtils.bean2Map(user, false);
                strippedPeoplesNearby.get(estimatedDistance).add(strippedUser);

            }

        }

        return Result.success(strippedPeoplesNearby);

    }


    @LogRecord(description = "更新用户当前的头像")
    @SentinelResource("web-content-user-controller")
    @Operation(description = "更新用户当前的头像")
    @PostMapping("/update/avatar")
    public Result<String> changeAvatar(

            @Parameter(name = "userId", description = "当前的用户ID", required = true) @RequestParam("userId") Long userId,
            @Parameter(name = "picture", description = "用户的新的头像图片文件", required = true) @RequestParam("picture") MultipartFile picture

    ) throws IOException, InterruptedException {

        if (picture.isEmpty()) {

            throw new PtpException(816, "用户上传的文件为空！");

        }

        String uri = userService.updateAvatar(userId, picture);

        if (StringUtils.hasLength(uri)) {

            return Result.success(uri);

        } else {

            return Result.failure("Upload picture failed");

        }

    }


}
