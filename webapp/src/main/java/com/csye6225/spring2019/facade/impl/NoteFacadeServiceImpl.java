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
            }else if(!isAWSURL(x.getUrl())){
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
}
