package com.hao.mybatis.plugin;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author xuh
 * @date 2023/9/17
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceType().name();
    }
}
