package com.hao.mybatis.proxy.statics;

/**
 * @author xuh
 * @date 2023/8/28
 */
public class TargetProxy implements TargetInterface{

    TargetInterface target;

    public TargetProxy(TargetInterface target) {
        this.target = target;
    }
    @Override
    public void sayHello() {
        System.out.println("前置处理");

        target.sayHello();

        System.out.println("后置处理");
    }
}
