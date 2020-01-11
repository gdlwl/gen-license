package com.example.license.verify.core;

import com.example.license.common.LicenseCheckModel;
import com.example.license.common.ServerInfosFactory;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author lwl
 *
 */
public class VerifyLicenseManager extends LicenseManager {
    private static Log log = LogFactory.getLog(VerifyLicenseManager.class);
    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    public VerifyLicenseManager() {
    }

    public VerifyLicenseManager(LicenseParam licenseParam) {
        super(licenseParam);
    }

    /**
     * TODO 复写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     * @param
     * @return de.schlichtherle.license.LicenseContent
     */
    @Override
    protected synchronized LicenseContent install(
            final byte[] key,
            final LicenseNotary notary)
            throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
        this.validate(content);
        setLicenseKey(key);
        setCertificate(certificate);
        return content;
    }

    /**
     * TODO 重写XMLDecoder解析XML
     * @param encoded XML类型字符串
     * @return Object
     */
    private Object load(String encoded){
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));
            decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE),null,null);
            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(decoder != null){
                    decoder.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error("XMLDecoder解析XML失败",e);
            }
        }
        return null;
    }
    /**
     * TODO 增加Mac地址等其他信息校验
     * @param content LicenseContent
     */
    @Override
    protected synchronized void validate(final LicenseContent content)
            throws LicenseContentException {
        LicenseCheckModel expectedCheckModel = null;
        LicenseCheckModel serverCheckModel = null;
        String errMsg = null;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        if( content.getNotAfter().before(calendar.getTime()) ){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd yyyy-MM-dd HH:mm:ss");
            errMsg = MessageFormat.format("证书不合法或已失效!,证书有效期为{0} - {1}",
                    simpleDateFormat.format(content.getNotBefore()),simpleDateFormat.format(content.getNotAfter()));
            log.error(errMsg);
        }
        //1. 首先调用父类的validate方法
        super.validate(content);
        //2. 然后校验自定义的License参数
        expectedCheckModel = (LicenseCheckModel) content.getExtra();
        //当前服务器真实的参数信息
        try {
            serverCheckModel = ServerInfosFactory.getServerInfos(System.getProperty("os.name").toLowerCase()).getServerInfos();
        } catch (Exception e) {
            throw new LicenseContentException("不能获取服务器硬件信息!");
        }
			
        if(expectedCheckModel != null&& serverCheckModel != null){
			//如果绑定机器，则校验服务器信息
			if(expectedCheckModel.getIsBindMac()==1){
				//校验Mac地址
				if(!checkIpAddress(expectedCheckModel.getMacAddress(),serverCheckModel.getMacAddress())){
					throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
				}
				//校验主板序列号
				if(!checkSerial(expectedCheckModel.getMainBoardSerial(),serverCheckModel.getMainBoardSerial())){
					throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
				}

				//校验CPU序列号
				if(!checkSerial(expectedCheckModel.getCpuSerial(),serverCheckModel.getCpuSerial())){
					throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
				}				
			}

        }else{
            throw new LicenseContentException("不能获取服务器硬件信息!");
        }
    }
    /**
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内
     * 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
     * @return boolean
     */
    private boolean checkIpAddress(List<String> expectedList, List serverList){
        if(expectedList != null && expectedList.size() > 0){
            if(serverList != null && serverList.size() > 0){
                for(String expected : expectedList){
                    if(serverList.contains(expected.trim())){
                        return true;
                    }
                }
            }
            return false;
        }else {
            return true;
        }
    }

    /**
     * TODO 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
     * @return boolean
     */
    private boolean checkSerial(String expectedSerial,String serverSerial){
        if(expectedSerial!=null&&expectedSerial.trim().length()>0){
            if(serverSerial!=null&&serverSerial.trim().length()>0){
                if(expectedSerial.equals(serverSerial)){
                    return true;
                }
            }
            return false;
        }else{
            return true;
        }
    }

}
