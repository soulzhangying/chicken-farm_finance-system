package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.config.JwtUtils;
import com.farm.finance.dto.LoginRequest;
import com.farm.finance.dto.LoginResponse;
import com.farm.finance.dto.RegisterRequest;
import com.farm.finance.entity.SysUser;
import com.farm.finance.repository.SysUserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(request.getUsername());

        SysUser user = sysUserRepository.findByUsername(request.getUsername()).orElse(null);

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(request.getUsername())
                .realName(user != null ? user.getRealName() : null)
                .role(user != null ? user.getRole() : null)
                .build();

        return Result.success("登录成功", response);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        if (sysUserRepository.existsByUsername(request.getUsername())) {
            return Result.error(400, "用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName() != null ? request.getRealName() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setRole("USER");
        user.setIsActive(true);
        user.setCreatedTime(LocalDateTime.now());

        sysUserRepository.save(user);

        return Result.success("注册成功", null);
    }

    @GetMapping("/me")
    public Result<SysUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        SysUser user = sysUserRepository.findByUsername(username)
                .orElse(null);

        if (user != null) {
            user.setPassword(null); // 不返回密码
        }

        return Result.success(user);
    }
}
