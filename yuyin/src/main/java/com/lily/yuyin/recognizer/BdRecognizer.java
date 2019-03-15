package com.lily.yuyin.recognizer;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;

public class BdRecognizer {

    private static final String TAG = "BdRecognizer";
    /**
     * 百度语音核心类
     */
    private EventManager asr;
    /**
     * 事件回调类，识别回调逻辑处理
     */
    private EventListener eventListener;
    /**
     * 是否加载离线资源
     */
    private static boolean isOfflineEngineLoaded = false;
    /**
     * 用于控制未release前，只能new一个BdRecognizer
     */
    private static volatile boolean isInited = false;

    
}
