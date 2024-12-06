package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.enums.ContentStatus;
import pfp.fltv.common.model.po.manage.Permission;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.PermissionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/12/6 PM 4:01:49
 * @description 角色权限控制器
 * @filename PermissionController.java
 */

@AllArgsConstructor
@Tag(name = "角色权限操作接口")
@RestController
@RequestMapping("/content/user/role/permission")
public class PermissionController {


    private PermissionService permissionService;


    @LogRecord(description = "根据ID查询单条角色权限数据")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "根据ID查询单条角色权限数据")
    @GetMapping("/query/single/{id}")
    public Result<Permission> querySinglePermission(

            @Parameter(name = "id", description = "待查询的单条角色权限数据ID", in = ParameterIn.PATH, required = true) @PathVariable("id") Long id

    ) {

        Permission permission = permissionService.getById(id);
        return (permission == null) ? Result.failure(null) : Result.success(permission);

    }


    @LogRecord(description = "批量(分页)查询多条角色权限数据(对实体可见状态不做限制)")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "批量(分页)查询多条角色权限数据(对实体可见状态不做限制)")
    @GetMapping("/query/page/{pageNum}/{pageSize}")
    public Result<List<Permission>> queryPermissionPage(

            @Parameter(name = "pageNum", description = "查询的一页角色权限数据的数据页页码", in = ParameterIn.PATH, required = true) @PathVariable("pageNum") Long pageNum,
            @Parameter(name = "pageSize", description = "查询的这一页角色权限数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("pageSize") Long pageSize

    ) {

        Page<Permission> permissionPage = new Page<>(pageNum, pageSize);
        permissionPage = permissionService.page(permissionPage);

        return Result.success(permissionPage.getRecords() == null ? new ArrayList<>() : permissionPage.getRecords());

    }


    @LogRecord(description = "查询全部可见的角色权限数据")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "查询全部可见的角色权限数据")
    @GetMapping("/query/all/available")
    public Result<List<Permission>> queryAllPermissions() {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ContentStatus.NORMAL.getCode());
        List<Permission> permissions = permissionService.list(queryWrapper);
        return Result.success(permissions);

    }


    @GlobalTransactional(name = "insert-single-permission", rollbackFor = Exception.class)
    @LogRecord(description = "添加单条角色权限数据")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "添加单条角色权限数据")
    @PostMapping("/insert/single")
    public Result<Long> insertSinglePermission(

            @Parameter(name = "permission", description = "待添加的单条角色权限数据", required = true) @RequestBody Permission permission

    ) {

        boolean isSaved = permissionService.save(permission);
        return isSaved ? Result.success(permission.getId()) : Result.failure(-1L);

    }


    @GlobalTransactional(name = "update-single-permission", rollbackFor = Exception.class)
    @LogRecord(description = "修改单条角色权限数据")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "修改单条角色权限数据")
    @PutMapping("/update/single")
    public Result<?> updateSinglePermission(@RequestBody Permission permission) {

        boolean isUpdated = permissionService.updateById(permission);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @GlobalTransactional(name = "delete-single-permission", rollbackFor = Exception.class)
    @LogRecord(description = "删除单条角色权限数据")
    @SentinelResource("web-content-user-role-permission-controller")
    @Operation(description = "删除单条角色权限数据")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSinglePermission(@PathVariable("id") Long id) {

        boolean isDeleted = permissionService.removeById(id);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}
