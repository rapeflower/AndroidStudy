package com.android.lily.review.proxy.dynamic;

/**
 * @author rape flower
 * @date 2018-07-18 15:17
 * @descripe
 */
public class Test {

    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        AbstractSubject subject = (AbstractSubject) new ProxySubject().bind(realSubject);
        subject.sing();
    }
}
