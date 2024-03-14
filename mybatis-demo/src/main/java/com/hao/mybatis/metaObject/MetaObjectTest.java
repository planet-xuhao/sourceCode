package com.hao.mybatis.metaObject;

import com.alibaba.fastjson.JSON;
import com.hao.mybatis.po.Blog;
import com.hao.mybatis.po.Comment;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author xuh
 * @date 2024/3/5
 */
public class MetaObjectTest {


    @Test
    public void test() {
        Blog blog = new Blog();

        Configuration configuration = new Configuration();
        MetaObject metaObject = configuration.newMetaObject(blog);

        // 赋值，自动创建对象
        metaObject.setValue("title", "张三");
        metaObject.setValue("author.name", "李四");

        System.out.println(JSON.toJSONString(blog));

        // 驼峰查找属性
        String result = metaObject.findProperty("author.phone_number", true);
        System.out.println(result);

        // 数组赋值,数组需要手动创建
        Comment comment = new Comment();
        comment.setDesc("评论");
        metaObject.setValue("comment", Collections.singletonList(comment));
        System.out.println(JSON.toJSONString(metaObject.getValue("comment[0].desc")));

        // map赋值，map需要手动创建
        metaObject.setValue("labels", new HashMap<>());
        metaObject.setValue("labels[red]", "顶");
        System.out.println(metaObject.getValue("labels[red]"));

    }
}
