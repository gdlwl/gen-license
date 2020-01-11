package com.example.license.core;

import com.example.license.utils.DateUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * License生成类需要的参数
 */
@Component
@ConfigurationProperties(prefix = "licensecreator")
public class LicenseCreatorParam{
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 密钥别称
     */
    private String privateAlias;

    /**
     * 密钥密码（需要妥善保管，不能让使用者知道）
     */
    private String keyPass;

    /**
     * 访问秘钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String privateKeysStorePath;

    /**
     * 证书生效时间
     */
    private Date issuedTime = new Date();
    /**
     * 证书生效时间字符串
     */
    private String issuedTimeStr;
    /**
     * 证书失效时间
     */
    private Date expiryTime;
    /**
     * 证书生效时间字符串
     */
    private String expiryTimeStr;
    /**
     * 用户类型
     */
    private String consumerType;

    /**
     * 用户数量
     */
    private Integer consumerAmount;

    /**
     * 描述信息
     */
    private String description;

    private Integer isBindMac;

    /**
     * 额外的服务器硬件校验信息
     */
    private LicenseCheckModel licenseCheckModel;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrivateAlias() {
        return privateAlias;
    }

    public void setPrivateAlias(String privateAlias) {
        this.privateAlias = privateAlias;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
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

    public String getPrivateKeysStorePath() {
        return privateKeysStorePath;
    }

    public void setPrivateKeysStorePath(String privateKeysStorePath) {
        this.privateKeysStorePath = privateKeysStorePath;
    }

    public Date getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public Integer getConsumerAmount() {
        return consumerAmount;
    }

    public void setConsumerAmount(Integer consumerAmount) {
        this.consumerAmount = consumerAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LicenseCheckModel getLicenseCheckModel() {
        return licenseCheckModel;
    }

    public void setLicenseCheckModel(LicenseCheckModel licenseCheckModel) {
        this.licenseCheckModel = licenseCheckModel;
    }

    public String getIssuedTimeStr() {
        return issuedTimeStr;
    }

    public void setIssuedTimeStr(String issuedTimeStr) {
        this.issuedTimeStr = issuedTimeStr;
        try {
            this.issuedTime = DateUtil.parse(issuedTimeStr,DateUtil.DateType.YMD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getExpiryTimeStr() {
        return expiryTimeStr;
    }

    public void setExpiryTimeStr(String expiryTimeStr) {
        this.expiryTimeStr = expiryTimeStr;
        try {
            this.expiryTime = DateUtil.parse(expiryTimeStr,DateUtil.DateType.YMD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "LicenseCreatorParam{" +
                "subject='" + subject + '\'' +
                ", privateAlias='" + privateAlias + '\'' +
                ", keyPass='" + keyPass + '\'' +
                ", storePass='" + storePass + '\'' +
                ", licensePath='" + licensePath + '\'' +
                ", privateKeysStorePath='" + privateKeysStorePath + '\'' +
                ", issuedTime=" + issuedTime +
                ", issuedTimeStr='" + issuedTimeStr + '\'' +
                ", expiryTime=" + expiryTime +
                ", expiryTimeStr='" + expiryTimeStr + '\'' +
                ", consumerType='" + consumerType + '\'' +
                ", consumerAmount=" + consumerAmount +
                ", description='" + description + '\'' +
                ", licenseCheckModel=" + licenseCheckModel +
                '}';
    }
}