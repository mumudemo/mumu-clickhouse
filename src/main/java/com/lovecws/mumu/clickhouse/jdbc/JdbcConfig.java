package com.lovecws.mumu.clickhouse.jdbc;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.sql.Connection;

/**
 * jdbc 数据源配置信息
 */
public class JdbcConfig {

    private static final Logger logger = Logger.getLogger(JdbcConfig.class);

    private javax.sql.DataSource dataSource;
    private String jdbcType;

    public JdbcConfig(String url, String driver, String user, String password) {
        dataSource = getDruidDataSource(url, driver, user, password);
        logger.debug("DataSource Inject Successfully...");
    }

    /**
     * 获取tomcat数据连接池
     *
     * @param url      url
     * @param driver   驱动
     * @param user     用户名
     * @param password 密码
     * @return
     */
    private DataSource getTomcatDataSource(String url, String driver, String user, String password) {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(url);
        setJdbcUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        logger.debug("DataSource Inject Successfully...");

        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestOnBorrow(false);
        dataSource.setValidationQuery("select 1");
        return dataSource;
    }

    /**
     * 获取druid连接池
     *
     * @param url      url
     * @param driver   驱动
     * @param user     用户名
     * @param password 密码
     * @return
     */
    private DruidDataSource getDruidDataSource(String url, String driver, String user, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        setJdbcUrl(url);
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);

        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("select 1");
        return druidDataSource;
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    public void setJdbcUrl(String url) {
        if (url == null) return;
        String[] split = url.split(":");
        if (split.length >= 2) {
            jdbcType = split[1];
        }
    }

    public String getJdbcType() {
        return jdbcType == null ? "" : jdbcType;
    }
}