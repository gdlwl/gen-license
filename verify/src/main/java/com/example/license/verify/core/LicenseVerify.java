package com.example.license.verify.core;

import com.example.license.verify.util.TomcatUtil;
import de.schlichtherle.license.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * @author lwl
 */
public class LicenseVerify{
    private static Log logger = LogFactory.getLog(LicenseVerify.class);

    private LicenseManager licenseManager;

    private static LicenseVerifyParam licenseVerifyParam = LicenseVerifyParam.getInstanse();

    public LicenseVerify(){
        licenseManager = new VerifyLicenseManager(this.initLicenseParam(licenseVerifyParam));
        try {
            this.install(licenseManager);
        } catch (Exception e) {
            logger.error("证书安装失败!");
            TomcatUtil.killTomcat();
        }
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }


    /**
     * TODO 安装证书
     * @param licenseManager
     * @throws Exception
     */
    public void install(LicenseManager licenseManager) throws Exception {
        LicenseContent licenseContent = null;
        logger.info("开始安装证书");
        try {
            //读取证书
            File licenseFile = new File(licenseVerifyParam.getLicensePath());
            //安装证书
            licenseContent = licenseManager.install(licenseFile);
        } catch (FileNotFoundException e) {
            logger.error(MessageFormat.format("目录[{0}]不存在授权证书，请先执行授权生成证书",licenseVerifyParam.getLicensePath()));
            throw e;
        } catch (Exception e) {
            logger.error(MessageFormat.format("证书安装失败:\r\n{0}",e.getMessage()));
            throw e;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",simpleDateFormat.format(licenseContent.getNotBefore()),
                simpleDateFormat.format(licenseContent.getNotAfter())));
    }
    /**
     * TODO 初始化证书生成参数
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
}
