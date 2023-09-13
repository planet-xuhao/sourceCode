package com.hao.mybatis;

import com.hao.mybatis.mapper.UserMapper;
import com.hao.mybatis.po.User;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author xuh
 * @date 2023/8/19
 */
public class Test {

    public static void main(String[] args) throws IOException {
        // 读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");

        // 构建SqlSessionFactory（框架初始化） DefaultSqlSessionFactory，该类持有Configuration的引用
        // 1.将读取的mybatis配置文件的流使用XPathParser进行读取，得到一个XMLConfigBuilder对象
        // 2.XMLConfigBuilder对象持有了XPathParser对象，该对象通过了jdk自带的xml解析方式，将流解析为了一个Document
        // 在解析过程中，因为要对xml格式做校验，使用了XMLMapperEntityResolver，可在离线环境下对xml做校验
        // 3.通过XMLConfigBuilder的parse()方法，得到最终的Configuration对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 打开SqlSession，持有Configuration和Executor对象
        // 1.从Configuration中获得environment信息
        // 2.根据environment信息获得事务工厂 TransactionFactory
        // 3.根据environment创建事务对象transaction
        // 4.根据configuration和transaction创建执行器Executor对象
        // 5.判断是否有插件，加载插件，比如分页的插件，这里会用动态代理
        // 6.根据configuration和Executor对象创建一个SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获得Mapper接口对象（底层是动态代理），此时的UserMapper是一个代理对象
        // 1.从knownMappers中通过UserMapper.class作为key，获得MapperProxyFactory对象
        // 2.knownMappers在解析配置文件时就已将knownMappers设置，key为接口，value为MapperProxyFactory<T>,org.apache.ibatis.builder.xml.XMLConfigBuilder.mapperElement
        // 3.调用MapperProxyFactory.newInstance使用jdk的动态代理得到UserMapper的代理对象
        // 4.调用UserMapper的所有方法都会先调用MapperProxy的invoke方法
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 项目启动时会读取mybatis.xml配置文件，会依次解析xml节点，当解析到<mapper>节点时org.apache.ibatis.builder.xml.XMLConfigBuilder.mapperElement，会做如下动作：
        // 1.读取到<mapper>节点时，判断是用包扫描的方式还是直接指定的xml文件路径
        // 2.当找到类路径后，调用XMLMapperBuilder的parse方法org.apache.ibatis.builder.xml.XMLMapperBuilder.parse，来解析具体的xxxMapper.xml
        // 3.解析mapper.xml中的各个节点，这里重点分析下<select>/<insert>/<update>/<delete>
        // 4.调用statementParser.parseStatementNode()，解析每一个sql节点（select,insert这种）
        // 5.得到解析sql节点中的所有属性后，调用builderAssistant.addMappedStatement
        // 6.使用建造者模式将这些属性封装到一个MappedStatement中，并把这个MappedStatement放到configuration的mappedStatements
        // 7.当第一次调用代理类的方法时，会new一个MapperMethod，该类有两个属性SqlCommand和MethodSignature，对于SqlCommand类，它是一个内部类，只要存储了sql，也就是mapperStatement的id和类型
        //   对于MethodSignature它其中的属性表示了这个方法返回的类型，参数的个数等等，也就是方法对象的描述
        // 8.创建完MapperMethod后会调用execute方法，参数是sqlSession和执行参数，根据第7步获取的command判断需要执行的操作是什么select，还是update等等，
        //   这里是select且返回的数目是多条，所以会执行executeForMany方法
        // 10.随后判断是否有指定分页信息，如果没有则查0 - 2147483647条的数据，最终会调用到defaultSqlSession的selectList方法
        // 11.在defaultSqlSession中通过接口方法名拿到对应的xml对象信息，也就是MappedStatement，随后专用执行器来查询结果CachingExecutor.query
        // 12.通过MappedStatement得到boundSql对象，这里有待执行的sql信息，且有占位符，如果你开启了二级缓存，随后就是一些二级缓存的操作了
        //    如果没有则直接调用BaseExecutor.query方法，获得单例的错误输出对象ErrorContext，再看缓存中是否有结果，有就处理，没有就去数据库查queryFromDatabase
        // 13.这里就开始真正的查数据库了，就关联到了sqlSessionFactory.openSession()中设置Executor时设置的是什么，批量执行还是简单的执行，不设置就是SimpleExecutor
        // 14.通过configuration.newStatementHandler得到一个StatementHandler对象
        //    new RoutingStatementHandler根据<select>标签上的statementType属性选择相应的处理器，大部分情况下该属性都不会赋值，那么此时的处理器将会是PreparedStatementHandler
        //    随后调用prepareStatement，得到预编译的sql语句，在这里面，需要使用类似与jdbc的方式来执行sql，这里mybatis做了一层封装。获得Statement对象：
        //    使用openConnection获得一个connection连接，mybatis中的连接会使用PooledDataSource作为一个数据连接池，从其中进行获取。org.apache.ibatis.datasource.pooled.PooledDataSource.getConnection(),真正获取连接的地方org.apache.ibatis.datasource.unpooled.UnpooledDataSource.doGetConnection(java.lang.String, java.lang.String)
        //    判断statementMapper是否开启了debug级别的日志，如果开启了则会使用代理模式获得一个被代理过的连接对象，以便用来打印日志org.apache.ibatis.logging.jdbc.ConnectionLogger.invoke
        // 15.然后调用RoutingStatementHandler.prepare得到一个JDBC预编译的statement
        //    org.apache.ibatis.executor.statement.BaseStatementHandler.prepare模板方法模式得到statement，最终会执行connection.prepareStatement(sql);这也就是JDBC中获得statemen的步骤，此时的sql还是占位符形式
        // 16.随后调用调用StatementHandler.parameterize设置预编译statement中的参数，用statement.setInt()这种方法，先判断参数的类型，选择相应的类型处理器，不同的类型set方法也不一样
        //    比如int是ps.setInt, String是ps.setString，参数设置完后最终就是调用statement的execute()或executeQuery()。org.apache.ibatis.executor.statement.PreparedStatementHandler.query
        //    如果你在mybatis中开启了日志，此时的statement会是一个代理的对象PreparedStatementLogger，这里会打印sel执行时的参数日志，就像Parameters：xxxxxx
        // 17.调用stmt.getResultSet()并包装到一个ResultSetWrapper，得到结果集
        List<User> users = userMapper.queryAllUser();

        users.forEach(System.out::println);


    }
}
