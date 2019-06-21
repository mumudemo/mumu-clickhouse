package com.lovecws.mumu.clickhouse.util;

import com.lovecws.mumu.clickhouse.annotation.TableField;
import com.lovecws.mumu.clickhouse.annotation.TableIndex;
import com.lovecws.mumu.clickhouse.annotation.TablePartition;
import com.lovecws.mumu.clickhouse.annotation.TableProperties;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @program: act-able
 * @description: 字段工具类
 * @author: 甘亮
 * @create: 2019-06-03 13:26
 **/
public class TableUtil {

    private static final Logger log = Logger.getLogger(TableUtil.class);

    /**
     * 获取表格字段信息
     *
     * @param object 实体对象信息
     * @return
     */
    public static Map<String, Object> getTableInfo(Object object) {
        Map<String, Object> tableInfoMap = new HashMap<>();
        if (object == null) return tableInfoMap;

        //获取列字段信息
        List<Map<String, String>> columns = new ArrayList<>();
        Map<String, Object> partitionMap = new HashMap<>();
        Map<String, Object> propertiesMap = new HashMap<>();
        List<Map<String, Object>> indexings = new ArrayList<>();
        //当对象为map类型的时候，字段为key
        if (object instanceof Map) {
            List<Map<String, Object>> flatMapKeys = MapFieldUtil.getFlatMapKeys((Map<String, Object>) object);
            flatMapKeys.forEach(map -> {
                Map<String, String> columnMap = new HashMap<>();
                columnMap.put("name", map.get("name").toString());
                columnMap.put("type", map.get("type").toString());
                columnMap.put("comment", "");
                columnMap.put("partition", "false");
                columnMap.put("indexing", "false");
                columns.add(columnMap);
            });
        } else {
            //分区字段
            Map<String, Map<String, String>> partitionColumnMap = new HashMap<String, Map<String, String>>();
            List<String> partitionFields = new ArrayList<>();
            TablePartition tablePartition = object.getClass().getAnnotation(TablePartition.class);
            if (tablePartition != null && tablePartition.partition()) {
                partitionFields.addAll(Arrays.asList(tablePartition.partitionFields()));
                partitionMap.put("type", tablePartition.partitionType());
                partitionMap.put("fields", StringUtils.join(tablePartition.partitionFields(), ","));
                Map<String, Object> tableMap = new HashMap<>();
                String[] partitionStragetys = tablePartition.partitionStragety();
                for (String partitionStragety : partitionStragetys) {
                    String[] split = partitionStragety.split(":");
                    tableMap.put(split[0], split[1]);
                }
                partitionMap.put("table", tableMap);
                partitionMap.put("database", tablePartition.databaseType());
            }
            //索引字段
            Map<String, Map<String, String>> indexColumnMap = new HashMap<String, Map<String, String>>();
            List<String> indexFields = new ArrayList<>();
            TableIndex tableIndex = object.getClass().getAnnotation(TableIndex.class);
            if (tableIndex != null && tableIndex.indexing()) {
                String[] indexStragetys = tableIndex.indexStragety();
                if (indexStragetys.length > 0) {
                    for (String indexStragety : indexStragetys) {
                        String[] split = indexStragety.split(":");
                        indexFields.addAll(Arrays.asList(split[0].split(",")));
                        Map<String, Object> indexMap = new HashMap<>();
                        indexMap.put("fields", split[0]);
                        indexMap.put("type", split[1]);
                        indexings.add(indexMap);
                    }
                } else {
                    indexFields.addAll(Arrays.asList(tableIndex.indexFields()));
                    Map<String, Object> indexMap = new HashMap<>();
                    indexMap.put("fields", StringUtils.join(tableIndex.indexFields(), ","));
                    indexMap.put("type", tableIndex.indexType());
                    indexings.add(indexMap);
                }
            }

            //表属性
            TableProperties tableProperties = object.getClass().getAnnotation(TableProperties.class);
            if (tableProperties != null) {
                propertiesMap.put("storage", tableProperties.storage());
                propertiesMap.put("inputFormat", tableProperties.inputFormat());
                propertiesMap.put("outputFormat", tableProperties.outputFormat());
                propertiesMap.put("location", tableProperties.location());
                propertiesMap.put("serde", tableProperties.serde());
                propertiesMap.put("properties", tableProperties.properties());
            }

            //判断对象是否添加TableField注解，否则添加字段信息
            Field[] tableFields = FieldUtils.getFieldsWithAnnotation(object.getClass(), TableField.class);
            if (tableFields == null || tableFields.length == 0) {
                Field[] allFields = FieldUtils.getAllFields(object.getClass());
                for (Field field : allFields) {
                    boolean commonField = true;
                    field.setAccessible(true);
                    String columnName = CamelCaseUtils.toUnderlineName(field.getName());
                    Map<String, String> columnMap = new HashMap<>();
                    columnMap.put("name", columnName);
                    columnMap.put("type", field.getType().getSimpleName());
                    columnMap.put("comment", "");
                    //分区字段
                    boolean partition = partitionFields.contains(columnName);
                    columnMap.put("partition", String.valueOf(partition));
                    if (partition) {
                        partitionColumnMap.put(columnName, columnMap);
                        commonField = false;
                    }
                    //索引字段
                    boolean indexing = indexFields.contains(columnName);
                    columnMap.put("indexing", String.valueOf(indexing));
                    if (indexing) {
                        indexColumnMap.put(columnName, columnMap);
                        commonField = false;
                    }
                    //普通字段
                    if (commonField) {
                        columns.add(columnMap);
                    }
                }
            } else {
                for (Field field : tableFields) {
                    boolean commonField = true;
                    field.setAccessible(true);
                    TableField annotation = field.getAnnotation(TableField.class);
                    String columnName = annotation.name();
                    Map<String, String> columnMap = new HashMap<>();
                    columnMap.put("name", columnName);
                    columnMap.put("type", annotation.type());
                    columnMap.put("comment", annotation.comment());
                    columnMap.put("format", annotation.format());
                    //分区
                    boolean partition = partitionFields.contains(columnName);
                    columnMap.put("partition", String.valueOf(partition));
                    if (partition) {
                        partitionColumnMap.put(columnName, columnMap);
                        commonField = false;
                    }
                    //索引
                    boolean indexing = indexFields.contains(columnName);
                    columnMap.put("indexing", String.valueOf(indexing));
                    if (indexing) {
                        indexColumnMap.put(columnName, columnMap);
                        commonField = false;
                    }
                    //普通字段
                    if (commonField) {
                        columns.add(columnMap);
                    }
                }
            }
            List<Map<String, String>> objects = new ArrayList<>();
            //分区 索引
            indexFields.forEach(indexField -> objects.add(indexColumnMap.get(indexField)));
            partitionFields.forEach(partitionField -> objects.add(partitionColumnMap.get(partitionField)));
            objects.addAll(columns);
            columns.clear();
            columns.addAll(objects);
        }
        tableInfoMap.put("columns", columns);
        tableInfoMap.put("partition", partitionMap);
        tableInfoMap.put("indexing", indexings);
        tableInfoMap.put("properties", propertiesMap);
        return tableInfoMap;
    }

