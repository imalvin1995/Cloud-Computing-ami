package com.csye6225.spring2019.service;

import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.impl.NoteServiceImpl;

import java.util.List;

public interface NoteService {

    Note addNewNote(Note note);
    List<Note> findAll(int userId);
    List<Note> findAll(int userId,String title);
    Note getNoteByNoteId (String id);
    boolean deleteNoteByNoteId (String id);
    boolean updateNote (Note note);
}
