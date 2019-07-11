package com.aihtd.translatelanguage.widget;

import android.content.Context;
import android.os.Environment;
import com.aihtd.translatelanguage.constant.ConstantString;

import java.io.File;


/**
 * 所在包名：com.dabin.huanxinone.widget
 * 描述：语音文件
 * 作者：Dabin
 * 创建时间：2019-03-06
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class VoiceFileUtil {

    private static VoiceFileUtil instance;
    private static File storageDir = null;
    private File voicePath = null;
    private File imagePath = null;
    private File videoPath = null;
    private File filePath;

    public static String pathPrefix;

    private String LOCATION_BASE = ConstantString.Companion.getFileUrl();

    public VoiceFileUtil(String chatTo, Context context) {
        String username = "user";//此处最好更改成当前用户名
        //保存地址
        pathPrefix = LOCATION_BASE;

        this.voicePath = generateVoicePath();
        if (!this.voicePath.exists()) {
            this.voicePath.mkdirs();
        }

    }


    private static File generateVoicePath() {
        return new File(pathPrefix);
    }


    private static File getStorageDir(Context context) {
        if (storageDir == null) {
            File file = Environment.getExternalStorageDirectory();
            if (file.exists()) {
                return file;
            }
            storageDir = context.getFilesDir();
        }
        return storageDir;
    }

    public File getVoicePath() {
        return this.voicePath;
    }


}
