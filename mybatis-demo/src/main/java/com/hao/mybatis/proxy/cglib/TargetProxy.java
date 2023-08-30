package com.hao.mybatis.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class TargetProxy implements MethodInterceptor {

    public <T> T getProxy(Class<T> clazz) {
        // 字节码增强的一个类
        Enhancer enhancer = new Enhancer();

        // 设置父类
        enhancer.setSuperclass(clazz);
        // 设置代理接口
        // enhancer.setInterfaces(new Class[]{clazz});

        // 设置回调类
        enhancer.setCallback(this);

        // 创建代理类
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置处理");


        Object result = methodProxy.invokeSuper(obj, args);

        // 如果是代理接口的话需要自己去实现接口

        System.out.println("后置处理");
        return result;
    }
}
