package com.android.lily.review.proxy.statics;

/**
 * @author rape flower
 * @date 2018-07-18 15:15
 * @descripe
 */
public class RealSubject implements AbstractSubject {

    @Override
    public void sing() {
        System.out.println("具体的实现类");
    }
}
