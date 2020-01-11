package com.example.license.controller;

import com.example.license.core.LicenseVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(value="login")
public class LoginController {
    @Autowired
    private LicenseVerify licenseVerify;
    @RequestMapping(value="doLogin")
    public Map login(){
        Map result = new HashMap();
        boolean success = licenseVerify.verify();
        if(success){
            result.put("msg","验证通过");
            return  result;
        }
        return  result;
    }
}
