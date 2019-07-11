package com.aihtd.translatelanguage.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Base64;

import java.io.*;


/**
 * 所在包名：com.suneee.ftx.weilian.util
 * 描述：文件与流处理工具类
 * 作者：Dabin
 * 创建时间：2018/2/8
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class FileUtil {


    /**
     * base64字符串转文件
     *
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64, String filePath, String fileName) {
        File file = null;
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            file = new File(filePath, fileName);
            if (!file.exists())
                file.createNewFile();
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
            }
//            play(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 文件转base64字符串
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) { // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }




    /**
     * 播放二进制的音频
     *
     * @param recordingFile
     */
    public static AudioTrack play(File recordingFile) {
        DataInputStream dis = null;
        try {
            //从音频文件中读取声音
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(recordingFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //最小缓存区
        final int bufferSizeInBytes = AudioTrack.getMinBufferSize(16 * 1000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //创建AudioTrack对象   依次传入 :流类型、采样率（与采集的要一致）、音频通道（采集是IN 播放时OUT）、量化位数、最小缓冲区、模式
        final AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, 16 * 1000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);

        final byte[] data = new byte[bufferSizeInBytes];

        final DataInputStream finalDis = dis;
        new Thread(new Runnable() {
            @Override
            public void run() {
                player.play();//开始播放
                while (true) {
                    int i = 0;
                    try {
                        while (finalDis.available() > 0 && i < data.length) {
                            data[i] = finalDis.readByte();//录音时write Byte 那么读取时就该为readByte要相互对应
                            i++;
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    player.write(data, 0, data.length);

                    if (i != bufferSizeInBytes) //表示读取完了
                    {
                        player.stop();//停止播放
                        player.release();//释放资源
                        //TODO  发送Eventbus 通知播放完了
                        break;
                    }
                }
            }
        }).start();
        return player;
    }



    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public static boolean notEmptyFile(String path){
        File file = new File(path);
        if(file.length() == 0){
            return false;
        }

        return true;

    }


}
