package com.hao.mybatis.proxy.javassist;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class TargetInterfaceImpl implements TargetInterface {
    @Override
    public void sayHello() {
        System.out.println("say hi!");
    }
}
