package com.lily.yuyin.recognizer;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.speech.asr.SpeechConstant;
import com.lily.yuyin.R;
import com.lily.yuyin.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author rape flower
 * @Date 2019-03-26 10:54
 * @Describe 百度语音识别结果mode
 */
public class OnlineRecognizerActivity extends BaseActivity implements IStatus {

    private static final String TAG = OnlineRecognizerActivity.class.getSimpleName();
    private TextView tvRecognizerResult;
    private Button btnStart;
    private Button btnStop;

    /**
     * 识别控制器，使用BdRecognizer控制识别的流程
     */
    protected BdRecognizer recognizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_recognizer);
        initView();
        IRecognizerListener recognizerListener = new MessageStatusListener(mHandler);
        recognizer = new BdRecognizer(this, recognizerListener);

        Log.w(TAG, "nativeLibraryDir = " + getApplicationInfo().nativeLibraryDir);
    }

    /**
     * 初始化view
     */
    private void initView() {
        tvRecognizerResult = findViewById(R.id.tv_recognizer_result);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(clickListener);
        btnStop.setOnClickListener(clickListener);
    }

    /**
     * 点击监听
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_start:
                    start();
                    break;
                case R.id.btn_stop:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开始录音
     */
    protected void start() {
        // 这只识别输入参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, true);
        params.put(SpeechConstant.DISABLE_PUNCTUATION, false);
        params.put(SpeechConstant.PID, 1536);

        recognizer.start(params);
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stop() {
        recognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancel() {
        recognizer.cancel();
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case STATUS_FINISHED:
                Log.w(TAG, "arg2 =" + msg.arg2);
                if (msg.arg2 == 1) {
                    tvRecognizerResult.setText(msg.obj.toString());
                }
                break;
            case STATUS_NONE:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                break;
            default:
                break;
        }
    }

    /**
     * 输出事件
     */
    IRecognizerListener listener = new IRecognizerListener() {
        @Override
        public void onAsrReady() {

        }

        @Override
        public void onAsrBegin() {

        }

        @Override
        public void onAsrEnd() {

        }

        @Override
        public void onAsrPartialResult(String[] results, RecognizerResult recognizerResult) {

        }

        @Override
        public void onAsrOnlineNluResult(String nluResult) {

        }

        @Override
        public void onAsrFinalResult(String[] results, RecognizerResult recognizerResult) {
            String message = "识别结束，结果是：“" + results[0] + "”";
            String json = "原始json：" + recognizerResult.getOriginalJson();
            tvRecognizerResult.setText(message + "\n" + json);
            Log.w(TAG, message + "\n" + json);
        }

        @Override
        public void onAsrFinish(RecognizerResult recognizerResult) {

        }

        @Override
        public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecognizerResult recognizerResult) {

        }

        @Override
        public void onAsrLongFinish() {

        }

        @Override
        public void onAsrVolume(int volumePercent, int volume) {

        }

        @Override
        public void onAsrAudio(byte[] data, int offset, int length) {

        }

        @Override
        public void onAsrExit() {

        }

        @Override
        public void onOfflineLoaded() {

        }

        @Override
        public void onOfflineUnLoaded() {

        }
    };

    @Override
    protected void onDestroy() {
        recognizer.release();
        super.onDestroy();
    }
}
