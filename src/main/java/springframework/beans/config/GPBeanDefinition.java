package springframework.beans.config;

import lombok.Data;

/**
 * @Description: 封装xml配置文件中bean的定义信息
 * @Author: zhang
 * @Date: 2019/4/12
 */
@Data
public class GPBeanDefinition {

    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;

}
