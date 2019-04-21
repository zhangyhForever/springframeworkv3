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
        this.templateRootDir = new File(templateRoot);
    }

    public GPView resolveViewName(String viewName, Locale locale){
        if(null == viewName || "".equals(viewName)){
            return null;
        }
        this.viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)?viewName:(viewName+DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath()+"/"+this.viewName).replaceAll("/+","/"));
        return new GPView(templateFile);
    }

    public String getViewName() {
        return this.viewName;
    }
}
