package springframework.webmvc.servlet;

import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPRequestMapping;
import com.forever.springframework.context.GPApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
@Slf4j
public class GPDispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    private List<GPHandlerMapping> handlerMappings = new ArrayList<GPHandlerMapping>();
    private Map<GPHandlerMapping, GPHandlerAdepter> handlerAdapters = new HashMap<GPHandlerMapping, GPHandlerAdepter>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcher(req, resp);
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(GPApplicationContext context) {
        //初始化九大组件
        //多文件上传组件
        initMultipartResolver(context);
        //本地化解析
        initLocaleResolver(context);
        //主题解析
        initThemeResolver(context);
        /**
        * @Description:  url和方法映射
        * @Author: zhang
        * @Date: 2019/4/14
        */
        initHandlerMappings(context);
        //动态匹配method参数值
        initHandlerAdapters(context);
        //异常处理
        initHandlerExceptionResolvers(context);
        /**
        * @Description:  解析请求到视图名
        * @Author: zhang
        * @Date: 2019/4/14
        */
        initRequestToViewNameTranslator(context);
        /**
        * @Description:  视图解析器，自己解析一套前台模板
        * @Author: zhang
        * @Date: 2019/4/14
        */
        initViewResolvers(context);
        //flash映射管理器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {

    }

    /**
    * @Description:
    * @Author: zhang
    * @Date: 2019/4/14
    */
    private void initViewResolvers(GPApplicationContext context) {

    }

    /**
    * @Description:  
    * @Author: zhang
    * @Date: 2019/4/14
    */
    private void initRequestToViewNameTranslator(GPApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {

    }

    private void initHandlerAdapters(GPApplicationContext context) {
        for(GPHandlerMapping handlerMapping: this.handlerMappings){
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdepter());
        }
    }

    /**
    * @Description:  
    * @Author: zhang
    * @Date: 2019/4/14
    */
    private void initHandlerMappings(GPApplicationContext context) {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String beanName: beanDefinitionNames){
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();
            if(!clazz.isAnnotationPresent(GPController.class)){
                continue;
            }
            String baseUrl = "";
            if(clazz.isAnnotationPresent(GPRequestMapping.class)){
                baseUrl = clazz.getAnnotation(GPRequestMapping.class).value();
            }
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method: methods){
                if(!method.isAnnotationPresent(GPRequestMapping.class)){
                    continue;
                }
                String methodUrl = method.getAnnotation(GPRequestMapping.class).value();
                Pattern regex = Pattern.compile(baseUrl + methodUrl);
                GPHandlerMapping handlerMapping = new GPHandlerMapping(regex, controller, method);
                handlerMappings.add(handlerMapping);
                log.info("Mapping===="+regex+"--->"+method);
            }
        }
    }

    private void initThemeResolver(GPApplicationContext context) {

    }

    private void initLocaleResolver(GPApplicationContext context) {

    }

    private void initMultipartResolver(GPApplicationContext context) {

    }
}
