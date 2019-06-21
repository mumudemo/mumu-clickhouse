package com.lovecws.mumu.clickhouse.annotation;

import java.lang.annotation.*;

/**
 * @program: act-able
 * @description: 分区策略
 * @author: 甘亮
 * @create: 2019-05-29 16:14
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TablePartition {

    /**
     * 是否分区
     *
     * @return
     */
    public boolean partition();

    /**
     * 分区字段(分区字段在实体对象存在),多字段分区按照数组顺序创建
     *
     * @return
     */
    public String[] partitionFields();


    /**
     * 分区类型  hive为默认分区，pg分区类型 list,range
     *
     * @return
     */
    public String partitionType() default "";

    /**
     * 分区策略
     *
     * @return
     */
    public String[] partitionStragety() default {};


    /**
     * 数据库类型  pg hive oracle
     *
     * @return
     */
    public String databaseType();
}
