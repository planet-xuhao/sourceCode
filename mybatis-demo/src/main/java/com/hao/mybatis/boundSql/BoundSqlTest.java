package com.hao.mybatis.boundSql;

import com.hao.mybatis.po.User;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author xuh
 * @date 2024/3/9
 */
public class BoundSqlTest {

    @Test
    public void test() {
        User user = new User();
        user.setId(1);
        user.setName("张三");

        Configuration configuration = new Configuration();
        DynamicContext context = new DynamicContext(configuration, user);

        // 会依次拼接
        StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode("select * from user");
        staticTextSqlNode.apply(context);

        // 多个SQL片段使用mixNode节点存储

        IfSqlNode ifSqlNodeOnId = new IfSqlNode(new StaticTextSqlNode(" and id = #{id}"), "id != null");
        IfSqlNode ifSqlNodeOnName = new IfSqlNode(new StaticTextSqlNode(" and name = #{name}"), "name != null");
        MixedSqlNode mixedSqlNode = new MixedSqlNode(Arrays.asList(ifSqlNodeOnId, ifSqlNodeOnName));

        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, mixedSqlNode);
        whereSqlNode.apply(context);

        System.out.println(context.getSql());
    }
}
