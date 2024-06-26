package com.fox.controller;

import com.fox.entity.FileSystem;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.UUID;

@Controller
public class FileAction {


    //MultipartHttpServletRequest：是httpservletRequest的强化版本，不仅可以装文本信 息，还可以装图片文件信息
    @RequestMapping("upload")
    public @ResponseBody FileSystem upload(MultipartHttpServletRequest request) throws Exception {
        FileSystem fileSystem = new FileSystem();
        /* 1、把文件保存到web服务器*/
        // 从页面请求中，获取上传的文件对象
        MultipartFile file = request.getFile("fname");
        // 从文件对象中获取 文件的原始名称
        String oldFileName = file.getOriginalFilename();
        // 通过字符串截取的方式，从文件原始名中获取文件的后缀 1.jpg
        String hou = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
        // 为了避免文件因为同名而覆盖，生成全新的文件名
        String newFileName = UUID.randomUUID().toString() + "." + hou;
        // 创建web服务器保存文件的目录(预先创建好D:/upload目录，否则系统找不到路径，会抛异常)
        File toSaveFile = new File("E:/upload/" + newFileName);
        // 将路径转换成文件
        file.transferTo(toSaveFile);
        // 获取服务器的绝对路径
        String newFilePath = toSaveFile.getAbsolutePath();
        /* 2、把文件从web服务器上传到FastDFS*/
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
        NameValuePair[] list = new NameValuePair[1];
        list[0] = new NameValuePair("fileName", oldFileName);
        String fileId = client.upload_file1(newFilePath, hou, list);
        trackerServer.close();
        // 封装fileSystem数据对象
        fileSystem.setFileId(fileId);
        fileSystem.setFileName(oldFileName);
        fileSystem.setFilePath(fileId);
        //已经上传到FastDFS上，通过fileId来访问图 片，所以fileId即为文件路径
        return fileSystem;
    }

}
