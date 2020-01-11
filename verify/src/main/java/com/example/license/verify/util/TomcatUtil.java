package com.example.license.verify.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class TomcatUtil {
    private static Log log = LogFactory.getLog(TomcatUtil.class);

    /**
     * TODO 关闭tomcat
     */
    public static  void killTomcat() {
        StringBuffer sbCmd = new StringBuffer();
        try {
            MBeanServer server = null;
            if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
                server = MBeanServerFactory.findMBeanServer(null).get(0);
            }
            Set names = server.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
            Iterator iterator = names.iterator();
            ObjectName name = null;
            while (iterator.hasNext()) {
                //先清空命令
                sbCmd.setLength(0);
                name = (ObjectName) iterator.next();
                //String protocol = server.getAttribute(name, "protocol").toString();
                //String scheme = server.getAttribute(name, "scheme").toString();
                String port = server.getAttribute(name, "port").toString();
                //kill tomcat进程
                killTomcatCmdByPort(sbCmd,port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Runtime.getRuntime().exit(0);
        }
    }

    /**
     * TODO runtime执行脚本
     * @param commands
     * @throws IOException
     */
    private static void  callCommand(String[] commands) throws IOException {
        //返回与当前的Java应用相关的运行时对象
        Runtime runtime = Runtime.getRuntime();
        //指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例
        log.error(commands);
        Process process = runtime.exec(commands);
    }

    /**
     * TODO 获取根据tomcat 启动端口,关闭tomcat的命令
     * @param sb
     * @param port
     * @throws IOException
     */
    private static void killTomcatCmdByPort(StringBuffer sb,String port) throws IOException {
        String osName = System.getProperty("os.name");
        //windows系统
        if(osName.toLowerCase().startsWith("windows")){
            sb.append("for /f \"tokens=5\" %a in ('netstat -ano ^| findstr ")
                    .append(port)
                    .append("') do taskkill /f /pid %a");
            String[] cmds = new String[]{"cmd","/c",sb.toString()};
            callCommand(cmds);

        }else{
            //sb.append("/bin/sh -c kill -9 `fuser -v -n tcp ").append(port).append("`");
            sb.append("kill -9 `lsof -t -i:").append(port).append("`");
            String[] cmds = new String[]{"/bin/sh", "-c",sb.toString()};
            callCommand(cmds);
        }
    }

}
