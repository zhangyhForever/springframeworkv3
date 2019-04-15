package springframework.beans.support;

import com.forever.springframework.beans.config.GPBeanDefinition;
import com.forever.springframework.context.support.GPAbstractApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/12
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    //用来存放xml文件中映射的bean的信息
    public Map<String, GPBeanDefinition> beanDefinitionMap = new HashMap<String, GPBeanDefinition>();
}
