package com.vk.common.datasource.annotation;


import com.mybatisflex.annotation.UseDataSource;

import java.lang.annotation.*;

/**
 * 从库数据源
 * 
 * @author ruoyi
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UseDataSource("slave")
public @interface Slave
{

}