package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.SysUser;
import com.farm.finance.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sys-users")
@RequiredArgsConstructor
public class SysUserController {
    
    private final SysUserService sysUserService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<SysUser>> findAll() {
        return Result.success(sysUserService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<SysUser> findById(@PathVariable Long id) {
        return sysUserService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }
    
    @GetMapping("/username/{username}")
    public Result<SysUser> findByUsername(@PathVariable String username) {
        return sysUserService.findByUsername(username)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/real-name/{realName}")
    public Result<List<SysUser>> findByRealName(@PathVariable String realName) {
        return Result.success(sysUserService.findByRealName(realName));
    }
    
    @GetMapping("/real-name/search")
    public Result<List<SysUser>> findByRealNameContaining(@RequestParam String keyword) {
        return Result.success(sysUserService.findByRealNameContaining(keyword));
    }
    
    @GetMapping("/phone/{phone}")
    public Result<SysUser> findByPhone(@PathVariable String phone) {
        return sysUserService.findByPhone(phone)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }
    
    @GetMapping("/phone/search")
    public Result<List<SysUser>> findByPhoneContaining(@RequestParam String keyword) {
        return Result.success(sysUserService.findByPhoneContaining(keyword));
    }
    
    @GetMapping("/role/{role}")
    public Result<List<SysUser>> findByRole(@PathVariable String role) {
        return Result.success(sysUserService.findByRole(role));
    }
    
    @GetMapping("/role/{role}/active")
    public Result<List<SysUser>> findByRoleAndActive(@PathVariable String role) {
        return Result.success(sysUserService.findByRoleAndIsActiveTrue(role));
    }
    
    @GetMapping("/active")
    public Result<List<SysUser>> findAllActive() {
        return Result.success(sysUserService.findAllActive());
    }
    
    @GetMapping("/status/{isActive}")
    public Result<List<SysUser>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(sysUserService.findByIsActive(isActive));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<SysUser>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(sysUserService.findAll(pageable));
    }
    
    @GetMapping("/username/search/page")
    public Result<Page<SysUser>> findByUsernameContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysUserService.findByUsernameContaining(keyword, pageable));
    }
    
    @GetMapping("/real-name/search/page")
    public Result<Page<SysUser>> findByRealNameContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysUserService.findByRealNameContaining(keyword, pageable));
    }
    
    @GetMapping("/role/{role}/page")
    public Result<Page<SysUser>> findByRolePage(
            @PathVariable String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysUserService.findByRole(role, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<SysUser>> search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(sysUserService.search(username, realName, phone, role, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<SysUser>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(sysUserService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(sysUserService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/role/{role}")
    public Result<Long> countByRole(@PathVariable String role) {
        return Result.success(sysUserService.countByRole(role));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<SysUser> create(@RequestBody SysUser user) {
        if (sysUserService.existsByUsername(user.getUsername())) {
            return Result.error(409, "用户名已存在");
        }
        if (user.getPhone() != null && sysUserService.existsByPhone(user.getPhone())) {
            return Result.error(409, "手机号已被使用");
        }
        return Result.success("创建成功", sysUserService.save(user));
    }
    
    @PutMapping("/{id}")
    public Result<SysUser> update(@PathVariable Long id, @RequestBody SysUser user) {
        return sysUserService.findById(id)
                .map(existing -> {
                    user.setId(id);
                    // 如果密码为空，保留原密码
                    if (user.getPassword() == null || user.getPassword().isEmpty()) {
                        user.setPassword(existing.getPassword());
                    }
                    return Result.success("更新成功", sysUserService.save(user));
                })
                .orElse(Result.error(404, "用户不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (sysUserService.findById(id).isEmpty()) {
            return Result.error(404, "用户不存在");
        }
        sysUserService.deleteById(id);
        return Result.success();
    }
}
