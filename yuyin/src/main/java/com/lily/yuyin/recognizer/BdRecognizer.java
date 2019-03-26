package com.lily.yuyin.recognizer;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.Map;

/**
 * @Author rape flower
 * @Date 2019-03-26 09:55
 * @Describe 百度语音识别控制器类
 */
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
    private static volatile boolean isInitialized = false;

    /**
     * 初始化
     * @param context
     * @param recognizerListener 将EventListener结果坐解析的回调，使用RecognizerEventAdapter适配EventListener
     */
    public BdRecognizer(Context context, IRecognizerListener recognizerListener) {
        this(context, new RecognizerEventAdapter(recognizerListener));
    }

    /**
     * 初始化 提供EventManagerFactory需要的Context和EventListener
     * @param context
     * @param eventListener
     */
    public BdRecognizer(Context context, EventListener eventListener) {
        if (isInitialized) {
            throw new RuntimeException("还未调用release()，请勿新建一个类");
        }
        isInitialized = true;
        this.eventListener = eventListener;
        //初始化asr的EventListener实例，多次得到的类，只能选一个使用
        asr = EventManagerFactory.create(context, "asr");
        //设置回调event，识别引擎会回调这个类告知重要状态和识别结果
        asr.registerListener(eventListener);
    }

    /**
     * 离线命令词，在线不需要调用
     *
     * @param params 离线命令词加载参数，"ASR_KWS_LOAD_ENGINE 输入事件参数"
     */
    public void loadOfflineEngine(Map<String, Object> params) {
        String json = new JSONObject(params).toString();
        // 加载离线命令词（离线时使用）
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        isOfflineEngineLoaded = true;
        // 没ASR_KWS_LOAD_ENGINE这个回调表示失败，如缺少第一次联网时下载的正式授权文件。
    }

    /**
     * 开始
     *
     * @param params
     */
    public void start(Map<String, Object> params) {
        String json = new JSONObject(params).toString();
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    /**
     * 提前结束录音等待识别结果
     */
    public void stop() {
        if (!isInitialized) {
            throw new RuntimeException("release() was called");
        }
        asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果
     * cancel与stop的区别是：cancel在stop的基础上，完全停止整个识别流程
     */
    public void cancel() {
        if (!isInitialized) {
            throw new RuntimeException("release() was called");
        }
        // 取消本次识别
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

    /**
     * 释放所有资源
     */
    public void release() {
        if (asr == null) {
            return;
        }
        cancel();
        if (isOfflineEngineLoaded) {
            // 如果之前有调用过 加载离线命令词，这里要对应释放
            asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
            isOfflineEngineLoaded = false;
        }
        // 卸载listener
        asr.unregisterListener(eventListener);
        asr = null;
        isInitialized = false;
    }

    /**
     * 设置事件回调
     * @param recognizerListener
     */
    public void setEventListener(IRecognizerListener recognizerListener) {
        if (!isInitialized) {
            throw new RuntimeException("release() was called");
        }
        this.eventListener = new RecognizerEventAdapter(recognizerListener);
        asr.registerListener(eventListener);
    }
}
