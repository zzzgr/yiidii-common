package cn.yiidii.framework.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yiidii.framework.annotation.IgnoreRequestTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 检查是否有忽略注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 检查方法上是否有注解
            IgnoreRequestTrace methodAnnotation = handlerMethod.getMethodAnnotation(IgnoreRequestTrace.class);
            if (methodAnnotation != null && methodAnnotation.value()) {
                return true;
            }
            
            // 检查类上是否有注解
            IgnoreRequestTrace classAnnotation = handlerMethod.getBeanType().getAnnotation(IgnoreRequestTrace.class);
            if (classAnnotation != null && classAnnotation.value()) {
                return true;
            }
        }
        
        // 包装request，使其可重复读取
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // 获取请求信息
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        // 获取请求参数
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            }
        });

        // 获取请求体
        String body = "";
        if (requestWrapper.getContentAsByteArray().length > 0) {
            body = new String(requestWrapper.getContentAsByteArray());
        }

        // 记录请求信息
        log.info("请求追踪 - URI: {}, Method: {}, IP: {}, User-Agent: {}, Params: {}, Body: {}",
                requestURI,
                method,
                ip,
                userAgent,
                JSONUtil.toJsonStr(params),
                body);

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 