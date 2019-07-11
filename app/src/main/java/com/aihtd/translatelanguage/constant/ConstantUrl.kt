package com.aihtd.translatelanguage.constant

/**
 * 所在包名：com.aihtd.translatelanguage
 * 描述：Url常量
 * 作者：Dabin
 * 创建时间：2019/7/4
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
interface ConstantUrl {
    companion object {

        //        汉语转维语
        //        String soapAction = "http://tr.xuxj.com/tr.aspx/TrCn2Uy";
        //        String methodName = "TrCn2Uy";
        //        String namespace = "http://tr.xuxj.com/tr.aspx";
        //        String url = "http://tr.xuxj.com:86/api.asmx";


        val namespace = "http://tr.xuxj.com/tr.aspx"
        val url = "http://tr.xuxj.com:86/api.asmx"


        //汉语转维语
        val CwMethodName = "TrCn2Uy"
        val CWSoapAction = "$namespace/$CwMethodName"


        //维语转汉语
        val WCMethodName = "TrUy2Cn"
        val WCSoapAction = "$namespace/$WCMethodName"


        //维语语音识别ASR
        val AsrMethodName = "UyghurAsr64"
        val AsrSoapAction = "$namespace/$AsrMethodName"

        //维语语音合成TTS
        val TtsMethodName = "UyghurTts"
        val TtsSoapAction = "$namespace/$TtsMethodName"
    }
}
