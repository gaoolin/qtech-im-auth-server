package com.qtech.im.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* 启动类 */
@SpringBootApplication
public class QtechImAuthServer {
    public static void main(String[] args) {
        SpringApplication.run(QtechImAuthServer.class, args);
        System.out.println("智能制造认证中心启动成功！");
    }
}
