package com.android.lily.review.proxy.statics;

/**
 * @author rape flower
 * @date 2018-07-18 15:12
 * @descripe
 */
public class ProxySubject implements AbstractSubject {

    private AbstractSubject abstractSubject;

    public ProxySubject(AbstractSubject as) {
        this.abstractSubject = as;
    }

    @Override
    public void sing() {
        this.abstractSubject.sing();
    }
}
