package com.icodechef.android.tick;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.concurrent.TimeUnit;

public class TickApplication extends Application {
    public static final int DEFAULT_WORK_LENGTH = 25;
    public static final int DEFAULT_SHORT_BREAK = 5;
    public static final int DEFAULT_LONG_BREAK  = 20;
    public static final int DEFAULT_LONG_BREAK_FREQUENCY = 4; // 默认 4 次开始长休息

    // 场景
    public static final int SCENE_WORK = 0;
    public static final int SCENE_SHORT_BREAK = 1;
    public static final int SCENE_LONG_BREAK = 2;

    // 当前状态
    public static final int STATE_WAIT = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_FINISH = 3;

    private long mStopTimeInFuture;
    private long mMillisInTotal;
    private long mMillisUntilFinished;

    private int mTimes;
    private int mState;

    private SpeechRecognizer speechRecognizer;

    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mState = STATE_WAIT;
    }

    public void reload() {
        switch(mState) {
            case STATE_WAIT:
            case STATE_FINISH:
                mMillisInTotal = TimeUnit.MINUTES.toMillis(getMinutesInTotal());
                mMillisUntilFinished = mMillisInTotal;
                break;
            case STATE_RUNNING:
                if (SystemClock.elapsedRealtime() > mStopTimeInFuture) {
                    finish();
                }
                break;
        }
    }

    public void start() {
        setState(STATE_RUNNING);
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInTotal;
    }

    public void pause() {
        setState(STATE_PAUSE);
        mMillisUntilFinished = mStopTimeInFuture - SystemClock.elapsedRealtime();
    }

    public void resume() {
        setState(STATE_RUNNING);
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisUntilFinished;
    }

    public void stop() {
        setState(STATE_WAIT);
        reload();
    }

    public void skip() {
        setState(STATE_WAIT);
        setTimes();
        reload();
    }

    public void finish() {
        setState(STATE_FINISH);
        setTimes();
        reload();
    }

    public void exit() {
        setState(STATE_WAIT);
        mTimes = 0;
        reload();
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    private void setTimes() {
        mTimes++; // 注意这里不能在 activity 中使用, 如果睡眠中就不能保证会运行
    }

    public int getCurrentPomodoro() {
        int frequency = getSharedPreferences()
                .getInt("pref_key_long_break_frequency", DEFAULT_LONG_BREAK_FREQUENCY);
        return (mTimes / 2) % frequency; // 返回现在进行到多少个番茄了, 休息和工作都会增加mTimes, 所以要除以2
    }

    public int getTotalPomodoros() {
        int frequency = getSharedPreferences()
                .getInt("pref_key_long_break_frequency", DEFAULT_LONG_BREAK_FREQUENCY);
        return frequency; // 返回番茄频率
    }

    public int getScene() {
        int frequency = getSharedPreferences()
                .getInt("pref_key_long_break_frequency", DEFAULT_LONG_BREAK_FREQUENCY);
        frequency = frequency * 2; // 工作/短休息/工作/短休息/工作/短休息/工作/长休息

        if (mTimes % 2  == 1) { // 偶数：工作, 奇数：休息

            if ((mTimes + 1 ) % frequency == 0) { // 长休息
                return SCENE_LONG_BREAK;
            }

            return SCENE_SHORT_BREAK;
        }

        return SCENE_WORK;
    }

    public int getMinutesInTotal() {
        int minutes = 0;

        switch (getScene()) {
            case SCENE_WORK:
                minutes = getSharedPreferences()
                        .getInt("pref_key_work_length", DEFAULT_WORK_LENGTH);
                break;
            case SCENE_SHORT_BREAK:
                minutes = getSharedPreferences()
                        .getInt("pref_key_short_break", DEFAULT_SHORT_BREAK);
                break;
            case SCENE_LONG_BREAK:
                minutes = getSharedPreferences()
                        .getInt("pref_key_long_break", DEFAULT_LONG_BREAK);
                break;
        }

        return minutes;
    }

    public long getMillisInTotal() {
        return mMillisInTotal;
    }

    public void setMillisUntilFinished(long millisUntilFinished) {
        mMillisUntilFinished = millisUntilFinished;
    }

    public long getMillisUntilFinished() {
        if (mState == STATE_RUNNING) {
            return mStopTimeInFuture - SystemClock.elapsedRealtime();
        }

        return mMillisUntilFinished;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

//    public void startrecognition(){
//        // 创建语音识别意图
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh");
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说出您的命令");
//
//        //开始识别
//        startActivityForResult(intent, SPEECH_REQUEST_CODE);
//        speechRecognizer.startListening(intent);
//        String[] Order={"开始计时","暂停计时","恢复计时","停止计时"};
//    }
}
