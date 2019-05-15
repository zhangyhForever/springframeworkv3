package com.forever.demo;

import com.forever.demo.service.impl.ActionService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

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
            Matcher matcher = pattern.matcher("class com.forever.demo.service.impl.ActionService");
            System.out.println(matcher.matches());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Class<ActionService> clazz = ActionService.class;
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.toString());
        }


    }
}
