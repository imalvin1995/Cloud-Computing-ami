package com.csye6225.spring2019.repository;


import com.amazonaws.services.s3.model.Bucket;
import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.util.S3Util;
import com.google.common.base.Strings;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class NoteRepositoryTest {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    private int userId=0;
    @Test
    public void testInsertNewNote(){
        List<Account> accounts = userRepository.findAll();
        if(accounts==null || accounts.isEmpty()) {
            System.out.println("No user found");
            return;
        }
        userId = accounts.get(0).getId();
        Note note = new Note();
//        note.setId();
        note.setUserId(userId);
        note.setTitle("sdd");
        note.setContent("dsss");
        int re = noteRepository.insertNewNote(note);
        Assert.assertTrue(re==1);
        Assert.assertFalse(Strings.isNullOrEmpty(note.getId()));
    }

    @Test
    public void testListNoteByUserIdAndTitle(){
//        testInsertNewNote();
        List<Note> notes = noteRepository.listNoteByUserIdAndTitle(3,"");
        Assert.assertTrue(notes!=null && !notes
        .isEmpty());
    }

    @Test
    public void test(){
        Bucket b = S3Util.getBucket("new-bucke");
        System.out.println(b.getName());
    }
    
}
