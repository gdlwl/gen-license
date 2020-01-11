package com.example.license.creator.controller;

import com.example.license.common.LicenseCheckModel;
import com.example.license.common.ServerInfosFactory;
import com.example.license.creator.core.LicenseCreator;
import com.example.license.creator.core.LicenseCreatorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lwl
 */
@Controller
@RequestMapping("/license")
public class LicenseController {
    @Autowired
    public  LicenseCreatorParam licenseCreatorParam;

    @RequestMapping("/index")
    public String goLicense(){
        return "license/licenseIndex.html";
    }
    /**
     * 生成证书
     * @param licenseParam
     * @return java.util.Map
     *
     */
    @RequestMapping(value = "/generateLicense")
    @ResponseBody
    public Map generateLicense(LicenseCreatorParam licenseParam, HttpServletResponse response) {
        Map resultMap = new HashMap();
        licenseCreatorParam.setIssuedTimeStr(licenseParam.getIssuedTimeStr());
        licenseCreatorParam.setExpiryTimeStr(licenseParam.getExpiryTimeStr());
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            setLicenseCheckModelByOsName(osName, licenseParam.getLicenseCheckModel().getIsBindMac());
        } catch (Exception e) {
            //获取服务器硬件信息失败
            resultMap.put("success",0);
            resultMap.put("msg",e.getMessage());
            return  resultMap;
        }
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorParam);
        try {
            licenseCreator.generateLicense();
        } catch (Exception e) {
            resultMap.put("success",0);
            resultMap.put("msg", MessageFormat.format("证书文件生成失败！{0}",e.getMessage()));
            return  resultMap;
        }
        resultMap.put("success",1);
        resultMap.put("msg","证书生成成功");
        return resultMap;
    }

    private void setLicenseCheckModelByOsName(String osName,Integer isBindMac) throws Exception {
        LicenseCheckModel licenseCheckModel = ServerInfosFactory.getServerInfos(osName).getServerInfos();
        licenseCheckModel.setIsBindMac(isBindMac);
        licenseCreatorParam.setLicenseCheckModel(licenseCheckModel);
    }
}
