package com.csye6225.spring2019.facade.impl;

import com.csye6225.spring2019.entity.Attachment;
import com.csye6225.spring2019.facade.NoteFacadeService;
import com.csye6225.spring2019.service.AttachmentService;
import com.csye6225.spring2019.service.NoteService;
import com.csye6225.spring2019.util.FileUtil;
import com.csye6225.spring2019.util.S3Util;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

@Service
@Log4j2
public class NoteFacadeServiceImpl implements NoteFacadeService {
    @Autowired
    private NoteService noteService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private Environment env;

    @Override
    public void deleteNote(String noteId){
        if(Strings.isEmpty(noteId)) return ;
        List<Attachment> attachments = attachmentService.findAttachmentsByNoteId(noteId);
        attachmentService.deleteAttachmentById(noteId);
        noteService.deleteNoteByNoteId(noteId);
        if(attachments==null||attachments.isEmpty()){
            return ;
        }
        attachments.stream().filter(x->Strings.isNotEmpty(x.getUrl())).forEach((x)->{
            if(!isRunLocal() && isAWSURL(x.getUrl())){
                String keyName = x.getUrl().substring(x.getUrl().indexOf("/"));
                S3Util.deleteFile(env.getProperty("csye6225.aws.bucket.name"),keyName);
            }else if(!isAWSURL(x.getUrl()) && isRunLocal()){
                FileUtil.deleteFileFromLocal(x.getUrl());
            }else {
                log.info("Cannot delete file due to the env"+x.getUrl() );
            }

        });
    }

    private boolean isRunLocal(){
        String pro = env.getProperty("csye6225.save.file.type");
        return Strings.isNotEmpty(pro) && pro.equals("local");
    }

    private boolean isAWSURL(String url){
        if(Strings.isEmpty(url)) return false;
        String awsSuffix = env.getProperty("csye6225.aws.url.suffix");
        return url.contains(awsSuffix);
    }

    @Override
    public void deleteAttachment(String attachmentId){
        if(Strings.isEmpty(attachmentId)) return;
        Attachment attachment  = attachmentService.getAttachmentById(attachmentId);
        if(attachment == null || Strings.isEmpty(attachment.getUrl())) return;
        String url = attachment.getUrl();
        deleteFileByUrl(url);
        attachmentService.deleteAttachmentById(attachmentId);
    }


    private void deleteFileByUrl(String url){

        String bucket = env.getProperty("csye6225.aws.bucket.name");
        if(isAWSURL(url) &&!isRunLocal()){
            String path = url.substring(url.indexOf(bucket)+bucket.length()+1);
            S3Util.deleteFile(bucket,path);
        }else if(!isAWSURL(url) && isRunLocal()){
            FileUtil.deleteFileFromLocal(url);
        }else {
            log.warn("Cannot delete file with url because of the wrong env : "+url);
        }
    }

    @Override
    public void updateAttachment(InputStream input, String fileName,String fileType,double fileSize,String attId){
        if(input==null || Strings.isEmpty(fileName)||Strings.isEmpty(fileType)||Strings.isEmpty(attId)) return;
        Attachment attachment = attachmentService.getAttachmentById(attId);
        if(attachment == null ) {
            log.warn("Cannot find attachment by id :" +attId);
        }
        String url;
        if(isRunLocal()){
            try {
                url = FileUtil.saveFileToLocal(input, env.getProperty("csye6225.file.folder"), fileName, fileType);
            }catch (IOException e){
                log.error(e);
                return ;
            }
        }else {
            url = S3Util.uploadFile(env.getProperty("csye6225.aws.bucket.name"),env.getProperty("csye6225.file.folder")
                    ,input,env.getProperty("csye6225.aws.url.suffix"),fileName,fileType);
        }
        if(url ==null){
            return ;
        }
        deleteFileByUrl(attachment.getUrl());
        attachment.setFileType(fileType);
        attachment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        attachment.setFileSize(fileSize);
        attachment.setUrl(url);
        attachment.setFileName(fileName);
        attachmentService.updateAttachment(attachment);
    }
}
