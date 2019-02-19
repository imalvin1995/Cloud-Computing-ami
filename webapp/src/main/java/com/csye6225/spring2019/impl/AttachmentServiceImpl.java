package com.csye6225.spring2019.impl;

import com.csye6225.spring2019.entity.Attachment;
import com.csye6225.spring2019.repository.AttachmentRepository;
import com.csye6225.spring2019.service.AttachmentService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public boolean addAttachmentsToNote(List<Attachment> list){
        if(list==null||list.isEmpty()) {
            log.warn("No attachments to add");
            return false;
        }
        for(Attachment a :list){
            if(Strings.isEmpty(a.getNoteId())){
                log.warn(String.format("No noteId for %s",a.getNoteId()));
                return false;
            }
            a.setId(UUID.randomUUID().toString());
        }
        int re = attachmentRepository.insertAttachments(list);
        return re>0;
    }

    @Override
    public List<Attachment> findAttachmentsByNoteId(String noteId){
        if(Strings.isEmpty(noteId)){
            log.warn("Empty note id");
        }
        return attachmentRepository.listAttachmentByNoteId(noteId);
    }

    @Override
    public boolean updateAttachment(Attachment attachment){
        if(attachment==null || Strings.isEmpty(attachment.getNoteId())
        ||Strings.isEmpty(attachment.getId())||Strings.isEmpty(attachment.getUrl())){
            return false;
        }
        int re = attachmentRepository.updateAttachment(attachment);
        return re>0;
    }

    @Override
    public Attachment getAttachmentById(String id){
        if(Strings.isEmpty(id)){
            log.warn("Empty id or note id");
            return null;
        }
        return attachmentRepository.getAttachmentById(id);
    }

    @Override
    public boolean deleteAttachmentById(String id){
        if(Strings.isEmpty(id)){
            log.warn("Empty id");
            return false;
        }
        int re = attachmentRepository.deleteAttachmentById(id);
        if(re==0){
            log.warn(String.format("Delete failed :%s! ",id));
            return false;
        }
        return true;

    }

    @Override
    public int deleteAttachmentByNoteId(String noteId){
        if(Strings.isEmpty(noteId)) return 0;
        return attachmentRepository.deleteAttachmentByNoteId(noteId);
    }

}
