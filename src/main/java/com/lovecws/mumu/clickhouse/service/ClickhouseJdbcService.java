package com.lovecws.mumu.clickhouse.service;

import com.lovecws.mumu.clickhouse.jdbc.AbstractJdbcService;
import com.lovecws.mumu.clickhouse.jdbc.BasicJdbcService;
import com.lovecws.mumu.clickhouse.jdbc.JdbcConfig;
import com.lovecws.mumu.clickhouse.model.ClickhouseModel;
import com.lovecws.mumu.clickhouse.util.TableUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ClickhouseJdbcService {

    public void clickHouse() {
        JdbcConfig jdbcConfig = new JdbcConfig("jdbc:clickhouse://192.168.0.25:8123/industry", "ru.yandex.clickhouse.ClickHouseDriver", "root", "123456");
        AbstractJdbcService jdbcService = new BasicJdbcService(jdbcConfig);

        Map<String, Object> tableInfo = TableUtil.getTableInfo(new ClickhouseModel());
        List<Map<String, String>> tableFields = TableUtil.getTableFields(new ClickhouseModel());

        String tableSql = jdbcService.getCreateTableSql("t_ods_mumu_clickhouse_mt", tableInfo);
        jdbcService.createTable(tableSql);

        for (int j = 0; j < 1; j++) {
            List<Map<String, Object>> datas = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                ClickhouseModel clickhouseModel = new ClickhouseModel();
                clickhouseModel.setId(i);
                clickhouseModel.setName("cws" + i);
                clickhouseModel.setPassword("lovecws" + i);
                clickhouseModel.setCreateTime(new Timestamp(new Date().getTime()));
                clickhouseModel.setDate(new java.sql.Date(new Date().getTime()));
                Map<String, Object> cloumnValues = TableUtil.getCloumnValues(clickhouseModel, tableFields);
                cloumnValues.put("date","2019-06-20");
                datas.add(cloumnValues);
            }
            jdbcService.batchInsertInto("t_ods_mumu_clickhouse_mt", tableFields, datas);
        }
    }

    public static void main(String[] args) {
        new ClickhouseJdbcService().clickHouse();
    }
}
