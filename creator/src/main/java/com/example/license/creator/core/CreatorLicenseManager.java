package com.example.license.creator.core;

import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;

import java.util.Date;

/**
 * @author lwl
 */
public class CreatorLicenseManager extends LicenseManager {

    public CreatorLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * TODO 复写create方法
     * @param content 证书内容
     * @param notary LicenseNotary
     * @return byte[]
     * @throws Exception 创建证书异常
     */
    @Override
    protected synchronized byte[] create(
            LicenseContent content,
            LicenseNotary notary)
            throws Exception {
        initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * TODO 校验生成证书的参数信息
     * @param content 证书正文
     */
    private synchronized void validateCreate(final LicenseContent content)
            throws Exception {
        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)){
            throw new LicenseContentException("证书失效时间必须大于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)){
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType){
            throw new LicenseContentException("用户类型不能为空");
        }
    }


}