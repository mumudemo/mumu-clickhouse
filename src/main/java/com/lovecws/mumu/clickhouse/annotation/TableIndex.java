package com.lovecws.mumu.clickhouse.annotation;

import java.lang.annotation.*;

/**
 * @program: act-able
 * @description: 索引
 * @author: 甘亮
 * @create: 2019-05-29 16:14
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableIndex {

    /**
     * 是否创建索引
     *
     * @return
     */
    public boolean indexing();

    /**
     * 索引类型 主键索引pk 普通索引 index
     *
     * @return
     */
    public String indexType();

    /**
     * 索引字段(索引字段在实体对象存在),多字段索引按照数组顺序创建
     *
     * @return
     */
    public String[] indexFields();

    /**
     * 索引策略(覆盖indexType、indexFields配置信息) 是当出现主键索引和普通索引的时候，使用策略来生效
     * id:pk,name:index
     *
     * @return
     */
    public String[] indexStragety() default {};
}
