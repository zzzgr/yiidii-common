package cn.yiidii.framework.interceptor;

import cn.yiidii.base.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上下文过滤器
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
public class CompletionInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ContextUtil.clear();
        LocaleContextHolder.resetLocaleContext();
    }
}
