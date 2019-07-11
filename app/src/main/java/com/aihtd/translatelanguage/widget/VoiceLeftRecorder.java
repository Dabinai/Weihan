package com.aihtd.translatelanguage.widget;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import com.aihtd.translatelanguage.MainActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Date;

/**
 * 所在包名：com.suneee.ftx.weilian.util
 * 描述：声音录制
 * 作者：Dabin
 * 创建时间：2018/2/8
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class VoiceLeftRecorder {
    MediaRecorder recorder;

    static final String PREFIX = "voice";
    static final String EXTENSION = ".wav";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;
    private Context appContext;

    public VoiceLeftRecorder(Context appContext, Handler handler) {
        this.appContext = appContext;
        this.handler = handler;
    }

    /**
     * start recording to the file
     */
    public String startRecording(Context appContext) {
        file = null;
        // need to create recorder every time, otherwise, will got exception
        // from setOutputFile when try to reuse
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        isRecording = true;
        ((MainActivity) appContext).setOnVolumClickListener(new MainActivity.OnVolumClickListener() {
            @Override
            public void onItemClick(final int volumNumber) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Logger.d("onItemClick-->" + volumNumber);
                            while (isRecording) {
                                android.os.Message msg = new android.os.Message();
//                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
//                                msg.what = 10 * 13 / 0x7FFF;
                                msg.what = volumNumber * 60 / 0x7FFF;
                                handler.sendMessage(msg);
                                SystemClock.sleep(100);
                            }
                        } catch (Exception e) {
                            // from the crash report website, found one NPE crash from
                            // one android 4.0.4 htc phone
                            // maybe handler is null for some reason
                        }
                    }
                }).start();
            }
        });

//            recorder.start();

        startTime = new Date().getTime();
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */

    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e) {
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return -1;
            }
            if (file.length() == 0) {
                file.delete();
                return -2;
            }

        }

        int seconds = (int) (new Date().getTime() - startTime) / 1000;
//        Log.e("1", "voice" + "voice recording finished. seconds:" + seconds + " file duration:" + file.length());
        return seconds;
//        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }


    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }


}
