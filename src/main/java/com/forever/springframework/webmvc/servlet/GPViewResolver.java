package com.forever.springframework.webmvc.servlet;

import com.forever.springframework.webmvc.GPView;

import java.io.File;
import java.util.Locale;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    private String viewName;

    public GPViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public GPView resolveViewName(String viewName, Locale locale){
        if(null == viewName || "".equals(viewName)){
            return null;
        }
        this.viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)?viewName:(viewName+DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/+","/"));
        return new GPView(templateFile);
    }
}
