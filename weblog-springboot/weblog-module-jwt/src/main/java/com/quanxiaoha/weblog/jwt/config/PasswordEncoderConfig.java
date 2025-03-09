package com.quanxiaoha.weblog.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author: 木萨·塔布提
 * @url: blog.arnasoft.site
 * @date: 2023-08-24 9:17
 * @description: 密码加密
 **/
@Component
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用NoOpPasswordEncoder，不对密码进行加密，直接使用明文密码
        return NoOpPasswordEncoder.getInstance();
    }

    public static void main(String[] args) {
        // 不再需要加密测试
        System.out.println("使用明文密码，无需加密");
    }
}
