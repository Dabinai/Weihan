package com.aihtd.translatelanguage.constant

import android.os.Environment

/**
 * 所在包名：com.aihtd.translatelanguage.constant
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/4
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
interface ConstantString {
    companion object {
        val SOAPTAG = "SoapResult"

        val fileUrl = Environment.getExternalStorageDirectory().absolutePath + "/维汉/tts"
    }
}
