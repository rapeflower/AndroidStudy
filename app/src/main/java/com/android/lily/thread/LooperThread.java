package com.android.lily.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/***********
 * @Author rape flower
 * @Date 2017-02-24 10:34
 * @Describe 线程
 */
public class LooperThread extends Thread{
    public Handler mHandler;

    @Override
    public void run() {
        Looper.prepare();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // process incoming message here
            }
        };

        Looper.loop();
    }
}
