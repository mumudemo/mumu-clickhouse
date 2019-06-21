package com.lovecws.mumu.clickhouse.jdbc;

import com.lovecws.mumu.clickhouse.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

/**
 * @program: act-able
 * @description: 基础jdbc操作
 * @author: 甘亮
 * @create: 2019-05-24 17:42
 **/
public class BasicJdbcService extends AbstractJdbcService {

    public BasicJdbcService(JdbcConfig jdbcConfig) {
        super(jdbcConfig);
    }

    private static final Logger log = Logger.getLogger(AbstractJdbcService.class);

    /**
     * 获取到实体字段
     *
     * @param table     表
     * @param tableInfo 表字段信息
     * @return
     */
    @Override
    public String getCreateTableSql(String table, Map<String, Object> tableInfo) {
        String jdbcType = jdbcConfig.getJdbcType();
        List<Map<String, String>> columns = (List<Map<String, String>>) tableInfo.get("columns");
        StringBuilder columnBuilder = new StringBuilder();
        columns.forEach(columnMap -> {
            String type = columnMap.get("type");
            if (StringUtils.isEmpty(type)) {
                if ("clickhouse".equalsIgnoreCase(jdbcType)) {
                    type = "String";
                } else if ("hive2".equalsIgnoreCase(jdbcType)) {
                    type = "string";
                } else if ("postgresql".equalsIgnoreCase(jdbcType)) {
                    type = "text";
                }
            }
            columnBuilder.append(columnMap.get("name") + " " + type + ",");
        });
        if (columnBuilder.toString().endsWith(",")) columnBuilder.deleteCharAt(columnBuilder.length() - 1);

        String tableSQL = "CREATE TABLE IF NOT EXISTS " + table + " ( " + columnBuilder.toString() + ")";
        if ("clickhouse".equalsIgnoreCase(jdbcType)) {
            //MergeTree(FlightDate, (Year, FlightDate), 8192)
            tableSQL = tableSQL + " engine MergeTree(date,(id,name),8192)";
        }
        return tableSQL;
    }


    /**
     * 批量插入数据
     *
     * @param table      表
     * @param columns    字段名称
     * @param eventDatas 事件集合
     * @return
     */
    @Override
    public boolean batchInsertInto(String table, List<Map<String, String>> columns, List<Map<String, Object>> eventDatas) {
        StringBuilder columnBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();
        Iterator<Map<String, String>> iterator = columns.iterator();
        List<String> columnNames = new ArrayList<>();
        while (iterator.hasNext()) {
            Map<String, String> columnMap = iterator.next();
            String columnName = columnMap.get("name");
            columnBuffer.append(columnName);
            columnNames.add(columnName);
            valueBuffer.append("?");
            if (iterator.hasNext()) {
                columnBuffer.append(",");
                valueBuffer.append(",");
            }
        }
        String bulkSql = "INSERT INTO " + table + " (" + columnBuffer.toString() + ") values (" + valueBuffer.toString() + ")";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = jdbcConfig.getConnection();
            preparedStatement = connection.prepareStatement(bulkSql);
            for (Map<String, Object> eventDataMap : eventDatas) {
                for (int i = 0; i < columnNames.size(); i++) {
                    Object o = eventDataMap.get(columnNames.get(i));

                    if (o instanceof Date) {
                        preparedStatement.setTimestamp(i + 1, new Timestamp(((Date) o).getTime()));
                    } else {
                        if (o == null) {
                            o = "";
                        }
                        preparedStatement.setObject(i + 1, o);
                    }
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            return true;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /**
     * 批量插入数据
     *
     * @param table     表
     * @param columns   字段列表
     * @param eventData 数据集
     * @return
     */
    @Override
    public boolean insertInto(String table, List<Map<String, String>> columns, Map<String, Object> eventData) {
        StringBuilder columnBuffer = new StringBuilder();
        StringBuilder valueBuffer = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Map<String, String> columnMap = columns.get(i);
            String columnName = columnMap.get("name");
            Object value = eventData.get(columnName);
            if (value == null) continue;

            columnBuffer.append(columnName);
            if (value instanceof String) {
                valueBuffer.append("\'" + value + "\'");
            } else if (value instanceof Date) {
                //valueBuffer.append("\'" + ((Date) value).getTime() + "\'");
                valueBuffer.append("\'" + DateUtils.formatDate((Date) value, "yyyy-MM-dd HH:mm:ss") + "\'");
            } else {
                valueBuffer.append(value);
            }
            columnBuffer.append(",");
            valueBuffer.append(",");
        }
        if (columnBuffer.toString().endsWith(",")) {
            columnBuffer.deleteCharAt(columnBuffer.lastIndexOf(","));
        }
        if (valueBuffer.toString().endsWith(",")) {
            valueBuffer.deleteCharAt(valueBuffer.lastIndexOf(","));
        }
        String bulkSql = "INSERT INTO " + table + " (" + columnBuffer.toString() + ") values (" + valueBuffer.toString() + ")";
        log.info(bulkSql);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = jdbcConfig.getConnection();
            statement = connection.createStatement();
            return statement.execute(bulkSql);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    @Override
    public boolean upset(String table, List<Map<String, String>> columns, Map<String, Object> eventData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean batchUpset(String table, Map<String, Object> tableInfoMap, List<Map<String, Object>> eventDatas) {
        throw new UnsupportedOperationException();
    }
}
