package com.aihtd.translatelanguage

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.media.AudioTrack
import android.os.*
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.format.Time
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.aihtd.translatelanguage.adapter.ChatAdapter
import com.aihtd.translatelanguage.bean.AsrResultBean
import com.aihtd.translatelanguage.bean.MessageBean
import com.aihtd.translatelanguage.bean.RecogResult
import com.aihtd.translatelanguage.constant.ConstantInt
import com.aihtd.translatelanguage.constant.ConstantString
import com.aihtd.translatelanguage.constant.ConstantUrl
import com.aihtd.translatelanguage.soap.Soaputil
import com.aihtd.translatelanguage.utils.FileUtil
import com.aihtd.translatelanguage.widget.VoiceLeftView
import com.aihtd.translatelanguage.widget.VoiceRecorderView
import com.alibaba.fastjson.JSON
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.ksoap2.serialization.SoapPrimitive
import java.io.File
import java.util.*

/**
 * 所在包名：
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/4
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
class MainActivity : AppCompatActivity(), EventListener {

    private var msgId: Int = 0
    private var asr: EventManager? = null
    private val logTime = true
    private var enableOffline = false // 测试离线命令词，需要改成true
    private var chatAdapter: ChatAdapter? = null
    private var messageList = mutableListOf<MessageBean>()
    private var mSpeechSynthesizer: SpeechSynthesizer? = null
    private var voiceAnimation: AnimationDrawable? = null
    private var imageView: ImageView? = null
    private var messageBean: MessageBean? = null
    private var play: AudioTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initPermission()
        initAsrAndTts()
        initAdapter()
        btn_start.setOnTouchListener { v, event ->
            if (play?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                play?.stop()
            }
            mSpeechSynthesizer?.stop()
            start()
            voice_left_recorder.onPressToSpeakBtnTouch(v, event, object : VoiceLeftView.EaseVoiceRecorderCallback {
                override fun onVoiceRecordComplete() {
                    stop()
//                    asr?.send(SpeechConstant.ASR_STOP, null, null, 0, 0) //
                }

                override fun onVoiceRecordCancle() {
                    cancel()
                }

                override fun onVoiceRecordStart() {
                }

                override fun onVoiceRecordShowView(msg: String) {
                }

                override fun ondismissView() {
                }
            })
        }

        btn_stop.setOnTouchListener { v, event ->
            if (play?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                play?.stop()
            }
            mSpeechSynthesizer?.stop()
            voice_right_recorder.onPressToSpeakBtnTouch(v, event, object : VoiceRecorderView.EaseVoiceRecorderCallback {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onVoiceRecordComplete(voiceFilePath: String?, voiceTimeLength: Int) {
                    Logger.d("voiceFilePath-->$voiceFilePath")
                    if (FileUtil.notEmptyFile(voiceFilePath)) {
                        val fileConvertToByteArray = FileUtil.fileToBase64(File(voiceFilePath))
                        AsyncWASR().execute(
                            fileConvertToByteArray, ConstantUrl.AsrMethodName,
                            ConstantUrl.AsrSoapAction
                        )
                    } else {
                        Toast.makeText(this@MainActivity, "请说话", Toast.LENGTH_SHORT)
                    }

                }

                override fun onVoiceRecordCancle() {}

                override fun onVoiceRecordStart() {}

                override fun onVoiceRecordShowView(msg: String) {}

                override fun ondismissView() {}

            })
        }
    }


    /**
     * 百度语音识别合成
     */
    private fun initAsrAndTts() {
        asr = EventManagerFactory.create(this, "asr")
        asr?.registerListener(this)


        var appId = "16715574"
        var appKey = "0ez4pjCKtZgkQznQLr7Z09B5"
        var secretKey = "1Gg43T4MXnE7dw0S39PtLxjnlBpQeYew"

        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        mSpeechSynthesizer?.setContext(this) // this 是Context的之类，如Activity
        mSpeechSynthesizer?.setSpeechSynthesizerListener(object : SpeechSynthesizerListener {
            override fun onSynthesizeStart(s: String) {

            }

            override fun onSynthesizeDataArrived(s: String, bytes: ByteArray, i: Int) {
            }

            override fun onSynthesizeFinish(s: String) {

            }

            override fun onSpeechStart(s: String) {

            }

            override fun onSpeechProgressChanged(s: String, i: Int) {
            }

            override fun onSpeechFinish(s: String) {
//                stopVoicePlayAnimation()
            }

            override fun onError(s: String, speechError: SpeechError) {

            }
        })
        mSpeechSynthesizer?.setAppId(appId)
        mSpeechSynthesizer?.setApiKey(appKey, secretKey)
        mSpeechSynthesizer?.auth(TtsMode.ONLINE)  // 纯在线
        mSpeechSynthesizer?.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0") // 设置发声的人声音，在线生效
        mSpeechSynthesizer?.initTts(TtsMode.MIX) // 初始化离在线混合模式，如果只需要在线合成功能，使用 TtsMode.ONLINE
//        mSpeechSynthesizer.synthesize("我是新人，百度尝试一下.初始化离在线混合模式，如果只需要在线合成功能，使用自定义输出事件类")
//        mSpeechSynthesizer.speak("我是新人，百度尝试一下")
    }

    private fun initAdapter() {
        rv_chat.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this@MainActivity, messageList)
        rv_chat.adapter = chatAdapter

        chatAdapter?.setOnItemClickListener { position, imageView, messageBean ->
            if (messageBean.type == ConstantInt.LEFT) {
                leftVoice(imageView, messageBean)
            } else {
                rightVoice(imageView, messageBean)
            }
        }

    }


    /**
     * 汉转维
     */
    private inner class AsyncCallWS : AsyncTask<String, Void, SoapPrimitive>() {
        private var content: String? = null
        override fun doInBackground(vararg params: String?): SoapPrimitive {
            content = params[0].toString()
            val calculate = Soaputil.calculate(
                ConstantUrl.namespace,
                ConstantUrl.url,
                params[1],
                params[2],
                params[0]
            )
            return calculate
        }

        override fun onPreExecute() {}


        override fun onPostExecute(result: SoapPrimitive) {
            var res: String = result.toString()
            Logger.d("汉转维--》$res")

            val now = Time()
            now.setToNow()
            var fileName = "tts" + now.toString().substring(0, 15) + ".pcm"

            var messageBean =
                MessageBean(ConstantInt.LEFT, ++msgId, res, content, ConstantString.fileUrl + "/" + fileName, "")
            messageList.add(messageBean)
            chatAdapter?.notifyItemChanged(messageList.size - 1)
            rv_chat.scrollToPosition(messageList.size - 1)
            AsyncWTTS().execute(
                res, ConstantUrl.TtsMethodName,
                ConstantUrl.TtsSoapAction, fileName
            )
        }
    }

    /**
     * 维语TTS
     */
    private inner class AsyncWTTS : AsyncTask<String, Void, SoapPrimitive>() {
        private var content: String? = null
        private var fileName: String? = null
        override fun doInBackground(vararg params: String?): SoapPrimitive {
            content = params[0]
            fileName = params[3]
            val calculate = Soaputil.calculateTTs(
                ConstantUrl.namespace,
                ConstantUrl.url,
                params[1],
                params[2],
                params[0]
            )
//

            return calculate
        }

        override fun onPreExecute() {
        }


        override fun onPostExecute(result: SoapPrimitive) {
            var res: String = result.toString()
            var s = "$res"


            s = FileUtil.toUtf8(s)

            if(s.contains("PjwvUmVzcG9uc2VJbmZv")){
//                val indexOf = s.indexOf("PjwvUmVzcG9uc2VJbmZv")
//                s = s.substring(indexOf + 22, s.length)
            }else if(s.contains("PC9SZXNwb25zZUluZm8+")){
                val indexOf = s.indexOf("PC9SZXNwb25zZUluZm8+")
                s = s.substring(indexOf + 20, s.length)
            }

            var f = File(ConstantString.fileUrl)
            if (!f.exists()) {
                f.mkdirs()
            }
            Runnable {
                val base64ToFile = FileUtil.base64ToFile(s, ConstantString.fileUrl, fileName)
                FileUtil.play(base64ToFile)
            }.run()


        }
    }

    /**
     * 维语ASR
     */
    private inner class AsyncWASR : AsyncTask<Any?, Void?, SoapPrimitive?>() {
        override fun doInBackground(vararg params: Any?): SoapPrimitive? {

            val calculate = Soaputil.calculateAsr(
                ConstantUrl.namespace,
                ConstantUrl.url,
                params[1].toString(),
                params[2].toString(),
                params[0].toString()
            )
//

            return calculate
        }

        override fun onPreExecute() {
        }


        override fun onPostExecute(result: SoapPrimitive?) {
            var res: String = result.toString()
            Logger.d("维语ASR--》$res")
            AsyncWC().execute(
                res, ConstantUrl.WCMethodName,
                ConstantUrl.WCSoapAction
            )

        }
    }


    /**
     * 维语转汉语
     */
    private inner class AsyncWC : AsyncTask<String, Void, SoapPrimitive>() {
        private var content: String? = null
        override fun doInBackground(vararg params: String): SoapPrimitive {
            content = params[0].toString()

            val calculate = Soaputil.calculateWC(
                ConstantUrl.namespace,
                ConstantUrl.url,
                params[1],
                params[2],
                params[0]
            )
//

            return calculate
        }

        override fun onPreExecute() {
        }


        override fun onPostExecute(result: SoapPrimitive) {
            var res: String = result.toString()
            var messageBean = MessageBean(ConstantInt.RIGHT, ++msgId, content, res, "", "")
            messageList.add(messageBean)
            chatAdapter?.notifyItemChanged(messageList.size - 1)
            rv_chat.scrollToPosition(messageList.size - 1)
            mSpeechSynthesizer?.speak(res)
            Logger.d("维转汉--》" + res)
        }
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.

            }
        }
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toTypedArray(), 123)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }


    /**
     * 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
     * 基于SDK集成3.1 开始回调事件
     */
    override fun onEvent(name: String, params: String?, data: ByteArray?, offset: Int, length: Int) {
        var logTxt = "name: $name"
        Logger.d("onEvent--> $name")

        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :$params"
        }
        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) {

            val recogResult = RecogResult.parseJson(params)

            // 识别结果
            val results = recogResult.getResultsRecognition()
            when {
                recogResult.isFinalResult -> {
                    // 最终识别结果，长语音每一句话会回调一次
//                    Logger.d("我的结果最终--》$logTxt")
                    val parseObject = JSON.parseObject(params, AsrResultBean::class.java)
                    var content = parseObject.origin_result.result.word[0]
                    Logger.d("我的结果最终--》$content")
                    AsyncCallWS().execute(
                        content, ConstantUrl.CwMethodName,
                        ConstantUrl.CWSoapAction
                    )

                }
                recogResult.isPartialResult -> {
                    // 临时识别结果
                }
                recogResult.isNluResult -> {
                    // 语义理解结果
                }
            }
        } else if (name == SpeechConstant.CALLBACK_EVENT_ASR_VOLUME) run {
            val vol = RecogResult.parseVolumeJson(params)
            Logger.d("音量大小--》${vol.volume}")
            mOnVolumListener?.onItemClick(vol.volume)
        } else if (data != null) {
            logTxt += " ;data length=" + data.size
        }
