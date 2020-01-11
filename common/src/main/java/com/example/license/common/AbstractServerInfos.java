package com.example.license.common;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author lwl
 */
public abstract class AbstractServerInfos {

    /**
     * 获取服务器配置信息
     * @return LicenseCheckModel
     */
    public LicenseCheckModel getServerInfos() throws Exception {
        LicenseCheckModel result = new LicenseCheckModel();

        try {
            result.setIpAddress(this.getIpAddress());
            result.setMacAddress(this.getMacAddress());
            result.setCpuSerial(this.getCPUSerial());
            result.setMainBoardSerial(this.getMainBoardSerial());
        }catch (Exception e){
            throw new Exception("获取服务器硬件信息失败");
        }

        return result;
    }

    /**
     * 获取IP地址
     * @return java.util.List
     */
    protected abstract List getIpAddress() throws Exception;

    /**
     * 获取Mac地址
     * @return java.util.List
     */
    protected abstract List getMacAddress() throws Exception;

    /**
     * 获取CPU序列号
     * @return java.lang.String
     */
    protected abstract String getCPUSerial() throws Exception;

    /**
     * 获取主板序列号
     * @return java.lang.String
     */
    protected abstract String getMainBoardSerial() throws Exception;

    /**
     * 获取当前服务器所有符合条件的InetAddress
     * @param
     * @return java.util.List
     */
    protected List<InetAddress> getLocalAllInetAddress() throws Exception {
        List result = new ArrayList(4);
        // 遍历所有的网络接口
        for (Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
            NetworkInterface iface = (NetworkInterface) networkInterfaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration inetAddresses = iface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                InetAddress inetAddr = (InetAddress) inetAddresses.nextElement();
                //排除LoopbackAddress,IPv4的loopback地址的范围是127.0.0.0 ~ 127.255.255.255
                // LinkLocalAddress,IPv4的本地连接地址的范围是169.254.0.0 ~ 169.254.255.255
                // MulticastAddress,IPv4的广播地址的范围是224.0.0.0 ~ 239.255.255.255
                //SiteLocalAddress,IPv4的地址本地地址分为三段：10.0.0.0 ~ 10.255.255.255、
                // 172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255。（企业内部或个人内部的局域网内部的ip都应该在此三个网段内）
                if(!inetAddr.isLoopbackAddress() && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()){
                    result.add(inetAddr);
                }
            }
        }

        return result;
    }

    /**
     * 获取某个网络接口的Mac地址
     * @param inetAddr
     * @return void
     */
    protected String getMacByInetAddress(InetAddress inetAddr){
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
            StringBuffer stringBuffer = new StringBuffer();

            for(int i=0;i<mac.length;i++){
                if(i != 0) {
                    stringBuffer.append("-");
                }

                //将十六进制byte转化为字符串
                String temp = Integer.toHexString(mac[i] & 0xff);
                if(temp.length() == 1){
                    stringBuffer.append("0" + temp);
                }else{
                    stringBuffer.append(temp);
                }
            }

            return stringBuffer.toString().toUpperCase();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}
