package com.farm.finance.service;

import com.farm.finance.entity.SysUser;
import com.farm.finance.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("尝试加载用户: {}", username);
        
        SysUser sysUser = sysUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("用户不存在: {}", username);
                    return new UsernameNotFoundException("用户不存在: " + username);
                });

        // 检查用户是否被禁用
        if (!sysUser.getIsActive()) {
            log.warn("用户已被禁用: {}", username);
            throw new DisabledException("用户已被禁用");
        }

        log.info("用户加载成功: {}, 角色: {}, 活跃状态: {}", username, sysUser.getRole(), sysUser.getIsActive());
        
        return User.builder()
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + sysUser.getRole())))
                .accountLocked(!sysUser.getIsActive())
                .build();
    }
}
