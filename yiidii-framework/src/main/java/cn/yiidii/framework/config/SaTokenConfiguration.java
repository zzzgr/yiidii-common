package cn.yiidii.framework.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpInterface;
import cn.yiidii.framework.satoken.PlusSaTokenDao;
import cn.yiidii.framework.satoken.StpInterfaceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 自动配置
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SaTokenDao.class, StpInterface.class})
public class SaTokenConfiguration {

    @Bean
    @ConditionalOnMissingBean(SaTokenDao.class)
    public SaTokenDao saTokenDao() {
        return new PlusSaTokenDao();
    }

    @Bean
    @ConditionalOnMissingBean(StpInterface.class)
    public StpInterface stpInterface() {
        return new StpInterfaceImpl();
    }
}
