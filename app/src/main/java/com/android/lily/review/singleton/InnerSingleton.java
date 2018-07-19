package com.android.lily.review.singleton;

/**
 * @Author rape flower
 * @Date 2018-07-19 9:55
 * @Describe 单例模式：Java方式
 * <p>内部类式</p>
 */
public class InnerSingleton {

    private InnerSingleton() {

    }

    private static class Holder {
        private static InnerSingleton INSTANCE = new InnerSingleton();
    }

    public static InnerSingleton getInstance() {
        return Holder.INSTANCE;
    }
}
