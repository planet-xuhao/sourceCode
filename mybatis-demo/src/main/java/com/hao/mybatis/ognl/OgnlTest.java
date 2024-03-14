package com.hao.mybatis.ognl;

import com.hao.mybatis.po.Blog;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.junit.Test;

/**
 * @author xuh
 * @date 2024/3/4
 */
public class OgnlTest {

    @Test
    public void test() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Blog blog = new Blog();
        blog.setId(1);
        // 访问属性
        boolean result = evaluator.evaluateBoolean("id != null || author.name != null", blog);
        System.out.println(result);

        // 访问方法
        System.out.println(evaluator.evaluateBoolean("comment != null and comment.size() > 0", blog));

    }
}
