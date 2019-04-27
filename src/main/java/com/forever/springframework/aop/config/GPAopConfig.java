package com.forever.springframework.aop.config;

import lombok.Data;

/**
 * @Description: aop配置文件映射类
 * @Author: zhang
 * @Date: 2019/4/27
 */
@Data
public class GPAopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}