    /**
     * 获取对象字段值
     *
     * @param object  对象信息
     * @param columns 对象字段集合
     * @return
     */
    public static Map<String, Object> getCloumnValues(Object object, List<Map<String, String>> columns) {
        Map<String, Object> columnValueMap = new TreeMap<>();
        if (columns == null) columns = getTableFields(object);
        if (object == null) return columnValueMap;
        try {
            if (object instanceof Map) {
                columns.forEach(columnMap -> {
                    String name = columnMap.get("name");
                    columnValueMap.put(name, MapFieldUtil.getMapField((Map<String, Object>) object, name));
                });
            } else {
                Field[] tableFields = FieldUtils.getFieldsWithAnnotation(object.getClass(), TableField.class);
                if (tableFields == null || tableFields.length == 0) {
                    Field[] fields = FieldUtils.getAllFields(object.getClass());
                    columns.forEach(columnMap -> {
                        try {
                            for (Field field : fields) {
                                field.setAccessible(true);
                                String underlineName = CamelCaseUtils.toUnderlineName(field.getName());
                                if (underlineName.equalsIgnoreCase(columnMap.get("name"))) {
                                    //将字段值由驼峰式转换为下划线风格字符
                                    columnValueMap.put(underlineName, field.get(object));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error(e.getLocalizedMessage(), e);
                        }
                    });
                } else {
                    columns.forEach(columnMap -> {
                        try {
                            if (columnMap != null) {
                                for (Field field : tableFields) {
                                    field.setAccessible(true);
                                    TableField annotation = field.getAnnotation(TableField.class);
                                    if (annotation != null && annotation.name().equalsIgnoreCase(columnMap.getOrDefault("name", ""))) {
                                        columnValueMap.put(annotation.name(), field.get(object));
                                    }
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error(e.getLocalizedMessage(), e);
                        }
                    });
                }
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
        return columnValueMap;
    }

    /**
     * 获取字段分区信息
     *
     * @param object
     * @return
     */
    public static Map<String, Object> getTablePartitionInfo(Object object) {
        Map<String, Object> partitionMap = new HashMap<>();
        if (object == null) return partitionMap;

        List<String> partitionFields = new ArrayList<>();
        TablePartition tablePartition = object.getClass().getAnnotation(TablePartition.class);
        if (tablePartition != null && tablePartition.partition()) {
            partitionMap.put("type", tablePartition.partitionType());
            partitionMap.put("fields", StringUtils.join(tablePartition.partitionFields(), ","));
            Map<String, Object> tableMap = new HashMap<>();
            String[] partitionStragetys = tablePartition.partitionStragety();
            for (String partitionStragety : partitionStragetys) {
                String[] split = partitionStragety.split(":");
                tableMap.put(split[0], split[1]);
            }
            partitionMap.put("table", tableMap);
        }
        return partitionMap;
    }

    /**
     * 获取字段索引信息
     *
     * @param object
     * @return
     */
    public static List<Map<String, Object>> getTableIndexInfo(Object object) {
        List<Map<String, Object>> indexings = new ArrayList<>();
        if (object == null) return indexings;

        TableIndex tableIndex = object.getClass().getAnnotation(TableIndex.class);
        if (tableIndex != null && tableIndex.indexing()) {
            String[] indexStragetys = tableIndex.indexStragety();
            if (indexStragetys.length > 0) {
                for (String indexStragety : indexStragetys) {
                    String[] split = indexStragety.split(":");
                    Map<String, Object> indexMap = new HashMap<>();
                    indexMap.put("fields", split[0]);
                    indexMap.put("type", split[1]);
                    indexings.add(indexMap);
                }
            } else {
                Map<String, Object> indexMap = new HashMap<>();
                indexMap.put("fields", StringUtils.join(tableIndex.indexFields(), ","));
                indexMap.put("type", tableIndex.indexType());
                indexings.add(indexMap);
            }
        }
        return indexings;
    }

    /**
     * 获取字段信息
     *
     * @param object
     * @return
     */
    public static List<Map<String, String>> getTableFields(Object object) {
        List<Map<String, String>> columns = new ArrayList<>();
        if (object == null) return columns;

        if (object instanceof Map) {
            List<Map<String, Object>> flatMapKeys = MapFieldUtil.getFlatMapKeys((Map<String, Object>) object);
            flatMapKeys.forEach(map -> {
                Map<String, String> columnMap = new HashMap<>();
                columnMap.put("name", map.get("name").toString());
                columnMap.put("type", map.get("type").toString());
                columnMap.put("comment", "");
                columnMap.put("partition", "false");
                columnMap.put("indexing", "false");
                columns.add(columnMap);
            });
        } else if (object instanceof List) {
            columns.addAll((List) object);
        } else {
            Map<String, Map<String, String>> partitionColumnMap = new HashMap<String, Map<String, String>>();
            Map<String, Map<String, String>> indexColumnMap = new HashMap<String, Map<String, String>>();
            //获取索引字段
            List<Map<String, Object>> tableIndexInfo = getTableIndexInfo(object);
            List<String> indexFields = new ArrayList<>();
            tableIndexInfo.forEach(map -> {
                for (String indexField : map.get("fields").toString().split(",")) {
                    indexFields.add(indexField);
                }
            });
            //获取分区字段
            List<String> partitionFields = new ArrayList<>();
            String partition = MapUtils.getString(getTablePartitionInfo(object), "fields", "");
            if (StringUtils.isNotEmpty(partition)) {
                partitionFields = Arrays.asList(partition.split(","));
            }
            Field[] tableFields = FieldUtils.getFieldsWithAnnotation(object.getClass(), TableField.class);
            if (tableFields == null || tableFields.length == 0) {
                Field[] allFields = FieldUtils.getAllFields(object.getClass());
                for (Field field : allFields) {
                    boolean commonField = true;
                    field.setAccessible(true);
                    String columnName = CamelCaseUtils.toUnderlineName(field.getName());
                    Map<String, String> columnMap = new HashMap<>();
                    columnMap.put("name", columnName);
                    columnMap.put("type", field.getType().getSimpleName());
                    columnMap.put("comment", "");
                    columnMap.put("partition", "false");
                    columnMap.put("indexing", "false");
                    //分区字段
                    if (partitionFields.contains(columnName)) {
                        commonField = false;
                        columnMap.put("partition", "true");
                        partitionColumnMap.put(columnName, columnMap);
                    }
                    //索引字段
                    if (indexFields.contains(columnName)) {
                        commonField = false;
                        columnMap.put("indexing", "true");
                        indexColumnMap.put(columnName, columnMap);
                    }
                    //普通字段
                    if (commonField) {
                        columns.add(columnMap);
                    }
                }
            } else {
                for (Field field : tableFields) {
                    boolean commonField = true;
                    field.setAccessible(true);
                    TableField annotation = field.getAnnotation(TableField.class);
                    String columnName = annotation.name();
                    Map<String, String> columnMap = new HashMap<>();
                    columnMap.put("name", columnName);
                    columnMap.put("type", annotation.type());
                    columnMap.put("comment", annotation.comment());
                    columnMap.put("partition", "false");
                    columnMap.put("indexing", "false");
                    //分区字段
                    if (partitionFields.contains(columnName)) {
                        commonField = false;
                        columnMap.put("partition", "true");
                        partitionColumnMap.put(columnName, columnMap);
                    }
                    //索引字段
                    if (indexFields.contains(columnName)) {
                        commonField = false;
                        columnMap.put("indexing", "true");
                        indexColumnMap.put(columnName, columnMap);
                    }
                    //普通字段
                    if (commonField) {
                        columns.add(columnMap);
                    }
                }
            }
            List<Map<String, String>> objects = new ArrayList<>();
            //分区 索引
            indexFields.forEach(indexField -> objects.add(indexColumnMap.get(indexField)));
            partitionFields.forEach(partitionField -> objects.add(partitionColumnMap.get(partitionField)));
            objects.addAll(columns);
            columns.clear();
            columns.addAll(objects);
        }
        return columns;
    }

    /**
     * 获取对象字段值
     *
     * @param object  对象信息
     * @param columns 对象字段集合
     * @return
     */
    public static List<Object> getValues(Object object, List<Map<String, String>> columns) {
        List<Object> values = new ArrayList<>();
        if (columns == null) columns = getTableFields(object);
        if (object == null) return values;
        try {
            if (object instanceof Map) {
                columns.forEach(columnMap -> {
                    values.add(MapFieldUtil.getMapField((Map<String, Object>) object, columnMap.get("name"), ""));
                });
            } else {
                Field[] tableFields = FieldUtils.getFieldsWithAnnotation(object.getClass(), TableField.class);
                if (tableFields == null || tableFields.length == 0) {
                    Field[] fields = FieldUtils.getAllFields(object.getClass());
                    columns.forEach(columnMap -> {
                        try {
                            for (Field field : fields) {
                                field.setAccessible(true);
                                if (field.getName().equalsIgnoreCase(columnMap.get("name"))) {
                                    values.add(field.get(object));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error(e.getLocalizedMessage(), e);
                        }
                    });
                } else {
                    columns.forEach(columnMap -> {
                        try {
                            for (Field field : tableFields) {
                                field.setAccessible(true);
                                TableField annotation = field.getAnnotation(TableField.class);
                                if (annotation.name().equalsIgnoreCase(columnMap.get("name"))) {
                                    values.add(field.get(object));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error(e.getLocalizedMessage(), e);
                        }
                    });
                }
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
        return values;
    }
}
