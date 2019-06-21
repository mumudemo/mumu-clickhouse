package com.lovecws.mumu.clickhouse.annotation;

import java.lang.annotation.*;

/**
 * @program: act-able
 * @description: hive表字段属性 存储方式 字符换行符等属性
 * @author: 甘亮
 * @create: 2019-05-29 16:14
 * parquet: org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe
 *          org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat
 *          org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat
 *
 * textfile:     org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe
 *          org.apache.hadoop.mapred.TextInputFormat
 *          org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableProperties {

    /**
     * 数据存储格式 csv(默认数据格式) 、avro 、parquet
     *
     * @return
     */
    public String storage() default "";

    /**
     * 存储输入格式化(csv格式默认) org.apache.hadoop.mapred.TextInputFormat
     *
     * @return
     */
    public String inputFormat() default "";

    /**
     * 存储输出格式化(csv格式默认) org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat
     *
     * @return
     */
    public String outputFormat() default "";

    /**
     * 数据存储位置 默认为hive内部表
     *
     * @return
     */
    public String location() default "";

    /**
     * 序列化 反序列化 默认 org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe
     *
     * @return
     */
    public String serde() default "";

    /**
     * TBLPROPERTIES 字段说明  csv格式  换行、map、list分隔符
     *
     * @return
     */
    public String properties() default "";
}
