package com.forever.springframework.webmvc.servlet;

import com.forever.springframework.annotation.GPController;
import com.forever.springframework.annotation.GPRequestMapping;
import com.forever.springframework.context.GPApplicationContext;
import com.forever.springframework.webmvc.GPView;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
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

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = new HashMap<GPHandlerMapping, GPHandlerAdapter>();

    private List<GPViewResolver> viewResolvers = new ArrayList<GPViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("detail", "自己抛的异常");
                model.put("stackTrace", Arrays.toString(e.getStackTrace()));
                processDispatchResult(req, resp, new GPModelAndView("500", model));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获得handlerMapping(封装的url和method映射关系)
        GPHandlerMapping handler = getHandler(req);
        if(handler == null){
            processDispatchResult(req, resp, new GPModelAndView("404"));
            return;
        }

        //从handlerAdapters中根据handlerMapping找到对应的handlerAdapter
        GPHandlerAdapter ha = handlerAdapters.get(handler);

        //将handlerMapping交给handlerAdapter处理(将请求转换成方法的执行)，
        //并返回一个ModelAndView(主要封装返回的参数以及页面名称)
        GPModelAndView mv = ha.handle(req, resp, handler);

        //渲染页面(根据ModelAndView找到前台页面，并赋值给页面中的参数)，并返回结果
        processDispatchResult(req, resp, mv);


    }

    /**
     * 根据视图名称找到对应的html文件，并将页面中的参数赋值（也就是渲染）返回
     * @param req
     * @param resp
     * @param mv
     * @throws Exception
     */
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, GPModelAndView mv) throws Exception {
        if(null == mv){
            return;
        }
        if(this.viewResolvers != null){
            for(GPViewResolver viewResolver: this.viewResolvers){
                GPView view = viewResolver.resolveViewName(mv.getViewName(), null);
                if(null != view){
                    view.render(mv.getModel(), req, resp);
                    return;
                }
            }
        }
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {
        if(handlerMappings.isEmpty()){
            return null;
        }
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String url = uri.replace(contextPath,"").replaceAll("/+","/");
        for(GPHandlerMapping handler: handlerMappings){
            Matcher matcher = handler.getPattern().matcher(url);
            if(matcher.matches()){
                return handler;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter(LOCATION));
        log.info("配置文件位置====="+config.getInitParameter(LOCATION));
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
        /**
         * @Description:  请求参数和方法参数匹配
         * @Author: zhang
         * @Date: 2019/4/14
         */
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
                log.info(regex+"==Mapping====>"+method);
            }
        }
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        for(GPHandlerMapping handlerMapping: this.handlerMappings){
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdapter());
        }
    }

    /**
     * @Description:
     * @Author: zhang
     * @Date: 2019/4/14
     */
    private void initViewResolvers(GPApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String webInf = new File(this.getClass().getResource("/").getFile()).getParent();
        templateRoot = webInf+File.separator+templateRoot;
        log.info("html存储路径=========="+templateRoot);
        File[] files = new File(templateRoot).listFiles();
        for(File file: files){
            viewResolvers.add(new GPViewResolver(templateRoot));
        }
    }


    private void initRequestToViewNameTranslator(GPApplicationContext context) {

    }

    private void initThemeResolver(GPApplicationContext context) {

    }

    private void initLocaleResolver(GPApplicationContext context) {

    }

    private void initMultipartResolver(GPApplicationContext context) {

    }

    private void initFlashMapManager(GPApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {

    }
}