//        Logger.d("我的结果--》$logTxt")
    }


    private fun start() {
        val params = LinkedHashMap<String, Any>()
        var event: String? = null
        event = SpeechConstant.ASR_START // 替换成测试的event

        if (enableOffline) {
            params[SpeechConstant.DECODER] = 2
        }
        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = true
        params[SpeechConstant.CALLBACK_EVENT_ASR_VOLUME] = true
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号

        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：搜索模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068); // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        AutoCheck(applicationContext, object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message = autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
                        Log.w("AutoCheckMessage", message);
                    }// 可以用下面一行替代，在logcat中查看代码
                }
            }
        }, enableOffline).checkAsr(params)
        var json: String? = null // 可以替换成自己的json
        json = JSONObject(params).toString() // 这里可以替换成你需要测试的json
        asr?.send(event, json, null, 0, 0)
    }

    /**
     * 点击停止按钮
     * 基于SDK集成4.1 发送停止事件
     */
    private fun stop() {

        asr?.send(SpeechConstant.ASR_STOP, null, null, 0, 0) //
    }

    private fun cancel() {
        asr?.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
    }


    private fun leftVoice(image: ImageView, message: MessageBean) {
        if (play?.playState == AudioTrack.PLAYSTATE_PLAYING) {
            play?.stop()
        }
        mSpeechSynthesizer?.stop()

        play = FileUtil.play(File(message.url))

    }


    private fun rightVoice(image: ImageView, message: MessageBean) {
        if (play?.playState == AudioTrack.PLAYSTATE_PLAYING) {
            play?.stop()
        }
        mSpeechSynthesizer?.stop()

        mSpeechSynthesizer?.speak(message.chContent)
    }


    //汉转维时，音量显示
    protected var mOnVolumListener: OnVolumClickListener? = null

    interface OnVolumClickListener {
        fun onItemClick(volumNumber: Int)
    }

    fun setOnVolumClickListener(OnVolumClickListener: OnVolumClickListener) {
        this.mOnVolumListener = OnVolumClickListener
    }
}
