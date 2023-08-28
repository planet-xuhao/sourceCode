package com.hao.mybatis.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class TargetProxy implements InvocationHandler {
    private Object target;

    public TargetProxy(Object target) {
        this.target = target;
    }

    public <T> T getProxy(Class interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{interfaces}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置处理");

        Object result = method.invoke(target, args);

        System.out.println("后置处理");
        return result;
    }
}
