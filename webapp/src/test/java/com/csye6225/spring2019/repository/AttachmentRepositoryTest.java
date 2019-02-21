package com.csye6225.spring2019.repository;

import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Attachment;
import com.csye6225.spring2019.entity.Note;
import org.apache.ibatis.annotations.Param;
import org.assertj.core.util.Lists;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttachmentRepositoryTest {
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
//    int updateAttachment(@Param("item")Attachment attachment);
//
//    int deleteAttachmentById(@Param("id")String id);
//
//    List<Attachment> listAttachmentByNoteId(@Param("noteId")String noteId);
//
//    Attachment getAttachmentById(@Param("id")String id);
    Note note = null;
    Account account = null;
    @Before
    public void before(){
        account = new Account();
        account.setEmailAddress("unittest");
        account.setPwdString("unittest");
        userRepository.insertAccount(account);
        note = new Note();
        note.setId(UUID.randomUUID().toString());
        note.setTitle("unit test");
        note.setContent("Unit Test");
        note.setUserId(account.getId());
        noteRepository.insertNewNote(note);
    }
    @After
    public void after(){
//        attachmentRepository.de
    }
    @Test
    public void testInsertAttachments(){
        Attachment attachment= new Attachment();
        attachment.setId(UUID.randomUUID().toString());
        attachment.setNoteId(note.getId());
        attachment.setUrl("/unittest");
        attachment.setFileName("unittest");
        attachment.setFileType("test");
        attachment.setFileSize(100);
        int re = attachmentRepository.insertAttachments(Lists.newArrayList(attachment));
        Assert.assertTrue(re>0);
    }
    @Test
    public void testListAttachmentByNoteId(){
        List<Attachment> list = attachmentRepository.listAttachmentByNoteId(note.getId());

    }
    @Test
    public void testUpdateAttachment(){

    }
}
