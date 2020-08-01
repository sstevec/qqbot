package com.botdemo.demo1.service;

import com.botdemo.demo1.mapper.SentenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
public class LocalFileSystemImp implements LocalFileSystem{

    @Autowired
    SentenceMapper sentenceMapper;

    public void loadLocalFile(){
        String filePath1 = "D:/pcrData/smallFile";
        File file = new File(filePath1);
        // get the folder list
        File[] array = file.listFiles();

        if(array != null){
            for (File value : array) {
                String name = value.getName();
                String path = filePath1 + "/" + name;
                String data = fileToBase64(path);
                if(data.length() < 20000){
                    sentenceMapper.storeFileCode(data,name);
                    value.delete();
                }else{
                    System.out.println(name + " is too large it has length " + data.length());
                }

            }
        }
    }

    // 只适合小文件
    public String fileToBase64(String path){
        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(path);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    // 0 for picture, 1 for voice
    public String generateCQForLocalFile(int type){
        String filePath1 = "D:/pcrData/pictureData";
        String filePathUrl ="D:\\pcrData\\pictureData\\";

        String filePath2 = "D:/pcrData/voiceData";
        String filePathUrl2 ="D:\\pcrData\\voiceData\\";

        File file;
        if(type == 0){
            // picture
            file = new File(filePath1);
        }else{
            file = new File(filePath2);
        }

        // get the folder list
        File[] array = file.listFiles();

        String result = "";
        if(array != null){
            Random random = new Random();
            int num = random.nextInt(array.length);

            if(type == 0){
                // picture
                result += "[CQ:image,file=file:///" + filePathUrl + array[num].getName()+"] ";
            }else{
                result += "[CQ:record,file=file:///" + filePathUrl2 + array[num].getName()+"] ";
            }

        }
        return result;

    }
}
