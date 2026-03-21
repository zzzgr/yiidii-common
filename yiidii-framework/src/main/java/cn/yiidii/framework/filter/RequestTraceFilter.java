package cn.yiidii.framework.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yiidii.framework.constant.Constant;
import cn.yiidii.framework.util.ContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 请求追踪日志
 */
@Slf4j
@RequiredArgsConstructor
public class RequestTraceFilter extends OncePerRequestFilter {

    private final int maxBodyLength;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String traceId = resolveTraceId(request);

        request.setAttribute(Constant.TRACE_ID, traceId);
        response.setHeader(Constant.TRACE_HEADER, traceId);
        ContextUtil.set(Constant.TRACE_ID, traceId);
        MDC.put(Constant.TRACE_ID, traceId);

        ContentCachingRequestWrapper requestWrapper = wrapRequest(request);
        ContentCachingResponseWrapper responseWrapper = wrapResponse(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                log.info(
                    "请求追踪 traceId={}, status={}, duration={}ms, uri={}, method={}, ip={}, params={}, requestBody={}, responseBody={}",
                    traceId,
                    responseWrapper.getStatus(),
                    System.currentTimeMillis() - startTime,
                    request.getRequestURI(),
                    request.getMethod(),
                    getClientIp(request),
                    JSONUtil.toJsonStr(getRequestParams(request)),
                    readPayload(requestWrapper.getContentAsByteArray(), requestWrapper.getContentType()),
                    readPayload(responseWrapper.getContentAsByteArray(), responseWrapper.getContentType())
                );
            } finally {
                responseWrapper.copyBodyToResponse();
                ContextUtil.clear();
                MDC.remove(Constant.TRACE_ID);
            }
        }
    }

    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        return wrapper != null ? wrapper : new ContentCachingRequestWrapper(request);
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        return wrapper != null ? wrapper : new ContentCachingResponseWrapper(response);
    }

    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            }
        });
        return params;
    }

    private String readPayload(byte[] content, String contentType) {
        if (content == null || content.length == 0) {
            return "";
        }
        if (StrUtil.startWithIgnoreCase(contentType, MediaType.MULTIPART_FORM_DATA_VALUE)
            || StrUtil.startWithIgnoreCase(contentType, MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
            return "[binary omitted]";
        }
        String body = new String(content, StandardCharsets.UTF_8);
        if (body.length() <= maxBodyLength) {
            return body;
        }
        return body.substring(0, maxBodyLength) + "...(truncated)";
    }

    private String resolveTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(Constant.TRACE_HEADER);
        if (StrUtil.isNotBlank(traceId)) {
            return traceId;
        }
        return UUID.randomUUID().toString().replace("-", "");
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
        return StrUtil.split(ip, ',').stream().findFirst().map(String::trim).orElse(ip);
    }
}
