package com.csye6225.spring2019.repository;

import com.csye6225.spring2019.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AttachmentRepository {
    int insertAttachments(@Param("list") List<Attachment> list);

    int updateAttachment(@Param("item")Attachment attachment);

    int deleteAttachmentById(@Param("id")String id);

    List<Attachment> listAttachmentByNoteId(@Param("noteId")String noteId);

    Attachment getAttachmentById(@Param("id")String id);

}
