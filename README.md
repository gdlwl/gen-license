common 是公共属性模块

creator是证书生成模块

verify证书检验模块



1.生成公私钥

```shell
#生成DSA秘钥对命令
keytool -genkeypair -keysize 1024 -validity 3650 -alias privateKey -keystore privateKeys.store -storepass kinth123 -keypass kinth1234 -dname "CN=lwl, OU=kinth, O=kinth, L=GZ, ST=GD, C=CN"

#导出私钥命令
keytool -exportcert -alias privateKey -keystore privateKeys.store -storepass kinth123 -file certfile.cer

#导入公钥命令
keytool -import -alias publicCert -file certfile.cer -keystore publicCerts.store -storepass kinth123
```

