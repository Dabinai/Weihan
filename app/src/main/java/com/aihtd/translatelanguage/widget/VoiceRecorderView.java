package com.aihtd.translatelanguage.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aihtd.translatelanguage.R;
import com.orhanobut.logger.Logger;


/**
 * 所在包名：com.suneee.ftx.weilian.view
 * 描述：录制声音自定View
 * 作者：Dabin
 * 创建时间：2018/2/8
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class VoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected VoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;
    private int duration = 60;
    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            text = recordingHint.getText().toString();
            if (text.equals("松开手指 取消发送")) {

            } else if (text.equals("录制时间太短")) {

            } else {
                micImage.setImageDrawable(micImages[msg.what]);
            }

        }
    };
    private boolean isRecording = false;
    private String text;
    private EaseVoiceRecorderCallback recorderCallback;

    public VoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_voice_recorder, this);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        voiceRecorder = new VoiceRecorder(micImageHandler);

//         animation resources, used for recording
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.chat_voice01_pressed),
                getResources().getDrawable(R.drawable.chat_voice01_pressed),
                getResources().getDrawable(R.drawable.chat_voice02_pressed),
                getResources().getDrawable(R.drawable.chat_voice02_pressed),
                getResources().getDrawable(R.drawable.chat_voice02_pressed),
                getResources().getDrawable(R.drawable.chat_voice03_pressed),
                getResources().getDrawable(R.drawable.chat_voice03_pressed),
                getResources().getDrawable(R.drawable.chat_voice03_pressed),
                getResources().getDrawable(R.drawable.chat_voice04_pressed),
                getResources().getDrawable(R.drawable.chat_voice04_pressed),
                getResources().getDrawable(R.drawable.chat_voice04_pressed),
                getResources().getDrawable(R.drawable.chat_voice05_pressed),
                getResources().getDrawable(R.drawable.chat_voice05_pressed),
                getResources().getDrawable(R.drawable.chat_voice05_pressed),
        };

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */

    MyCountDownTimer myCountDownTimer;
    float startX = 0, startY = 0, nowX = 0, nowY = 0;

    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
//                Logger.e("发送语音：手指：ACTION_DOWN");
                startY = event.getRawY();
                startX = event.getRawX();
                this.recorderCallback = recorderCallback;


                duration = 60;
                try {
                    // 判断是否在播放
//                    AudioManager manager =
//                    if (VoicePlayClickListener.isPlaying)
//                        VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    v.setPressed(true);
                    if (startRecording()) {
                        isRecording = true;
                        myCountDownTimer = new MyCountDownTimer(60000, 1000, v, this.recorderCallback, event);
                        myCountDownTimer.start();
                        if (this.recorderCallback != null) {
                            this.recorderCallback.onVoiceRecordStart();
                        }
                    }

                } catch (Exception e) {
                    v.setPressed(false);
                    isRecording = false;
                }

                return true;
            case MotionEvent.ACTION_MOVE:
