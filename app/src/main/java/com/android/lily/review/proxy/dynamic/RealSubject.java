package com.android.lily.review.proxy.dynamic;

/**
 * @author rape flower
 * @date 2018-07-18 15:30
 * @descripe
 */
public class RealSubject implements AbstractSubject {

    @Override
    public void sing() {
        System.out.println("dynamic -> 具体的实现类");
    }
}
