package com.example.license.creator.controller;


import com.example.license.verify.core.LicenseVerify;
import com.example.license.verify.util.TomcatUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lwl
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static Log logger = LogFactory.getLog(LoginController.class);
    @RequestMapping("/verify")
    public Map verifyLicense(){
        HashMap<String,Object> result = new HashMap<>();
        try {
            //安装证书
            LicenseVerify licenseVerify =new LicenseVerify();
            //校验证书
            licenseVerify.getLicenseManager().verify();
        } catch (Exception e) {
            logger.error(e.getMessage());
            TomcatUtil.killTomcat();
        }
        return result;
    }

}
