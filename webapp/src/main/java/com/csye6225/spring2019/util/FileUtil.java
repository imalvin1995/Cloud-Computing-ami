package com.csye6225.spring2019.util;

import com.google.common.base.Splitter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.util.List;

@Log4j2
public class FileUtil {
    public static boolean createfolder(String folderPath){
        if(Strings.isEmpty(folderPath))
            return false;
        File file = new File(folderPath);
        boolean isSuccess = true;
        if(!file.exists()){
            isSuccess = file.mkdir();
        }
        return isSuccess;
    }

    public static String saveFileToLocal(InputStream input,String folderPath,String fileName,String suffix) throws IOException{
        if(input==null||Strings.isEmpty(folderPath)||Strings.isEmpty(fileName)||Strings.isEmpty(suffix)){
            log.warn("Invalid params");
            return null;
        }
        if(!createfolder(folderPath)){
            log.error("cannot create the folder with path :"+folderPath);
            return null;
        }

        String filePath = String.format("%s/%s.%s",folderPath,fileName,suffix);
        File outfile = new File(filePath);
        outfile.createNewFile();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(outfile));
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)){
            byte[] b = new byte[1000];
            int n;
            while ((n = input.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            byte[] buffer = bos.toByteArray();
            bf.write(buffer);
            bf.flush();
        }catch (IOException e){
            log.error(e);
            return null;
        }
        return filePath;
    }



    public static boolean deleteFileFromLocal(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            log.error("cannot find file with path :"+filePath);
            return false;
        }
        try {
            file.delete();
        }catch (Exception e){
            log.error("Cannot delete file "+filePath);
        }
        return true;
    }

}
