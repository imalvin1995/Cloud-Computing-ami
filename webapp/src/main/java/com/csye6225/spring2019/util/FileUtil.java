package com.csye6225.spring2019.util;

import com.google.common.base.Splitter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.util.List;

@Log4j2
public class FileUtil {
    private static void createfolder(String folderPath) throws IOException {
        if(Strings.isEmpty(folderPath))
            return;
        File file = new File(folderPath);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public static String saveFileToLocal(File file,String folderPath){
        if(file==null||Strings.isEmpty(folderPath)){
            log.warn("Invalid params");
            return null;
        }
        String fileName = file.getName();
        if(Strings.isEmpty(fileName)){
            log.warn("Cannot get file without a name");
            return null;
        }
        List<String> list = Splitter.on(".").trimResults().splitToList(fileName);
        if(list.size()!=2){
            log.warn(String.format("Cannot get the name and suffix :%s",fileName));
            return null;
        }
        String filePath = String.format("%s/%s-%d.%s",folderPath,list.get(0),System.currentTimeMillis(),list.get(1));
        File outfile = new File(filePath);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            BufferedWriter bf = new BufferedWriter(new FileWriter(outfile))){
            createfolder(folderPath);
            outfile.createNewFile();
            String i;
            while ((i=reader.readLine())!=null){
                bf.write(i);
            }
        }catch (IOException e){
            log.error(e);
            return null;
        }
        return filePath;
    }

}
