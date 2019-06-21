package com.lovecws.mumu.clickhouse.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MapFieldUtil {

    /**
     * 根据字段的名称获取字段的值
     *
     * @param fieldName 字段名称 可以获取嵌套字段
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getMapFieldValue(Map _sourceMap, String fieldName, String defaultVal) {
        Object mapField = getMapField(_sourceMap, fieldName, defaultVal);
        if (mapField == null) mapField = "";
        return String.valueOf(mapField);
    }

    /**
     * 根据字段的名称获取字段的值
     *
     * @param fieldName 字段名称 可以获取嵌套字段
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object getMapField(Map _sourceMap, String fieldName) {
        Object mapField = getMapField(_sourceMap, fieldName, "");
        if (mapField == null) mapField = "";
        return mapField;
    }

    /**
     * 根据字段的名称获取字段的值
     *
     * @param fieldName 字段名称 可以获取嵌套字段
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object getMapField(Map _sourceMap, String fieldName, Object defaultVal) {
        if (fieldName == null || "".equals(fieldName)) {
            return defaultVal;
        }
        String[] fieldNames = fieldName.split("\\.");
        Map map = _sourceMap;
        for (int i = 0; i < fieldNames.length; i++) {
            Object object = map.get(fieldNames[i]);
            if (object == null) {
                return defaultVal;
            }
            if (i == fieldNames.length - 1) {
                return object;
            } else {
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    if (jsonArray.size() > 0) {
                        object = jsonArray.get(0);
                    } else {
                        object = new HashMap<>();
                    }
                }
                map = (Map) object;
            }
        }
        return defaultVal;
    }

    /**
     * 设置map字段值
     *
     * @param _sourceMap 源map
     * @param fieldName  字段
     * @param fieldValue 值
     * @return
     */
    public static Map<String, Object> setMapField(Map<String, Object> _sourceMap, String fieldName, Object fieldValue) {
        if (fieldName == null || "".equals(fieldName) || fieldValue == null) {
            return new HashMap<String, Object>();
        }
        if (_sourceMap == null) _sourceMap = new HashMap<String, Object>();
        String[] fieldNames = fieldName.split("\\.");
        Map<String, Object> map = _sourceMap;
        for (int i = 0; i < fieldNames.length; i++) {
            String currentFieldName = fieldNames[i];
            Object object = map.get(currentFieldName);
            if (object == null) {
                if (i == fieldNames.length - 1) {
                    map.put(currentFieldName, fieldValue);
                    return _sourceMap;
                } else {
                    object = new HashMap<>();
                    map.put(currentFieldName, object);
                }
            }
            if (object instanceof Map) {
                Map<String, Object> newmap = (Map<String, Object>) object;
                if (i == fieldNames.length - 1) {
                    newmap.put(currentFieldName, fieldValue);
                } else {
                    map = newmap;
                }
            } else {
                map.put(currentFieldName, fieldValue);
                return _sourceMap;
            }
        }
        return _sourceMap;
    }

    /**
     * 從map中获取到key 原始map 可以嵌套map
     *
     * @param sourceMap
     * @return
     */
    public static List<Map<String, Object>> getFlatMapKeys(Map<String, Object> sourceMap) {
        return getFlatMapKeys(sourceMap, null, null);
    }

    /**
     * 從map中获取到key，深层map通过`.`来分割
     *
     * @param sourceMap 原始map 可以嵌套map
     * @param flatMap   保存的 name type结果集
     * @param prefix    key前缀
     * @return
     */
    public static List<Map<String, Object>> getFlatMapKeys(Map<String, Object> sourceMap, List<Map<String, Object>> flatMap, String prefix) {
        if (sourceMap == null) return new ArrayList<>();
        if (flatMap == null) flatMap = new ArrayList<>();
        if (prefix == null) prefix = "";

        Set<String> keys = sourceMap.keySet();
        for (String key : keys) {
            Object o = sourceMap.get(key);
            if (o == null) continue;
            if (o instanceof Map) {
                getFlatMapKeys((Map<String, Object>) o, flatMap, key);
            } else {
                Map<String, Object> keyMap = new HashMap<>();
                String name = key;
                if (StringUtils.isNotEmpty(prefix)) {
                    name = prefix + "." + key;
                }
                keyMap.put("name", name);
                keyMap.put("type", o.getClass().getSimpleName().toLowerCase());
                flatMap.add(keyMap);
            }
        }
        return flatMap;
    }

    /**
     * 获取对象字段值
     *
     * @param object  对象信息
     * @param columns 对象字段集合
     * @return
     */
    public static List<Object> getFlatMapValues(Map<String, Object> object, List<String> columns) {
        List<Object> columnValues = new ArrayList<Object>();
        if (columns == null) columns = new ArrayList<>();
        if (object == null) return columnValues;
        try {
            columns.forEach(columnName -> columnValues.add(MapFieldUtil.getMapField(object, columnName)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return columnValues;
    }

    public static void main(String[] args) {
        Map<String, Object> _sourceMap = new HashMap<>();
        _sourceMap.put("ipoper1", "xlxl");
        _sourceMap.put("zzz", 1112);
        Map map = setMapField(_sourceMap, "ipoper.province_name", "1234");
        System.out.println(JSON.toJSONString(map));

        List flatMapKeys = getFlatMapKeys(map, null, null);
        System.out.println(JSON.toJSONString(flatMapKeys));
    }
}
