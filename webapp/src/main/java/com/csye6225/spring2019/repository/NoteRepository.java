package com.csye6225.spring2019.repository;

import com.csye6225.spring2019.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoteRepository {

    Note getNoteByNoteId(@Param("id")String id);

    List<Note> listNoteByUserIdAndTitle(@Param("userId")int userId,@Param("title")String title);

    int updateNoteTitleAndContentById(Note note);

    int deleteNoteById(@Param("id")String id);

    int insertNewNote(Note note);
}
