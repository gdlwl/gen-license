package com.example.license.common;

import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author lwl
 */
public class WindowsServerInfos extends AbstractServerInfos {

    @Override
    protected List<InetAddress> getIpAddress() throws Exception {
        List result = null;
        //获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();
        if(inetAddresses != null && inetAddresses.size() > 0){
            result = inetAddresses.stream().map(x->x.getHostAddress()).distinct().map(String::toLowerCase).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    protected List getMacAddress() throws Exception {
        List result = null;
        //1. 获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();

        if(inetAddresses != null && inetAddresses.size() > 0){
            //2. 获取所有网络接口的Mac地址
            result = inetAddresses.stream().map(x->getMacByInetAddress(x)).distinct().collect(Collectors.toList());
        }
        return result;
    }

    @Override
    protected String getCPUSerial() throws Exception {
        //序列号
        String serialNumber = "";
        //使用WMIC获取CPU序列号
        Process process = Runtime.getRuntime().exec("wmic cpu get processorid");
        process.getOutputStream().close();
        Scanner scanner = new Scanner(process.getInputStream());
        if(scanner.hasNext()){
            scanner.next();
        }
        if(scanner.hasNext()){
            serialNumber = scanner.next().trim();
        }
        scanner.close();
        return serialNumber;
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        //序列号
        String serialNumber = "";

        //使用WMIC获取主板序列号
        Process process = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
        process.getOutputStream().close();
        Scanner scanner = new Scanner(process.getInputStream());

        if(scanner.hasNext()){
            scanner.next();
        }

        if(scanner.hasNext()){
            serialNumber = scanner.next().trim();
        }

        scanner.close();
        return serialNumber;
    }
}
