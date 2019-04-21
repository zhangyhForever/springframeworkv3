package com.forever.springframework.webmvc;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/16
 */
@Slf4j
public class GPView {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";

    private File viewFile;

    public GPView(File viewFile){
        this.viewFile = viewFile;
    }

    public String getContentType(){
        return DEFAULT_CONTENT_TYPE;
    }

    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse res) throws Exception {
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");

        String line = null;
        while(null != (line=ra.readLine())){
            line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()){
                String paramName = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}", "");
                Object paramValue = model.get(paramName);
                if(null == paramValue){
                    log.error("未匹配到参数{"+paramName+"}");
                    return;
                }
                line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
//                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        ra.close();
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(sb.toString());
    }

    private String makeStringForRegExp(String paramValue) {
        return paramValue.replace("\\","\\\\").replace("*","\\*")
                .replace("+","\\+").replace("|","\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }
}
