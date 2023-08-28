package com.hao.mybatis.proxy.statics;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        TargetProxy targetProxy = new TargetProxy(new TargetInterfaceImpl());
        targetProxy.sayHello();
    }
}
