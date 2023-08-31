package com.hao.mybatis.proxy.javassist;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class TargetProxy implements MethodHandler {

    public Object getProxy(Class clazz) throws InstantiationException, IllegalAccessException {
        ProxyFactory proxyFactory = new ProxyFactory();

        // 代理类保存到硬盘
        proxyFactory.writeDirectory = "D:\\";

        proxyFactory.setSuperclass(clazz);

        Object proxy = proxyFactory.createClass().newInstance();

        ((ProxyObject) proxy).setHandler(this);

        return proxy;
    }


    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        System.out.println("前置处理");

        proceed.invoke(self, args);

        System.out.println("后置处理");

        return null;
    }
}
