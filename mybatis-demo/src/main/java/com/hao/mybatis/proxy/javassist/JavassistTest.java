package com.hao.mybatis.proxy.javassist;

/**
 * @author xuh
 * @date 2023/8/31
 */
public class JavassistTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        TargetProxy targetProxy = new TargetProxy();

        TargetInterface proxy = (TargetInterface) targetProxy.getProxy(TargetInterfaceImpl.class);

        proxy.sayHello();
    }
}
