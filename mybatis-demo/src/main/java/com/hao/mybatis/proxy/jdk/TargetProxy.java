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

    /**
     * 带着疑问看源码
     * 从Proxy.newProxyInstance可以知道，我们通过getProxyClass0得到了具体的代理类的Class，为什么此时不直接使用Class.newInstance，得到具体的代理类对象
     * 而是要再次使用构造方法呢？
     */
    public <T> T getProxy(Class interfaces) {
        // 在内存中生成一个代理类，具体实现流程如下：
        // 1. 通过Class<?> cl = getProxyClass0(loader, intfs)获得需要代理的class
        //    1.1 通过subKeyFactory.apply(key, parameter)得到一个代理的对象
        //    1.1 subKeyFactory是一个函数式接口，其实现类为ProxyClassFactory，该类为Proxy的内部类
        //    1.3 在ProxyClassFactory中，将通过Class.forName(intf.getName(), false, loader)的方式获得Class判断要代理的是否是接口，如果是接口则报错
        //    1.4 随后通过byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags)得到代理类文件字节数组
        //        在该方法中可通过系统配置sun.misc.ProxyGenerator.saveGeneratedFiles来判断是否将代理得到的字节数组文件保存到本地
        //    1.5 最终调用defineClass0()得到真正的类对象
        // 2. 通过cl.getConstructor({ InvocationHandler.class })获得该类的构造器
        // 3. 通过cons.newInstance(new Object[]{h})最终得到代理对象
        // 4. 最终使用代理类对象.方法()，其实会执行代理类中对应的方法 super.h.invoke(this, m3, (Object[])null)
        // 5. super.h这个对象是父类Proxy的InvocationHandler，那么这个InvocationHandler是怎么赋值的呢？
        // 6. 在Proxy.newProxyInstance中得到一个代理的class后，也就是我们的第一步，还会执行final Constructor<?> cons = cl.getConstructor(constructorParams)
        //    使用代理类中的参数为InvocationHandler的构造方法，也就是第2步，最终将我们的实现InvocationHandler的内作为构造参数得到真正到代理类对象

        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{interfaces}, this);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置处理");

        Object result = method.invoke(target, args);

        System.out.println("后置处理");
        return result;
    }
}
