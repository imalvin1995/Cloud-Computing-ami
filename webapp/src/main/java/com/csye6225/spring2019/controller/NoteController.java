package com.csye6225.spring2019.controller;

import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.filter.Verifier;
import com.csye6225.spring2019.service.NoteService;
import com.csye6225.spring2019.service.RegisterService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csye6225.spring2019.repository.UserRepository;
import com.csye6225.spring2019.service.NoteService;
import com.csye6225.spring2019.service.RegisterService;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import java.util.ArrayList;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@RestController
public class NoteController {

    @Autowired
    NoteService noteService;
    @Autowired
    RegisterService registerService;


    @GetMapping("/note")
    public Result<List<Note>> getAllNote(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
        Result<List<Note>> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if(account == null || !registerService.checkAccount(account)){
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
        }
        else{
            account = registerService.findByEmail(account.getEmailAddress());
            int id = account.getId();
            res.setData(noteService.findAll(id));
            res.setStatusCode(200);
            res.setMessage("OK");
        }
        return res;
    }

    @PostMapping("/note")
    public Result<Note> postNote(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody Note note) throws IOException{
        Result<Note> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if(account == null || !registerService.checkAccount(account)){
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
            return res;
        }
        else{
            if(Strings.isNullOrEmpty(note.getContent()) || Strings.isNullOrEmpty(note.getTitle())){
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST,"Bad Request");
                return res;
            }
           // httpServletResponse.se(SC_CREATED,"created");
            account = registerService.findByEmail(account.getEmailAddress());
            note.setUserId(account.getId());
            note = noteService.addNewNote(note);
            if(note == null){
                httpServletResponse.sendError(SC_INTERNAL_SERVER_ERROR,"Unexpected error");
                return res;
            }
//            httpServletResponse.setStatus(SC_CREATED);*/
            httpServletResponse.setStatus(SC_CREATED);
            //httpServletResponse.sendError(SC_CREATED,"Created");
            res.setData(note);
            res.setStatusCode(201);
            res.setMessage("created");
        }
        return res;
    }


    @GetMapping("/note/{id}")
    public Result<Note> getCertainNote(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(name = "id") String noteId) throws IOException{
        Result<Note> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if(account == null || !registerService.checkAccount(account)){
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
        }
        else{
            String email = account.getEmailAddress();
            Account user = registerService.findByEmail(email);
            Note note = noteService.getNoteByNoteId(noteId);
            if(note == null){
                res.setStatusCode(404);
                res.setMessage("Not Fount");
                httpServletResponse.setStatus(SC_NOT_FOUND);
                httpServletResponse.sendError(SC_NOT_FOUND,"Not Found");
            }
            else{
                if((note.getUserId() != user.getId())){
                    res.setStatusCode(401);
                    res.setMessage("Unauthorized");
                    httpServletResponse.setStatus(SC_UNAUTHORIZED);
                    httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
                    return res;
                }else{
                    res.setData(noteService.getNoteByNoteId(noteId));
                    res.setStatusCode(200);
                    res.setMessage("OK");
                }
            }
        }
        return res;
    }

    @PutMapping("/note/{id}")
    public Result<Note> putCertainNote(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(name = "id") String noteId, @RequestBody Note note) throws IOException{
        Result<Note> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if(account == null || !registerService.checkAccount(account)){
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
        }
        else{
            String content = note.getContent();
            String title = note.getTitle();
            Note userNote = noteService.getNoteByNoteId(noteId);
            if(userNote == null || Strings.isNullOrEmpty(note.getTitle()) || Strings.isNullOrEmpty(note.getContent())){
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST,"Bad Request");
                return res;
            }
            else {
                userNote.setContent(content);
                userNote.setTitle(title);
                String email = account.getEmailAddress();
                Account user = registerService.findByEmail(email);
                if ((userNote.getUserId() != user.getId())) {
                    httpServletResponse.setStatus(SC_UNAUTHORIZED);
                    httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                } else {
                    httpServletResponse.setStatus(SC_NO_CONTENT);
                    noteService.updateNote(userNote);
                    res.setData(userNote);
                    res.setMessage("OK");
                }
            }
        }
        return res;
    }

    @DeleteMapping("/note/{id}")
    public Result<String> deleteCertainNote(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(name = "id") String noteId) throws IOException {
        Result<String> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if(account == null || !registerService.checkAccount(account)){
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
        }
        else{
            String email = account.getEmailAddress();
            Account user = registerService.findByEmail(email);
            Note note = noteService.getNoteByNoteId(noteId);
            if(note == null){
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST,"Bad Request");
                return res;
            }
            else {
                if ((note.getUserId() != user.getId())) {
                    httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                } else {
                    noteService.deleteNoteByNoteId(noteId);
                    httpServletResponse.setStatus(SC_NO_CONTENT);
                    res.setMessage("successfully deleted");
                }
            }
        }
        return res;
    }
}
