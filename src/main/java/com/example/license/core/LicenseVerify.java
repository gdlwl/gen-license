package com.example.license.core;

import com.example.license.utils.DateUtil;
import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.prefs.Preferences;
@Component
public class LicenseVerify implements InitializingBean {
    private static Logger logger = LogManager.getLogger(LicenseVerify.class);

    private LicenseManager licenseManager;

    @Autowired
    private LicenseVerifyParam licenseVerifyParam;

    public LicenseVerify(){
        //this.initLicenseVerify();
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    public void setLicenseManager(LicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }

    /**
     * 安装证书
     * @param licenseManager
     * @throws Exception
     */
    public void install(LicenseManager licenseManager){
        LicenseContent licenseContent = null;
        try {
            //读取证书
            File licenseFile = new File(licenseVerifyParam.getLicensePath());
            //安装证书
            licenseContent = licenseManager.install(licenseFile);
        } catch (FileNotFoundException e) {
            logger.info(MessageFormat.format("目录[{0}]不存在授权证书，请先执行授权生成证书",licenseVerifyParam.getLicensePath()));
            return;
        } catch (Exception e) {
            logger.info(MessageFormat.format("证书安装失败:\r\n{0}",e.getMessage()));
            e.printStackTrace();
            return;
        }
        logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",DateUtil.format(licenseContent.getNotBefore(), DateUtil.DateType.YMDHMS),
                DateUtil.format(licenseContent.getNotAfter(),DateUtil.DateType.YMDHMS)));
        this.verify();
    }
    /**
     * 校验License证书
     * @return boolean
     */
    public boolean verify(){
        // 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            logger.info("证书内容开始==========");
            logger.info(licenseContent);
            logger.info("证书内容终止==========");
            logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",DateUtil.format(licenseContent.getNotBefore(), DateUtil.DateType.YMDHMS),
                    DateUtil.format(licenseContent.getNotAfter(),DateUtil.DateType.YMDHMS)));
            return true;
        }catch (Exception e){
            logger.error("证书校验失败！",e);
            return false;
        }
    }

    /**
     * 初始化证书验证组件
     * @throws Exception
     */
    private void initLicenseVerify(){
        licenseManager = new CustomLicenseManager(this.initLicenseParam(licenseVerifyParam));
        this.install(licenseManager);
    }
    /**
     * 初始化证书生成参数
     * @param param License校验类需要的参数
     * @return de.schlichtherle.license.LicenseParam
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param){
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new DefaultKeyStoreParam(LicenseVerify.class
                ,param.getPublicKeysStorePath()
                ,param.getPublicAlias()
                ,param.getStorePass()
                ,null);

        return new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,publicStoreParam
                ,cipherParam);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initLicenseVerify();
    }
}
