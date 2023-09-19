package com.hao.mybatis.plugin;

/**
 * @author xuh
 * @date 2023/9/17
 */
public class DataSourceHolder {

    public static final ThreadLocal<DataSourceType> holder = new ThreadLocal<DataSourceType>();

    /**
     * 获取数据类型
     */
    public static DataSourceType getDataSourceType() {
        DataSourceType dataSourceType = holder.get();
        if (dataSourceType == null) {
            dataSourceType = DataSourceType.WRITE;
        }
        return dataSourceType;
    }

    /**
     * 设置数据源
     */
    public static void setDataSourceType(DataSourceType dataSourceType) {
        holder.set(dataSourceType);
    }
}
