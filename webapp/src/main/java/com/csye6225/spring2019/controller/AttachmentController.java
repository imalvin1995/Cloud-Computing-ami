package com.csye6225.spring2019.controller;


import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Attachment;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.filter.Verifier;
import com.csye6225.spring2019.service.AttachmentService;
import com.csye6225.spring2019.service.NoteService;
import com.csye6225.spring2019.service.RegisterService;
import javafx.scene.control.TableView;
import jdk.internal.joptsimple.internal.Strings;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@RestController
public class AttachmentController {
    @Autowired
    RegisterService registerService;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    NoteService noteService;

    @GetMapping("/note/{id}/attachments")
    public Result<List<Attachment>> getAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(name = "id") String noteId) throws IOException {
        Result<List<Attachment>> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
        } else {
            String email = account.getEmailAddress();
            Account user = registerService.findByEmail(email);
            Note note = noteService.getNoteByNoteId(noteId);
            if (note == null) {
                res.setStatusCode(404);
                res.setMessage("Not Fount");
                httpServletResponse.setStatus(SC_NOT_FOUND);
                httpServletResponse.sendError(SC_NOT_FOUND, "Not Found");
            } else {
                if ((note.getUserId() != user.getId())) {
                    res.setStatusCode(401);
                    res.setMessage("Unauthorized");
                    httpServletResponse.setStatus(SC_UNAUTHORIZED);
                    httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                    return res;
                } else {
                    res.setData(attachmentService.findAttachmentsByNoteId(noteId));
                    res.setStatusCode(200);
                    res.setMessage("OK");
                }
            }
        }
        return res;
    }

    @PostMapping("/note/{id}/attachments")
    public Result<Attachment> postAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(name = "id") String noteId, File file) throws IOException {
        Result<Attachment> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
            return res;
        } else {
            if (noteService.getNoteByNoteId(noteId) == null) {
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST, "Bad Request");
                return res;
            }
            String userEmail = account.getEmailAddress();
            Account user = registerService.findByEmail(userEmail);
            if (user.getId() != noteService.getNoteByNoteId(noteId).getUserId()) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }
            List<Attachment> list = new LinkedList<>();
            /*
            URL url = file.toURI().toURL();
            Attachment attachment = new Attachment();
            attachment.setNoteId(noteId);
            attachment.setUrl(url);*/
        }
        return res;
    }

    @PutMapping("/note/{id}/attachments/{idAttachments}")
    public Result<Attachment> updateAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,@PathVariable(name = "id") String noteId, @PathVariable(name = "idAttachments") String idAttachment, File file) throws IOException{
        Result<Attachment> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
            return res;
        }else{
            if (noteService.getNoteByNoteId(noteId) == null) {
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST, "Bad Request");
                return res;
            }
            String userEmail = account.getEmailAddress();
            Account user = registerService.findByEmail(userEmail);
            if (user.getId() != noteService.getNoteByNoteId(noteId).getUserId()) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }
            Attachment attachment = new Attachment();
            /*
            URL url = file.toURL();
            attachment.setUrl(url);*/
            attachmentService.updateAttachment(attachment);
            httpServletResponse.setStatus(SC_NO_CONTENT);
            return res;
        }
    }
    @DeleteMapping("/note/{id}/attachments/{idAttachments}")
    public Result<String> deleteAttachments(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable(name = "id") String noteId, @PathVariable(name = "idAttachments") String idAttachment) throws IOException {
        Result<String> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
            return res;
        } else {
            if (noteService.getNoteByNoteId(noteId) == null) {
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST, "Bad Request");
                return res;
            }
            String userEmail = account.getEmailAddress();
            Account user = registerService.findByEmail(userEmail);
            if (user.getId() != noteService.getNoteByNoteId(noteId).getUserId()) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }

            if (Strings.isNullOrEmpty(idAttachment) || attachmentService.findAttachmentsByNoteId(idAttachment) == null) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }

            //attachmentService.deleteAttachmentById(idAttachment);
            httpServletResponse.setStatus(SC_NO_CONTENT);
        }
        return res;
    }


}
