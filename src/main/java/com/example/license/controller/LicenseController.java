package com.example.license.controller;

import com.example.license.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/license")
public class LicenseController {
    @Autowired
    public LicenseCreatorParam licenseCreatorParam;
    @RequestMapping("/index")
    public String goLicense(){
        return "/license/licenseIndex.html";
    }
    /**
     * 生成证书
     * @param licenseParam 生成证书需要的参数，如：{"subject":"kinth-models","privateAlias":"privateKey","keyPass":"5T7Zz5Y0dJFcqTxvzkH5LDGJJSGMzQ","storePass":"3538cef8e7","licensePath":"D:/license/license.lic","privateKeysStorePath":"C:/Users/zifangsky/Desktop/privateKeys.keystore","issuedTime":"2018-04-26 14:48:12","expiryTime":"2018-12-31 00:00:00","consumerType":"User","consumerAmount":1,"description":"这是证书描述信息","licenseCheckModel":{"ipAddress":["192.168.20.66"],"macAddress":["08-71-90-73-05-B4"],"cpuSerial":"BB","mainBoardSerial":"CC"}}
     * @return java.util.Map
     *
     */
    @RequestMapping(value = "/generateLicense")
    @ResponseBody
    public Map generateLicense(LicenseCreatorParam licenseParam, HttpServletResponse response) {
        Map resultMap = new HashMap();
        licenseCreatorParam.setIssuedTimeStr(licenseParam.getIssuedTimeStr());
        licenseCreatorParam.setExpiryTimeStr(licenseParam.getExpiryTimeStr());
        setLicenseCheckModelByOsName(System.getProperty("os.name").toLowerCase());
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorParam);
        boolean result = licenseCreator.generateLicense();
        if(result){
            resultMap.put("success",1);
            resultMap.put("msg","证书生成成功");
        }else{
            resultMap.put("success",0);
            resultMap.put("msg","证书文件生成失败！");
        }
        return resultMap;
    }

    private void setLicenseCheckModelByOsName(String osName){
        AbstractServerInfos abstractServerInfos = null;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        }else{//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        LicenseCheckModel licenseCheckModel = abstractServerInfos.getServerInfos();
        licenseCreatorParam.setLicenseCheckModel(licenseCheckModel);
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorParam);
    }
}
