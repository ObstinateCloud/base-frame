package com.lenged.system.minio;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * @title: FileUploader
 * @description: minio文件上传
 * @auther: zhangjianyun
 * @date: 2022/8/10 9:38
 */
public class FileUploader {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, MinioException {

        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient("http://192.168.20.207:9000", "minioadmin", "minioadmin");

        // 1.检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists("lengedyun");
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket("lengedyun");
        }

       //2.查看桶文件对象
//        Iterable<Result<Item>> applogs = minioClient.listObjects("applogs");
//        Iterator<Result<Item>> iterator = applogs.iterator();
//        int count = 1;
//        while (iterator.hasNext()) {
//            count++;
//            Result<Item> next = iterator.next();
//            Item item = next.get();
//            System.out.print(item.objectName() + ",");
//            System.out.print(item.lastModified() + ",");
//            System.out.print(item.size() + ",");
//            System.out.print(item.owner().id() + ",");
//            System.out.println(item.etag());
//        }
//        System.out.println(count);

        // 3.使用putObject上传一个文件到存储桶中。
       // PutObjectOptions putObjectOptions = new PutObjectOptions(102546653,-1);
      //minioClient.putObject("lengedyun","jar/taobao-hsf.sar-dev-SNAPSHOT.jar", "D://taobao-hsf.sar-dev-SNAPSHOT.jar", putObjectOptions);

        // 4.下载文件对象
       // minioClient.getObject("lengedyun", "taobao-hsf.sar-dev-SNAPSHOT.jar","aaa.jar");

        // 5.删除文档
        minioClient.removeObject("lengedyun","asiaphotos.zip");
    }

}
