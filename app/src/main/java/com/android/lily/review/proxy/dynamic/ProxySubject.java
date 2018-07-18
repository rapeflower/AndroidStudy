package com.android.lily.review.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author rape flower
 * @date 2018-07-18 15:30
 * @descripe
 */
public class ProxySubject implements InvocationHandler {

    private Object proxy;

    public Object bind(Object target) {
        this.proxy = target;
        return Proxy.newProxyInstance(this.proxy.getClass().getClassLoader(), this.proxy.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        result = method.invoke(this.proxy, args);
        return result;
    }
}
