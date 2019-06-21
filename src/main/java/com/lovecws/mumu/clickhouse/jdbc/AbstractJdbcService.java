package com.lovecws.mumu.clickhouse.jdbc;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @program: act-able
 * @description: 抽象jdbc服务
 * @author: 甘亮
 * @create: 2019-05-29 14:23
 **/
public abstract class AbstractJdbcService implements Serializable {

    public JdbcConfig jdbcConfig;

    public AbstractJdbcService(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    private static final Logger log = Logger.getLogger(AbstractJdbcService.class);

    /**
     * 判断表是否存在
     *
     * @param tableName 表名称
     * @return
     */
    public boolean tableExists(String tableName) {
        Connection connection = jdbcConfig.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getTables(null, null, tableName, null);
            return rs.next();
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            try {
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    /**
     * 创建表
     *
     * @param tableSql 建表语句
     * @return
     */
    public boolean createTable(String tableSql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = jdbcConfig.getConnection();
            log.info(new String(tableSql.getBytes("ISO-8859-1"), "UTF-8"));
            statement = connection.createStatement();
            return statement.execute(tableSql);
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

    /**
     * 获取创建表sql
     *
     * @param table     表名称
     * @param tableInfo 表信息
     * @return
     */
    public abstract String getCreateTableSql(String table, Map<String, Object> tableInfo);

    /**
     * 批量插入数据
     *
     * @param table
     * @param columns
     * @param eventDatas
     * @return
     */
    public abstract boolean batchInsertInto(String table, List<Map<String, String>> columns, List<Map<String, Object>> eventDatas);

    /**
     * 数据插入
     *
     * @param table
     * @param columns
     * @param eventData
     * @return
     */
    public abstract boolean insertInto(String table, List<Map<String, String>> columns, Map<String, Object> eventData);


    /**
     * 更新或者插入
     *
     * @param table
     * @param columns
     * @param eventData
     * @return
     */
    public abstract boolean upset(String table, List<Map<String, String>> columns, Map<String, Object> eventData);

    /**
     * 批量更新或者插入
     *
     * @param table
     * @param tableInfoMap
     * @param eventDatas
     * @return
     */
    public abstract boolean batchUpset(String table, Map<String, Object> tableInfoMap, List<Map<String, Object>> eventDatas);

}
