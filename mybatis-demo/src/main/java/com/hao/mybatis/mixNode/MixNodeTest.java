package com.hao.mybatis.mixNode;

import com.alibaba.fastjson.JSON;
import com.hao.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xuh
 * @date 2024/3/10
 */


public class MixNodeTest {

    SqlSession sqlSession;

    @Before
    public void before() throws IOException {
        InputStream resource = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resource);
        sqlSession = sessionFactory.openSession();
    }

    @Test
    public void 美元Sql() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.queryUserByName("张三");
    }

    @Test
    public void 井Sql() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.queryUserById(1);
    }

    @Test
    public void 动态Sql() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.queryUserByIdOnCondition(1);
    }

    @Test
    public void resultMapSql() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.getUserNameById(1);
    }

    @Test
    public void 延迟加载test() {
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(JSON.toJSONString(userMapper.queryUserByUserId(1)));
    }
}















