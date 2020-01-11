package com.example.license.common;

/**
 * @author lwl
 */
public class ServerInfosFactory {
    private static AbstractServerInfos abstractServerInfos;
    public static AbstractServerInfos getServerInfos(String osName){
        if(abstractServerInfos == null){
            abstractServerInfos = null;
            //根据不同操作系统类型选择不同的数据获取方法
            if (osName.startsWith(CommonProperties.WINDOS)) {
                abstractServerInfos = new WindowsServerInfos();
            }else{//其他服务器类型
                abstractServerInfos = new LinuxServerInfos();
            }
        }
        return abstractServerInfos;
    }
}
