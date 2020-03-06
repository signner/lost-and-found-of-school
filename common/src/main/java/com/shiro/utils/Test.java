package com.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * DATE: 2019/10/3 22:47
 * USER: create by 申水根
 */
public class Test {
    //    public static String getSignatureForPOST(Map<String, String> paramMap) {
//        StringBuffer stringBuffer = new StringBuffer();
//        for (Map.Entry<String, String> entry : paramMap.entrySet())
//            stringBuffer.append((String) entry.getKey() + "=" + (String) entry.getValue() + "&");
//        return stringBuffer.toString().substring(0, stringBuffer.length() - 1);
//    }
//
//    public static void main(String[] args) {
//        TreeMap hashMap = new TreeMap();
//        hashMap.put("userId", "99ac92");
//        hashMap.put("guid", "87e592e9785e25a2");
//        hashMap.put("contentId", "36660");
//        hashMap.put("programCode", "03380ed9356c4d40828a4daaa7ef27d6");
//        hashMap.put("resolution", "H265_8M_4K,8M");
//
//        System.out.println(getSignatureForPOST(hashMap));
//
//        String hashAlgorithmName = "md5"; // 加密方式
//        Object crdentials = getSignatureForPOST(hashMap); // 密码原值
////        Object salt = ByteSource.Util.bytes(salt1); // 盐值
//        Object result = new SimpleHash(hashAlgorithmName, crdentials);
//        System.out.println(result);
//    }
//
//}
    private static String cw(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("&salt=");
        stringBuilder.append("290e0d6629511568");
        String str = stringBuilder.toString();
        return str;
    }

    public static String cx(String paramString) {
        StringBuffer stringBuffer = new StringBuffer();
        if (paramString.contains("?")) {
            String[] arrayOfString = paramString.substring(paramString.indexOf("?") + 1).split("&");
            Arrays.sort(arrayOfString);
            for (byte b = 0; b < arrayOfString.length; b++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(arrayOfString[b]);
                stringBuilder.append("&");
                stringBuffer.append(stringBuilder.toString());
            }
            String str = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
            return cw(str);
        }
        paramString = null;
        return cw(paramString);
    }

    public static String k(Map<String, String> paramMap) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append((String) entry.getValue());
            stringBuilder.append("&");
            stringBuffer.append(stringBuilder.toString());
        }
        return cw(stringBuffer.toString().substring(0, stringBuffer.length() - 1));
    }

    public static void main(String[] args) {

        TreeMap hashMap = new TreeMap();
        hashMap.put("userId", "99ac92");
        hashMap.put("guid", "87e592e9785e25a2");
        hashMap.put("contentId", "36660");
        hashMap.put("programCode", "03380ed9356c4d40828a4daaa7ef27d6");
        hashMap.put("resolution", "H265_8M_4K,8M");
        System.out.println(k(hashMap));
        String hashAlgorithmName = "md5"; // 加密方式
        Object crdentials = k(hashMap); // 密码原值
        int hashIterations = 2; // 加密2次
        Object salt = ByteSource.Util.bytes("290e0d6629511568"); // 盐值
        Object result = new SimpleHash(hashAlgorithmName, crdentials);
        System.out.println(result);
        System.out.println("----------------------------------------------");
        Object result1 = new SimpleHash(hashAlgorithmName, crdentials, salt);
        System.out.println(result1);
        System.out.println("----------------------------------------------");
        crdentials = k(hashMap).replace("&salt=290e0d6629511568", "");
        System.out.println(crdentials);
        Object result2 = new SimpleHash(hashAlgorithmName, crdentials, salt);
        System.out.println(result2);
    }
}


