package com.lovecws.mumu.clickhouse.annotation;

import java.lang.annotation.*;

/**
 * @program: act-able
 * @description: TODO
 * @author: 甘亮
 * @create: 2019-05-29 16:13
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableField {

    /**
     * 字段存储方式 hive字段名称
     *
     * @return
     */
    public String name();

    /**
     * 存储数据类型 数据字段类型
     *
     * @return
     */
    public String type() default "";

    /**
     * hive字段注释
     *
     * @return
     */
    public String comment() default "";

    /**
     * hive日期格式
     *
     * @return
     */
    public String format() default "";
}
