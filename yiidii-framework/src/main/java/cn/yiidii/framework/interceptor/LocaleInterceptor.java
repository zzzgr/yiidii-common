package cn.yiidii.framework.interceptor;

import cn.yiidii.base.util.ContextUtil;
import cn.yiidii.base.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * Locale过滤器
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
public class LocaleInterceptor implements HandlerInterceptor {

    private static final Map<String, Locale> LOCALE_PMAP = Map.of(
            "zh", Locale.CHINA,
            "en", new Locale("en_US")
    );


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String lang = Util.ifNull(request.getParameter("lang"), "zh");
        if (LOCALE_PMAP.containsKey(lang)) {
            LocaleContextHolder.setLocale(LOCALE_PMAP.get(lang));
        }
        return true;
    }
}