//                Logger.e("发送语音：手指：ACTION_MOVE");
                nowX = event.getRawX();
                nowY = event.getRawY();
                if (isRecording && duration > 10) {
                    if (startY - nowY > 300) {
                        showReleaseToCancelHint();
                    } else {
                        showMoveUpToCancelHint();
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
//                Logger.e("发送语音：手指：ACTION_UP");
                nowX = event.getRawX();
                nowY = event.getRawY();
                if (isRecording) {
                    if (myCountDownTimer != null) {
                        myCountDownTimer.onFinish();
                        myCountDownTimer.cancel();
                        myCountDownTimer = null;
                       discardRecording();
                        if (startY - nowY > 300) {
                            if(recorderCallback != null){
                                recorderCallback.onVoiceRecordCancle();
                            }
                        }else {
                            if(recorderCallback != null){
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), 0);
                            }
                        }


                        startY = 0;
                        startX = 0;
                        nowX = 0;
                        nowY = 0;
                        return true;
                    }

                    v.setPressed(false);
                    if (startY - nowY > 300) {
                        discardRecording();

                    } else {
                        try {
                            int length = stopRecoding();
//                            LogUtils.e("VoiceRecorderView：：ACTION_UP" + length);
//                        Toast.makeText(context, "up" + length, Toast.LENGTH_SHORT).show();
                            if (length > 0) {
                                if (this.recorderCallback != null) {
                                    this.recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                                }
                            } else if (length == -1) {
                                this.recorderCallback.onVoiceRecordShowView("未获得权限");
                            } else if (length == -2) {
                                this.recorderCallback.onVoiceRecordShowView("请打开录音权限");
                            } else {
                                this.recorderCallback.onVoiceRecordShowView("录音时间太短");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            Toast.makeText(context, "录制失败", Toast.LENGTH_SHORT).show();
                        } finally {
                            isRecording = false;
                        }

                    }
                }
                startY = 0;
                startX = 0;
                nowX = 0;
                nowY = 0;
                return true;
            default:
//                Logger.e("发送语音：手指：default");
//                discardRecording();
                //当进行其手势时的操作
                if (isRecording) {
                    if (myCountDownTimer != null) {
                        myCountDownTimer.onFinish();
                        myCountDownTimer.cancel();
                        myCountDownTimer = null;
                        if (this.recorderCallback != null) {
                            this.recorderCallback.onVoiceRecordCancle();
                        }
                        startY = 0;
                        startX = 0;
                        nowX = 0;
                        nowY = 0;
                        return true;
                    }

                    v.setPressed(false);
                    if (startY - nowY > 300) {
                        discardRecording();
                    } else {
                        try {
                            if (this.recorderCallback != null) {
                                this.recorderCallback.ondismissView();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            isRecording = false;
                        }

                    }
                }
                return false;
        }
    }


    /**
     * 继承 CountDownTimer 防范
     * <p>
     * 重写 父类的方法 onTick() 、 onFinish()
     */

    public class MyCountDownTimer extends CountDownTimer {


        private EaseVoiceRecorderCallback recorderCallback;
        private View view;
        private MotionEvent event;

        public MyCountDownTimer(long millisInFuture, long countDownInterval, View view, EaseVoiceRecorderCallback recorderCallback, MotionEvent event) {
            super(millisInFuture, countDownInterval);

            this.recorderCallback = recorderCallback;
            this.view = view;
            this.event = event;
        }

        @Override
        public void onFinish() {
            duration = 60;
//            Logger.e("发送语音onFinish--->", "onFinish");
            view.setPressed(false);
//            Logger.e("发送语音onFinish--->", "onFinish" + event.getY());
            if (startY - nowY > 300) {
                // discard the recorded audio.
                discardRecording();
            } else {
                // stop recording and send voice file
                try {
                    int length = stopRecoding();
//                    LogUtils.e("VoiceRecorderView：：finish");
//                    Toast.makeText(context, length, Toast.LENGTH_SHORT).show();
                    if (length > 0) {
                        if (recorderCallback != null) {
                            recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                        }
                    } else if (length == -1) {
                        recorderCallback.onVoiceRecordShowView("未获得权限");
                    } else if (length == -2) {
                        recorderCallback.onVoiceRecordShowView("请打开录音权限");
                    } else {
                        recorderCallback.onVoiceRecordShowView("录音时间太短");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "录制失败", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "录制失败", Toast.LENGTH_SHORT).show();
                } finally {
                    isRecording = false;
                }
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //  Log.d("millisUntilFinished--->",millisUntilFinished+"");
//            LogUtils.e("倒计时：----》" + millisUntilFinished / 1000);
            if (millisUntilFinished / 1000 < 11) {
                duration = (int) (millisUntilFinished / 1000);
                recordingHint.setText("还可以说" + millisUntilFinished / 1000 + "秒");
            }
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);

        void onVoiceRecordCancle();

        void onVoiceRecordStart();

        //显示录制时间太短
        void onVoiceRecordShowView(String msg);

        //default 操作
        void ondismissView();
    }

    public boolean startRecording() {
        if (!isSdcardExist()) {
//            Toast.makeText(context, "发送语音消息需要使用内存卡", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "发送语音消息需要使用内存卡", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            recordingHint.setText("手指上滑 取消发送");
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "请确认设备是否被占用", Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "请确认设备是否被其他应用占用", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isSdcardExist() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText("松开手指 取消发送");
        recordingHint.setBackgroundResource(R.drawable.bg_voicel);
        micImage.setImageResource(R.drawable.ic_voicemassage_cancel);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText("手指上划 取消发送");
        recordingHint.setBackgroundColor(55000000);
    }


    /**
     * 释放资源
     */
    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
        isRecording = false;
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public void cancelRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();

        if (isRecording) {
            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
                myCountDownTimer = null;
                if (this.recorderCallback != null) {
                    this.recorderCallback.onVoiceRecordCancle();
                }
                startY = 0;
                startX = 0;
                nowX = 0;
                nowY = 0;
            }
            try {
                // stop recording
                if (voiceRecorder.isRecording()) {
                    voiceRecorder.discardRecording();
                    this.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
            }
            isRecording = false;
        }
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
