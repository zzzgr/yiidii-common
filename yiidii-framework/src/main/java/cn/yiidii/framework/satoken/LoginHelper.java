package cn.yiidii.framework.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.yiidii.base.constant.Constant;
import cn.yiidii.base.core.LoginUser;

/**
 * LoginHelper
 *
 * @author ed w
 * @since 1.0
 */
public class LoginHelper {

    public static void login(LoginUser loginUser) {
        SaHolder.getStorage().set(Constant.LOGIN_USER, loginUser);
        StpUtil.login(loginUser.getUserId(),
                SaLoginConfig
                        .setExtra("id", loginUser.getUserId())
                        .setExtra("username", loginUser.getUsername())
                        .setExtra("nickname", loginUser.getNickname())
        );
        loginUser.setToken(StpUtil.getTokenInfo().getTokenValue());
        setLoginUser(loginUser);
    }

    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(Constant.LOGIN_USER, loginUser);
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(Constant.LOGIN_USER);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(Constant.LOGIN_USER);
        SaHolder.getStorage().set(Constant.LOGIN_USER, loginUser);
        return loginUser;
    }

    public static boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser.getUserId().equals(1L);
    }

    public static boolean isAdmin(Long id) {
        return id.equals(1L);
    }
}
