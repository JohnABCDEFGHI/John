package com.gyh.Hamburger.controller;


import com.gyh.Hamburger.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
/*
* 文件的上传与下载
* */
@RestController
@RequestMapping("/common")

public class CommonController {

    /**
     * @param file
     * @return
     */

    @Value("${reggie.path}")
    private String filePath;



    @PostMapping("/upload")
    //与前端参数名保持一致
    public Result<String> upload(MultipartFile file){
//  file 是临时文件 需要转存 需要保存一下
//        log.info(file.toString());
//        动态转存文件位置 不能写死 可以写配置文件
//          原始文件名 不推荐 因为不同用户可能会覆盖
        String originalFilename = file.getOriginalFilename();
//使用UUID 重新生成文件名 防止文件名字相同会覆盖
        //把后缀截出来
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString();

        File dir = new File(filePath);
        if(!dir.exists())
            dir.mkdirs();
        try {
            file.transferTo(new File(filePath+newFilename+suffix ));
        } catch (IOException e) {
            e.getMessage();
        }
        return Result.success(newFilename+suffix);
    }


    @GetMapping("/download")
    //与前端参数名保持一致
    public void download(String name, HttpServletResponse response){
//        输入流 读取文件内容 输出流 将文件写回浏览器

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath+name));
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            int len= 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes) )!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
