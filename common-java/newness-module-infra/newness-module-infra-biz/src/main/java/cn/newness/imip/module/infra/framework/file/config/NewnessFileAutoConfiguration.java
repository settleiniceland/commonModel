package cn.newness.imip.module.infra.framework.file.config;

import cn.newness.imip.module.infra.framework.file.core.client.FileClientFactory;
import cn.newness.imip.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 *
 * @author 新奇源码
 */
@Configuration(proxyBeanMethods = false)
public class NewnessFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
