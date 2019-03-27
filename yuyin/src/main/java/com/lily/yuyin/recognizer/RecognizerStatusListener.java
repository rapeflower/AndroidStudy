package com.lily.yuyin.recognizer;

/**
 * @Author rape flower
 * @Date 2019-03-26 11:52
 * @Describe 根据回调，判断asr引擎的状态
 *
 * 通常状态变化如下：
 * STATUS_NONE 初始状态
 * STATUS_READY 引擎准备完毕
 * STATUS_SPEAKING 用户开始说话到用户说话完毕前
 * STATUS_RECOGNITION 用户说话完毕后，识别结束前
 * STATUS_FINISHED 获得最终识别结果
 */
public class RecognizerStatusListener implements IRecognizerListener, IStatus {

    private static final String TAG = "RecognizerStatusListener";

    /**
     * 识别的引擎当前状态
     */
    protected int status = STATUS_NONE;

    @Override
    public void onAsrReady() {
        status = STATUS_READY;
    }

    @Override
    public void onAsrBegin() {
        status = STATUS_SPEAKING;
    }

    @Override
    public void onAsrEnd() {
        status = STATUS_RECOGNITION;
    }

    @Override
    public void onAsrPartialResult(String[] results, RecognizerResult recognizerResult) {

    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        status = STATUS_FINISHED;
    }

    @Override
    public void onAsrFinalResult(String[] results, RecognizerResult recognizerResult) {
        status = STATUS_FINISHED;
    }

    @Override
    public void onAsrFinish(RecognizerResult recognizerResult) {
        status = STATUS_FINISHED;
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecognizerResult recognizerResult) {
        status = STATUS_FINISHED;
    }

    @Override
    public void onAsrLongFinish() {
        status = STATUS_LONG_SPEECH_FINISHED;
    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {

    }

    @Override
    public void onAsrAudio(byte[] data, int offset, int length) {
        if (offset != 0 || data.length != length) {
            byte[] actualData = new byte[length];
            System.arraycopy(data, 0, actualData, 0, length);
            data = actualData;
        }
    }

    @Override
    public void onAsrExit() {
        status = STATUS_NONE;
    }

    @Override
    public void onOfflineLoaded() {

    }

    @Override
    public void onOfflineUnLoaded() {

    }
}
