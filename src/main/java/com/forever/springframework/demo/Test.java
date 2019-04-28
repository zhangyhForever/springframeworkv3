package com.forever.springframework.demo;

import com.forever.springframework.demo.service.impl.ActionService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: zhang
 * @Date: 2019/4/14
 */

@Slf4j
public class Test {

    public static void main(String[] args) {
        /*InputStream in = Test.class.getClassLoader().getResourceAsStream("application.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            String pointCut = prop.getProperty("pointCut")
                    .replaceAll("\\.", "\\\\.")
                    .replaceAll("\\\\.\\*", ".*")
                    .replaceAll("\\(", "\\\\(")
                    .replaceAll("\\)", "\\\\)");
            System.out.println(pointCut);
            String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(")-4);
            System.out.println(pointCutForClassRegex);
            pointCutForClassRegex = "class "+pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ")+1);
            System.out.println(pointCutForClassRegex);
            Pattern pattern = Pattern.compile(pointCutForClassRegex);
            Matcher matcher = pattern.matcher("class com.forever.springframework.demo.service.impl.ActionService");
            System.out.println(matcher.matches());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Class<ActionService> clazz = ActionService.class;
        try {
            for (Method declaredMethod : clazz.getMethods()) {
                System.out.println(declaredMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
