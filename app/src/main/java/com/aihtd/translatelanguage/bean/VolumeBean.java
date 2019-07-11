package com.aihtd.translatelanguage.bean;

/**
 * 所在包名：com.aihtd.translatelanguage.bean
 * 描述：
 * 作者：Dabin
 * 创建时间：2019/7/9
 * 修改人：
 * 修改时间：
 * 修改描述：
 */
public class VolumeBean {
    public int volumePercent = -1;
    public int volume = -1;
    public String origalJson;


    public int getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(int volumePercent) {
        this.volumePercent = volumePercent;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getOrigalJson() {
        return origalJson;
    }

    public void setOrigalJson(String origalJson) {
        this.origalJson = origalJson;
    }
}
