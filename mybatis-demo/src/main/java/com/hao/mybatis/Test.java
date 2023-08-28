package com.hao.mybatis;

import com.hao.mybatis.mapper.UserMapper;
import com.hao.mybatis.po.User;
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

        List<User> users = userMapper.queryAllUser();

        users.forEach(System.out::println);


    }
}
