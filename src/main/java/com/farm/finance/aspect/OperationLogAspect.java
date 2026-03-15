package com.farm.finance.aspect;

import com.farm.finance.entity.SysOperationLog;
import com.farm.finance.service.SysOperationLogService;
import com.farm.finance.config.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SysOperationLogService sysOperationLogService;
    private final JwtUtils jwtUtils;

    /**
     * 切点：所有Controller层的增删改操作
     */
    @Pointcut("execution(* com.farm.finance.controller.*Controller.*(..)) && " +
              "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void operationLogPointcut() {}

    /**
     * 环绕通知：记录操作日志
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean success = true;
        Object result = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            try {
                // 异步记录日志，不影响主业务
                saveOperationLog(joinPoint, success, System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("记录操作日志失败", e);
            }
        }
    }

    private void saveOperationLog(ProceedingJoinPoint joinPoint, boolean success, long duration) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        
        // 获取当前用户信息
        String token = request.getHeader("Authorization");
        Long userId = null;
        String username = "anonymous";
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                userId = jwtUtils.extractUserId(token);
                username = jwtUtils.extractUsername(token);
            } catch (Exception e) {
                log.debug("解析token失败", e);
            }
        }

        // 构建操作描述
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String operation = buildOperationDescription(className, methodName);
        
        // 创建日志记录
        SysOperationLog log = new SysOperationLog();
        log.setUserId(userId != null ? userId : 0L);
        log.setUsername(username);
        log.setOperation(operation);
        log.setIp(getClientIp(request));
        log.setStatus(success);
        log.setIsActive(true);
        log.setCreatedTime(LocalDateTime.now());
        
        sysOperationLogService.save(log);
    }

    private String buildOperationDescription(String className, String methodName) {
        String module = className.replace("Controller", "");
        String action;
        
        if (methodName.startsWith("create") || methodName.startsWith("save") || methodName.equals("create")) {
            action = "创建";
        } else if (methodName.startsWith("update") || methodName.startsWith("edit")) {
            action = "更新";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            action = "删除";
        } else if (methodName.startsWith("import")) {
            action = "导入";
        } else if (methodName.startsWith("export")) {
            action = "导出";
        } else {
            action = "操作";
        }
        
        return action + module;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
