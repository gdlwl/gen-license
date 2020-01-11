package com.example.license.verify.core;

import com.example.license.common.CommonProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LicenseVerifyParam {
    private static  LicenseVerifyParam instance = new LicenseVerifyParam();
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    private LicenseVerifyParam() {
        InputStream in = LicenseVerifyParam.class.getResourceAsStream("/license-verify.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.subject = CommonProperties.subject;
        this.storePass = CommonProperties.storePass;
        this.licensePath = CommonProperties.licensePath;
        this.publicAlias = properties.getProperty("publicAlias");
        this.publicKeysStorePath = properties.getProperty("publicKeysStorePath");
    }
    public static LicenseVerifyParam getInstanse(){
        return instance;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublicAlias() {
        return publicAlias;
    }

    public void setPublicAlias(String publicAlias) {
        this.publicAlias = publicAlias;
    }

    public String getStorePass() {
        return storePass;
    }

    public void setStorePass(String storePass) {
        this.storePass = storePass;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getPublicKeysStorePath() {
        return publicKeysStorePath;
    }

    public void setPublicKeysStorePath(String publicKeysStorePath) {
        this.publicKeysStorePath = publicKeysStorePath;
    }

    @Override
    public String toString() {
        return "LicenseVerifyParam{" +
                "subject='" + subject + '\'' +
                ", publicAlias='" + publicAlias + '\'' +
                ", storePass='" + storePass + '\'' +
                ", licensePath='" + licensePath + '\'' +
                ", publicKeysStorePath='" + publicKeysStorePath + '\'' +
                '}';
    }
}
