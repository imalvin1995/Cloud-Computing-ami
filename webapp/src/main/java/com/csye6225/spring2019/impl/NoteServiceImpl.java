package com.csye6225.spring2019.impl;


import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.repository.NoteRepository;
import com.csye6225.spring2019.service.NoteService;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class NoteServiceImpl implements NoteService{

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note addNewNote(Note note){
        if(note==null||note.getUserId()<=0|| Strings.isNullOrEmpty(note.getTitle())){
            log.warn("Lacking data for add a new note");
            return null;
        }
        note.setCreateTime(new Timestamp(System.currentTimeMillis()));
        note.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        note.setId(UUID.randomUUID().toString());
        int re = noteRepository.insertNewNote(note);
        return re>0?note:null;
    }


    @Override
    public List<Note> findAll(int userId,String title) {
        if (userId<=0) {
            log.warn("No  user id found");
            return null;
        }
        List<Note> list=noteRepository.listNoteByUserIdAndTitle(userId, title);
        return list;
    }

    @Override
    public List<Note> findAll(int userId){
        return findAll(userId,null);
    }


    @Override
    public Note getNoteByNoteId(String id) {
        if(Strings.isNullOrEmpty(id) )
            return null;
        Note note = noteRepository.getNoteByNoteId(id);

        return note;
    }

    @Override
    public boolean deleteNoteByNoteId( String id) {
        if(Strings.isNullOrEmpty(id))
            return false;

        int re = noteRepository.deleteNoteById(id);
        return re>0;
    }

    @Override
    public boolean updateNote(Note note) {
        if(Strings.isNullOrEmpty(note.getId()))
            return false;
        //Note note=noteRepository.getNoteByNoteId(id);
        note.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int re = noteRepository.updateNoteTitleAndContentById(note);
        return re>0;
    }

}
