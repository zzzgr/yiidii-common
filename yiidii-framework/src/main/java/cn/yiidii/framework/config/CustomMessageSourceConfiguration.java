package cn.yiidii.framework.config;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class CustomMessageSourceConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        String[] strings = scanModelsForI18nFolders();
        messageBundle.setBasenames(strings);
        messageBundle.setDefaultEncoding("UTF-8");
        log.info("加载messageSource：{}", ArrayUtil.join(strings, ","));
        return messageBundle;
    }

    public String[] scanModelsForI18nFolders() {
        try {
            String resourcePattern = "classpath*:i18n/*.properties";
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(resourcePattern);

            Set<String> pathSet = new HashSet<>();
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if (fileName == null || !fileName.endsWith(".properties")) {
                    continue;
                }
                String basename = fileName.substring(0, fileName.length() - ".properties".length());
                if (basename.matches(".*_[a-z]{2}_[A-Z]{2}$")) {
                    basename = basename.substring(0, basename.length() - 6);
                } else if (basename.matches(".*_[a-z]{2}$")) {
                    basename = basename.substring(0, basename.length() - 3);
                }
                pathSet.add("classpath:i18n/" + basename);
            }

            return pathSet.toArray(new String[0]);
        } catch (Exception e) {
            log.warn("扫描 i18n 资源失败", e);
        }
        return new String[0];
    }

    @Bean
    @ConditionalOnMissingBean(LocalValidatorFactoryBean.class)
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
