package com.hao.mybatis.proxy.jdk;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class JdkProxyTest {

    public static void main(String[] args) {
        TargetProxy targetProxy = new TargetProxy(new TargetInterfaceImpl());

        TargetInterface targetObject = (TargetInterface) targetProxy.getProxy(TargetInterface.class);

        targetObject.sayHello();
    }
}
