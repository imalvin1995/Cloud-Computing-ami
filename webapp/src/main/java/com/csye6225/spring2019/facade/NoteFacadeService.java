package com.csye6225.spring2019.facade;

import com.csye6225.spring2019.entity.Attachment;

import java.io.InputStream;

public interface NoteFacadeService {
    void deleteNote(String noteId);

    void updateAttachment(InputStream input, String fileName,String fileType,double fileSize,String attId);

    void deleteAttachment(String attachmentId);
}
