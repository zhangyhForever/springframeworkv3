package com.forever.springframework.webmvc.servlet;

import java.util.Map;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */
public class GPModelAndView {

    private String viewName;
    private Map<String, ?> model;

    public GPModelAndView(String viewName) {
        this(viewName, null);
    }

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
