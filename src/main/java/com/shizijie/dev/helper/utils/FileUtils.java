package com.shizijie.dev.helper.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shizijie
 * @version 2019-11-12 下午7:39
 */
@Slf4j
public class FileUtils {
    public static void downloadBatchByFile(HttpServletResponse response, Path dir, String zipName){
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
            response.setContentType("application/x-msdownload");
            response.setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode(zipName, "utf-8"));
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(zos);
            for(Path path : stream){
                //每个文件名
                String fileName =path.getFileName().toString();
                //这个文件的字节
                byte[] file = Files.readAllBytes(path);
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));
                int len = 0;
                byte[] buf = new byte[10 * 1024];
                while( (len=bis.read(buf, 0, buf.length)) != -1){
                    bos.write(buf, 0, len);
                }
                bis.close();
                bos.flush();
            }
            bos.close();
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public static void createFile(String path,String fileName,byte[] content){
        Path out= Paths.get(path, fileName);
        if(!Files.exists(out)){
            try {
                Files.createFile(out);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        try {
            Files.write(out,content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    public static void deleteFile(File file) {
        //判断路径是否存在
        if(file.exists()) {
            //boolean isFile():测试此抽象路径名表示的文件是否是一个标准文件。
            if(file.isFile()){
                file.delete();
            }
            //不是文件，对于文件夹的操作
            else{
                //保存 路径D:/1/新建文件夹2  下的所有的文件和文件夹到listFiles数组中
                //listFiles方法：返回file路径下所有文件和文件夹的绝对路径
                File[] listFiles = file.listFiles();
                for (File file2 : listFiles) {
                    /*
                     * 递归作用：由外到内先一层一层删除里面的文件 再从最内层 反过来删除文件夹
                     *    注意：此时的文件夹在上一步的操作之后，里面的文件内容已全部删除
                     *         所以每一层的文件夹都是空的  ==》最后就可以直接删除了
                     */
                    deleteFile(file2);
                }
            }
            file.delete();
        }
    }
}
