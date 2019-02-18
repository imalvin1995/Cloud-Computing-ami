package com.csye6225.spring2019.service;

import com.csye6225.spring2019.entity.Attachment;

import java.util.List;

public interface AttachmentService {
    boolean addAttachmentsToNote(List<Attachment> list);

    List<Attachment> findAttachmentsByNoteId(String id);

    boolean updateAttachment(Attachment attachment);

    Attachment getAttachmentById(String id);

    boolean deleteAttachmentById(String id);

    int deleteAttachmentByNoteId(String noteId);

}
