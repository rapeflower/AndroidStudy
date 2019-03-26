package com.lily.yuyin.recognizer;

import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author rape flower
 * @Date 2019-03-26 10:59
 * @Describe 自定义百度语音识回调
 */
public class RecognizerEventAdapter implements EventListener {

    private static final String TAG = "RecognizerEventAdapter";
    private IRecognizerListener listener;

    public RecognizerEventAdapter(IRecognizerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logMessage = "name: " + name + "; params: " + params;
        Log.i(TAG, logMessage);

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LOADED)) {
            listener.onOfflineLoaded();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED)) {
            listener.onOfflineUnLoaded();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            // 引擎准备就绪，可以开始说话
            listener.onAsrReady();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)) {
            // 检测到用户已经开始说话
            listener.onAsrBegin();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)) {
            listener.onAsrEnd();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            RecognizerResult recognizerResult = RecognizerResult.parseJson(params);
            // 临时识别结果，长语音模式需要从此消息中取出结果
            String[] results = recognizerResult.getResultsRecognition();
            if (recognizerResult.isFinalResult()) {
                listener.onAsrFinalResult(results, recognizerResult);
            } else if (recognizerResult.isPartialResult()) {
                listener.onAsrPartialResult(results, recognizerResult);
            } else if (recognizerResult.isNluResult()) {
                listener.onAsrOnlineNluResult(new String(data, offset, length));
            }
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // 识别结束，最终识别结果或可能的错误
            RecognizerResult recognizerResult = RecognizerResult.parseJson(params);
            if (recognizerResult.hasError()) {
                int errorCode = recognizerResult.getError();
                int subErrorCode = recognizerResult.getSubError();
                listener.onAsrFinishError(errorCode, subErrorCode, recognizerResult.getDesc(), recognizerResult);
            } else {
                listener.onAsrFinish(recognizerResult);
            }
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH)) {
            // 长语音
            listener.onAsrLongFinish();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)) {
            listener.onAsrExit();
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
            Volume volume = parseVolumeJson(params);
            listener.onAsrVolume(volume.volumePercent, volume.volume);
        } else if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_AUDIO)) {
            if (data.length != length) {
                Log.e(TAG, "internal error: asr.audio callback data length is not equal to length param");
            }
            listener.onAsrAudio(data, offset, length);
        }
    }

    private Volume parseVolumeJson(String jsonStr) {
        Volume volume = new Volume();
        volume.originalJson = jsonStr;

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            volume.volumePercent = jsonObject.getInt("");
            volume.volume = jsonObject.getInt("volume");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return volume;
    }

    private class Volume {
        private int volumePercent = -1;
        private int volume = -1;
        private String originalJson;
    }
}
