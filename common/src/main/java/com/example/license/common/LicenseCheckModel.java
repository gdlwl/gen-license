package com.example.license.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwl
 */
public class LicenseCheckModel implements Serializable {


    private static final long serialVersionUID = -374845454317886315L;
    /**
     * 可被允许的IP地址
     */
    private List ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private List macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    /**
     * 是否绑定机器
     */
    private Integer isBindMac;


    public List getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(List ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(List macAddress) {
        this.macAddress = macAddress;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getMainBoardSerial() {
        return mainBoardSerial;
    }

    public void setMainBoardSerial(String mainBoardSerial) {
        this.mainBoardSerial = mainBoardSerial;
    }

    public Integer getIsBindMac() {
        return isBindMac;
    }

    public void setIsBindMac(Integer isBindMac) {
        this.isBindMac = isBindMac;
    }

    @Override
    public String toString() {
        return "LicenseCheckModel{" +
                "ipAddress=" + ipAddress +
                ", macAddress=" + macAddress +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", mainBoardSerial='" + mainBoardSerial + '\'' +
                ", isBindMac=" + isBindMac +
                '}';
    }
}
