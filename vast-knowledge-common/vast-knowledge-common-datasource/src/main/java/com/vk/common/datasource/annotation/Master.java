package com.vk.common.datasource.annotation;


import com.mybatisflex.annotation.UseDataSource;

import java.lang.annotation.*;

/**
 * 主库数据源
 * 
 * @author ruoyi
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UseDataSource("master")
public @interface Master
{

}