package com.example.license.creator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.io.IOException;
@Configuration
public class IndexConfig {
    @Autowired
    private Environment environment;
    @EventListener({ApplicationReadyEvent.class})
    void applicationReadyEvent() {
        System.out.println("应用已经准备就绪 ...");
        String port = environment.getProperty("local.server.port");
        String url = "http://localhost:"+port;
        System.out.println("启动浏览器:打开"+url);
        Runtime runtime = Runtime.getRuntime();
        String osName = System.getProperty("os.name").toLowerCase();
        String[] cmds = new String[3];
        //如果是windows系统，直接打开浏览器
        if(osName.startsWith("windows")){
            cmds[0] = "cmd";
            cmds[1] = "/c";
            cmds[2] = "start "+url;
            try {
                runtime.exec(cmds);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
