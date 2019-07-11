package com.aihtd.translatelanguage

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


/**
 * 所在包名：com.aihtd.translatelanguage
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/4
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
class TranApplication : Application() {

    private var INSTANCE: TranApplication? = null
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    fun getContext(): TranApplication? {
        return INSTANCE
    }


}
