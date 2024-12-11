package ptp.fltv.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.annotation.LogRecord;
import pfp.fltv.common.model.po.manage.Role;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.RoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/3/27 下午 7:05:08
 * @description 角色控制器
 * @filename RoleController.java
 */

@AllArgsConstructor
@Tag(name = "角色操作接口")
@RestController
@RequestMapping("/content/user/role")
public class RoleController {


    private RoleService roleService;


    @LogRecord(description = "根据ID查询角色信息")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "根据ID查询角色信息")
    @GetMapping("/query/single/{roleId}")
    public Result<Role> queryRoleById(

            @Parameter(name = "roleId", description = "待查询的角色ID", in = ParameterIn.PATH, required = true) @PathVariable("roleId") Long roleId

    ) {

        Role role = roleService.getById(roleId);
        return (role == null) ? Result.failure(null) : Result.success(role);

    }


    @LogRecord(description = "批量(分页)查询多条角色数据")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "批量(分页)查询多条角色数据")
    @GetMapping("/query/page/{offset}/{limit}")
    public Result<List<Role>> queryRolePage(

            @Parameter(name = "offset", description = "查询的一页角色数据的起始偏移量", in = ParameterIn.PATH, required = true) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页角色数据的数量", in = ParameterIn.PATH, required = true) @PathVariable("limit") Long limit

    ) {

        Page<Role> rolePage = new Page<>(offset, limit);
        rolePage = roleService.page(rolePage);

        return Result.success(rolePage.getRecords() == null ? new ArrayList<>() : rolePage.getRecords());

    }


    @LogRecord(description = "查询所有角色信息")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "查询所有角色信息")
    @GetMapping("/query/all")
    public Result<List<Role>> queryAllRoles() {

        List<Role> roles = roleService.list();
        return Result.success(roles);

    }


    @LogRecord(description = "添加角色信息")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "添加角色信息")
    @PostMapping("/insert/single")
    public Result<Long> insertRole(

            @Parameter(name = "role", description = "待添加的角色信息", required = true) @RequestBody Role role

    ) {

        boolean isSaved = roleService.save(role);
        return isSaved ? Result.success(role.getId()) : Result.failure(-1L);

    }


    @LogRecord(description = "修改角色信息")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "修改角色信息")
    @PutMapping("/update/single")
    public Result<?> updateRole(

            @Parameter(name = "role", description = "待修改的角色信息", required = true) @RequestBody Role role

    ) {

        boolean isUpdated = roleService.updateById(role);
        return isUpdated ? Result.success(null) : Result.failure(null);

    }


    @LogRecord(description = "删除角色信息")
    @SentinelResource("web-content-user-role-controller")
    @Operation(description = "删除角色信息")
    @DeleteMapping("/delete/single/{roleId}")
    public Result<?> deleteRole(

            @Parameter(name = "roleId", description = "待删除的角色ID", in = ParameterIn.PATH, required = true) @PathVariable("roleId") Long roleId

    ) {

        boolean isDeleted = roleService.removeById(roleId);
        return isDeleted ? Result.success(null) : Result.failure(null);

    }


}
