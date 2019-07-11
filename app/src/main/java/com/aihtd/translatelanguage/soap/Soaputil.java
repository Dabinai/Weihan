package com.aihtd.translatelanguage.soap;

import android.util.Log;
import com.aihtd.translatelanguage.constant.ConstantInt;
import com.aihtd.translatelanguage.constant.ConstantString;
import com.orhanobut.logger.Logger;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * 所在包名：sxy.com.soapandroiddemo
 * 描述：Soap请求
 * 作者：Dabin
 * 创建时间：2019/7/4
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class Soaputil {
    /**
     * 汉转维
     *
     * @param namespace
     * @param url
     * @param methodName
     * @param soapAction
     * @param content
     * @return
     */
    public static SoapPrimitive calculate(String namespace, String url, String methodName, String soapAction, String content) {
        SoapPrimitive resultString = null;
        try {
            //创建soapObject,即拼装soap bodyin
            SoapObject Request = new SoapObject(namespace, methodName);
            //添加传入参数，根据具体格式测试
            Request.addProperty("CnStr", content);
            Request.addProperty("OpenID", ConstantInt.Companion.getOPENID());
            //创建soap 数据
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(url);
            //soap 协议发送
            transport.call(soapAction, soapEnvelope);
            //soap 请求完成后返回数据并转换成字符串
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
        } catch (Exception ex) {
            Logger.d(ConstantString.Companion.getSOAPTAG(), "请求结果出错--》: " + ex.getMessage());
        }

        return resultString;
    }

    /**
     * 维语合成TTS
     *
     * @param namespace
     * @param url
     * @param methodName
     * @param soapAction
     * @param content
     * @return
     */
    public static SoapPrimitive calculateTTs(String namespace, String url, String methodName, String soapAction, String content) {
        SoapObject resultString;
        SoapPrimitive resultStri = null;
        try {
            SoapObject Request = new SoapObject(namespace, methodName);
            Request.addProperty("StrUyghur", content);
            Request.addProperty("OpenID", ConstantInt.Companion.getOPENID());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(url);
            transport.call(soapAction, soapEnvelope);
            resultString = (SoapObject) soapEnvelope.bodyIn;
            resultStri = (SoapPrimitive) resultString.getProperty("UyghurTtsResult");

        } catch (Exception ex) {
            Log.e("请求结果出错", ex.toString());
        }

//        return resultString;
        return resultStri;
    }

    /**
     * 维语识别ASr
     *
     * @param namespace
     * @param url
     * @param url
     * @param methodName
     * @param soapAction
     * @param content
     * @return
     */
    public static SoapPrimitive calculateAsr(String namespace, String url, String methodName, String soapAction, String content) {
        SoapPrimitive resultString = null;
        try {
            SoapObject Request = new SoapObject(namespace, methodName);
            Request.addProperty("WavData64", content);
            Request.addProperty("OpenID", ConstantInt.Companion.getOPENID());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
//            new MarshalFloat().register(soapEnvelope); //添加这一句就可以解决float 型数据序列化问题
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(url);
            transport.call(soapAction, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
        } catch (Exception ex) {
            Log.e("请求结果出错", ex.toString());
        }

        return resultString;
    }

    /**
     * 维语转汉语
     *
     * @param namespace
     * @param url
     * @param methodName
     * @param soapAction
     * @param content
     * @return
     */
    public static SoapPrimitive calculateWC(String namespace, String url, String methodName, String soapAction, String content) {
        SoapPrimitive resultString = null;
        try {
            SoapObject Request = new SoapObject(namespace, methodName);
            Request.addProperty("UyStr", content);
            Request.addProperty("OpenID", ConstantInt.Companion.getOPENID());
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(url);
            transport.call(soapAction, soapEnvelope);
            //soap 请求完成后返回数据并转换成字符串
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
        } catch (Exception ex) {
            Log.e("请求结果出错", ex.toString());
        }

        return resultString;
    }

}
