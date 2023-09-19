package com.hao.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * 对于mybatis的插件原理，其实就是mybatis源码中在ParameterHandler，ResultSetHandler，StatementHandler，Executor中调用了pluginAll方法
 * 这个方法最终会调用interceptor.plugin(target)得到一个代理对象，使用jdk代理
 * 在这个方法中我们一般都是调用Plugin.wrap，该方法会读取插件上的注解信息，得到要改变哪个类，哪些接口，然后产生代理对象
 * Proxy.newProxyInstance(
 * type.getClassLoader(),
 * interfaces,
 * new Plugin(target, interceptor, signatureMap));
 * <p>
 * <p>
 * 实现一个读写分离插件
 *
 * @author xuh
 * @date 2023/9/17
 */
@Intercepts({
        @Signature(type = Executor.class,
                method = "update",
                args = {
                        MappedStatement.class, Object.class
                }),
        @Signature(type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
                }),
        @Signature(type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
                })
})
public class ReadWritePlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取目标方法的执行参数
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];

        DataSourceType dataSourceType = null;
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            // selectKey为自增id查询主键，使用主库
            if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                dataSourceType = DataSourceType.WRITE;
            } else {
                dataSourceType = DataSourceType.READ;
            }
        } else {
            // 删，改，查
            dataSourceType = DataSourceType.WRITE;
        }

        // 修改当前线程要选择的数据源
        DataSourceHolder.setDataSourceType(dataSourceType);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
