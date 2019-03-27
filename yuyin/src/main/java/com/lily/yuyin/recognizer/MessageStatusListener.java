package com.lily.yuyin.recognizer;


import android.os.Handler;
import android.os.Message;

import com.baidu.speech.asr.SpeechConstant;

public class MessageStatusListener extends RecognizerStatusListener {

    private static final String TAG = "MessageStatusListener";
    private Handler mHandler;
    private long speechEndTime = 0;
    private boolean needTime = true;

    public MessageStatusListener(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onAsrReady() {
        super.onAsrReady();
        speechEndTime = 0;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_WAKEUP_READY, "引擎就绪，可以开始说话");
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN, "检测到用户说话");
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        speechEndTime = System.currentTimeMillis();
        sendMessage("【asr.end事件】检测到用户说话结束");
    }

    @Override
    public void onAsrPartialResult(String[] results, RecognizerResult recognizerResult) {
        String msg = "临时识别结果，结果是：“" + results[0] + "”；原始json：" + recognizerResult.getOriginalJson();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, msg);
        super.onAsrPartialResult(results, recognizerResult);
    }

    @Override
    public void onAsrFinalResult(String[] results, RecognizerResult recognizerResult) {
        super.onAsrFinalResult(results, recognizerResult);
        String msg = "识别结束，结果是：“" + results[0] + "”";
        String json = "；原始json：" + recognizerResult.getOriginalJson();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, msg + json);
        if (speechEndTime > 0) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - speechEndTime;
            msg += "；说话结束到识别结束耗时【 " + diffTime + "ms 】" + currentTime;
        }
        speechEndTime = 0;
        sendMessage(msg, status, true);
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecognizerResult recognizerResult) {
        super.onAsrFinishError(errorCode, subErrorCode, descMessage, recognizerResult);
        String msg = "【asr.finish事件】识别错误，错误码：" + errorCode + "，" + subErrorCode + "；" + descMessage;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, msg);
        if (speechEndTime > 0) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - speechEndTime;
            msg += "；说话结束到识别结束耗时【 " + diffTime + "ms 】";
        }
        speechEndTime = 0;
        sendMessage(msg, status, true);
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, "原始语义识别结果json：" + nluResult);
        }
    }

    @Override
    public void onAsrFinish(RecognizerResult recognizerResult) {
        super.onAsrFinish(recognizerResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_FINISH, "识别一段话结束，如果是长语音的情况会继续识别下段话。");
    }

    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH, "长语音识别结束");
    }

    @Override
    public void onOfflineLoaded() {
        super.onOfflineLoaded();
        // 没有此回调可能离线语法功能不能使用
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LOADED, "离线资源加载成功");
    }

    @Override
    public void onOfflineUnLoaded() {
        super.onOfflineUnLoaded();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED, "离线资源卸载成功");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_EXIT, "识别引擎结束并空闲中");
    }

    private void sendStatusMessage(String eventName, String message) {
        message = "[" + eventName + "]" + message;
        sendMessage(message, status);
    }

    private void sendMessage(String message) {
        sendMessage(message, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }

    private void sendMessage(String message, int what, boolean flag) {
        if (needTime && what != STATUS_FINISHED) {
            message += " ; time = " + System.currentTimeMillis();
        }
        if (mHandler == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (flag) {
            msg.arg2 = 1;
        }
        msg.obj = message + "\n";
        mHandler.sendMessage(msg);
    }
}
