package com.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * DATE: 2019/9/11 20:24
 * USER: create by shen
 */
public class PasswordUtil {
    /**
     * md5 加密
     *
     * @param pass
     * @return
     */
    public static String convertMD5(String pass, String salt1) {
        String hashAlgorithmName = "md5";
        Object crdentials = pass;
        Object salt = ByteSource.Util.bytes(salt1);
        int hashIterations = 2;
        Object result = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
        return String.valueOf(result);
    }

    public static void main(String[] args){
        System.out.println(convertMD5("shen.0011","2014-02-28"));
    }

}
