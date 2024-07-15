package cn.yiidii.framework.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class CustomMessageSourceConfiguration {

    @Bean
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
            // 通配规则 直接获取 Resources中的内容
            String resourcePattern = "classpath*:i18n/*";
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(resourcePattern);

            Set<String> pathSet = new HashSet<>();
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                String[] urlStrArr = resource.getURL().toString().split("/");
                int index = urlStrArr.length - 1;
                String fileName = urlStrArr[index];
                String moduleName = fileName.split("_")[0];
                // 通配符加+ 模块名 + /国际化数据目录
//                pathSet.add("classpath:i18n/" + moduleName + "_messages");
                pathSet.add("classpath:i18n/" + moduleName + "_messages");
            }

            return pathSet.toArray(new String[0]);
        } catch (Exception ignored) {

        }
        return null;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

}
