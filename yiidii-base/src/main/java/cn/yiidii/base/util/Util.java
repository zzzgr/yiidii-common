package cn.yiidii.base.util;

import java.util.Objects;

public class Util {

    public static <T> T ifNull(T value, T dft) {
        if (Objects.isNull(value)) {
            return dft;
        }
        return value;
    }
}
