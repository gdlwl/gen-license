package com.example.license.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lwl
 */
public class CommonProperties {
    public static final String OS_NAME="os.name";
    public static final String WINDOS="windows";
    /**
     * 密钥库密码
     */
    public  static  String storePass ;
    /**
     * #证书subject
     */
    public  static  String subject ;
    /**
     * 证书生成路径
     */
    public  static  String licensePath ;

    static{
        InputStream in = CommonProperties.class.getResourceAsStream("/cmomon.properties");
        Properties commonProperties = new Properties() ;
        try {
            commonProperties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storePass = commonProperties.getProperty("storePass");
        subject = commonProperties.getProperty("subject");
        if(System.getProperty(OS_NAME).toLowerCase().startsWith(WINDOS)){
            licensePath = commonProperties.getProperty("windowsLicensePath");
        }else{
            licensePath = commonProperties.getProperty("linuxLicensePath");
        }

    }
}
