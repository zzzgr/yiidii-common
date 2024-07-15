package cn.yiidii.base.util;

import cn.hutool.core.convert.Convert;
import cn.yiidii.base.constant.Constant;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文工具类
 *
 * @author ed w
 */
@SuppressWarnings("unused")
public class ContextUtil {

    private ContextUtil() {
    }

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, Object> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, Object> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, def));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public static <T> T getUserId(Class<T> type) {
        return get(Constant.USER_ID, type, 0);
    }


    /**
     * 用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Number userId) {
        set(Constant.USER_ID, userId);
    }

}
