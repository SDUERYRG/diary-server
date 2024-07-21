package com.yrg.springboot.utils;
import org.springframework.stereotype.Component;

/**
 * 生成6位数的邮件验证码
 *
 * @Author yrg
 * @Date 2024/7/19 15:53
 */
@Component
public class CodeUtils {
    public String generateCode(String email) { //生成6位随机加密验证码
        int hash = email.hashCode();
        long result = hash ^ 20221012;  //加密
        result = result ^ System.currentTimeMillis();
        String code = String.valueOf(result);
        return code.substring(code.length() - 6);
    }
}
