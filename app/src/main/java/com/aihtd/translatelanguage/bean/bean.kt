package com.aihtd.translatelanguage.bean

import android.widget.ImageView
import org.ksoap2.serialization.SoapPrimitive
import java.io.Serializable

/**
 * 所在包名：com.aihtd.translatelanguage.bean
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/5
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
data class MessageBean(
    var type: Int,
    var msgId: Int,
    var weiContent: String?,
    var chContent: String?,
    var url: String?,
    var msgTime: String

) :Serializable


data class AsrBean(
    val best_result: String,
    val error: Int,
    val origin_result: OriginResult,
    val result_type: String,
    val results_recognition: List<String>
)

data class OriginResult(
    val corpus_no: Long,
    val err_no: Int,
    val result: Result,
    val sn: String,
    val voice_energy: Double
)

data class Result(
    val word: List<String>
)

data class EventMessage(
    val messageBean: MessageBean,
    val imageView: ImageView
)

data class VolumBean(
    var volume :Int
)
