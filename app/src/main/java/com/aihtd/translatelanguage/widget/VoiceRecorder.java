package com.aihtd.translatelanguage.widget;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import com.aihtd.translatelanguage.TranApplication;
import com.aihtd.translatelanguage.utils.AudioRecordManager;
import com.orhanobut.logger.Logger;


import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.media.AudioFormat.ENCODING_PCM_16BIT;

/**
 * 所在包名：com.suneee.ftx.weilian.util
 * 描述：声音录制
 * 作者：Dabin
 * 创建时间：2018/2/8
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class VoiceRecorder {
//    MediaRecorder recorder;
    AudioRecordManager instance;
    static final String PREFIX = "voice";
    static final String EXTENSION = ".pcm";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    public VoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     */
    public String startRecording(Context appContext) {


        instance = AudioRecordManager.getInstance();
        voiceFileName = getVoiceFileName("temp");
        voiceFilePath = new VoiceFileUtil("chat", appContext).getVoicePath() + voiceFileName;
        instance.startRecord(voiceFilePath);
        isRecording = true;
//        file = null;
//        try {
//            // need to create recorder every time, otherwise, will got exception
//            // from setOutputFile when try to reuse
//            if (recorder != null) {
//                recorder.release();
//                recorder = null;
//            }
//            recorder = new MediaRecorder();
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            recorder.setAudioChannels(1); // MONO
//            recorder.setAudioSamplingRate(16000); // 8000Hz
//            recorder.setAudioEncodingBitRate(ENCODING_PCM_16BIT ); // seems if change this to
//            // 128, still got same file
//            // size.
//            // one easy way is to use temp file
//            // file = File.createTempFile(PREFIX + userId, EXTENSION,
//            // User.getVoicePath());
//            voiceFileName = getVoiceFileName("temp");
//            ;
//            voiceFilePath = new VoiceFileUtil("chat", appContext).getVoicePath() + voiceFileName;
//            Log.d("voiceFilePath--->", voiceFilePath);
//            file = new File(voiceFilePath);
//            recorder.setOutputFile(file.getAbsolutePath());
//            recorder.prepare();
//            isRecording = true;
//            recorder.start();
//        } catch (IOException e) {
//            //    EMLog.e("voice", "prepare() failed");
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
//                        Logger.d("nihao -->" + recorder.getMaxAmplitude());
                        android.os.Message msg = new android.os.Message();
                        msg.what = 10 * 13 / 0x7FFF;
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
        startTime = new Date().getTime();
        return file == null ? null : file.getAbsolutePath();
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */

    public void discardRecording() {
        if(instance!=null){
            instance.stopRecord();
            isRecording = false;
        }

//        if (recorder != null) {
//            try {
//                recorder.stop();
//                recorder.release();
//                recorder = null;
//                if (file != null && file.exists() && !file.isDirectory()) {
//                    file.delete();
//                }
//            } catch (IllegalStateException e) {
//            } catch (RuntimeException e) {
//            }
//            isRecording = false;
//        }
    }

    public int stopRecoding() {
        if(instance!=null){
            isRecording = false;
            instance.stopRecord();
        }
//        if (recorder != null) {
//            isRecording = false;
//            recorder.stop();
//            recorder.release();
//            recorder = null;
//
//            if (file == null || !file.exists() || !file.isFile()) {
//                return -1;
//            }
//            if (file.length() == 0) {
//                file.delete();
//                return -2;
//            }
//            int seconds = (int) (new Date().getTime() - startTime) / 1000;
//             Log.e("1","voice"+ "voice recording finished. seconds:" + seconds + " file duration:" + file.length());
//            return seconds;
//        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();

        if(instance!=null){
            instance.stopRecord();
        }
//        if (recorder != null) {
//            recorder.release();
//        }
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
