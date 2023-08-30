package com.hao.mybatis.proxy.cglib;

import net.sf.cglib.core.DebuggingClassWriter;

/**
 * @author xuh
 * @date 2023/8/30
 */
public class CglibTest {

    public static void main(String[] args) {
        // 通过参数设置，把动态代理生成的clas文件输出到磁盘，默认是不输出的
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:/code");

        TargetProxy targetProxy = new TargetProxy();

        // 难道目标代理类
        TargetInterface targetInterface = targetProxy.getProxy(TargetInterfaceImpl.class);

        // 代理接口
        // TargetInterface targetInterface = targetProxy.getProxy(TargetInterface.class);

        targetInterface.sayHello();
    }
}
