package com.shiro.utils;

import com.shiro.myEnum.UPFILEPATH;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * DATE: 2019/9/5 12:42
 * USER: create by 申水根
 */
public class FileUploadUtil {


    public static String up(MultipartFile multipartFile, UPFILEPATH upfilepath, String urlPath, String local)throws IOException {
        String find = "find/";
        String lost = "lost/";
        String avatar = "avatar/";
        switch (upfilepath) {
            case find:
                local += find;
                urlPath += find;
                break;
            case lost:
                local += lost;
                urlPath += lost;
                break;
            case avatar:
                local += avatar;
                urlPath += avatar;
                break;
            default:
                return null;
        }

            ArrayList<String> list = FileUploadUtil.getFile(local, 0);
            String name = multipartFile.getOriginalFilename();
            name = FileUploadUtil.checkFileName(list, name, 0);

            File file = new File(local, name);
            System.out.println("上传到本地的路径:"+local + name);
            //临时文件转换成我的文件，临时文件转换完成后会删除
            multipartFile.transferTo(file);
            System.out.println("浏览器访问的路径:"+urlPath + name);
            return urlPath + name;

    }

    public static void del(String urlPath, String local, String online) {
        String file = "";
        file = local + urlPath.replace(online, "");
        File fl = new File(file);
        System.out.println("删除的文件:"+file);
        fl.delete();
    }

    /**
     * @param path 需要遍历的路径
     * @return 路径下文件的名称集合
     */
    private static ArrayList<String> getFile(String path, int deep) {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        ArrayList<String> list = new ArrayList<String>();
        int n = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile())//如果是文件
            {
                for (int j = 0; j < deep; j++)//输出前置空格
                    System.out.print(" ");
                // 只输出文件名字
                list.add(array[i].getName());
            }
        }
        return list;
    }

    /**
     * @param names 文件下文件名的集合
     * @param name  存入的文件名
     * @param index 索引的开始位置
     * @return 符合要求的文件名
     */
    private static String checkFileName(ArrayList<String> names, String name, int index) {
        if (names.contains(name.substring(0, name.indexOf(".")) + "(" + index + ")" + name.substring(name.indexOf("."), name.length()))) {
            name = FileUploadUtil.checkFileName(names, name, index + 1);
        } else {
            if (index == 0)
                name = FileUploadUtil.checkFileName(names, name, index + 1);
            else
                return name.substring(0, name.indexOf(".")) + "(" + index + ")" + name.substring(name.indexOf("."), name.length());
        }
        return name;
    }

//    public static void main(String[] args) {
//        ArrayList<String> list = getFile("D:\\phpstudy\\nginx\\html\\temp\\avatar", 0);
//        String filename = "unnamed.jpg";
//        filename = checkFileName(list, filename, 0);
//        System.out.println(filename);
//    }

}
