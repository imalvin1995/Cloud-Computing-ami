package com.csye6225.spring2019.controller;


import com.amazonaws.services.s3.model.Bucket;
import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Attachment;
import com.csye6225.spring2019.entity.Note;
import com.csye6225.spring2019.filter.Verifier;
import com.csye6225.spring2019.service.AttachmentService;
import com.csye6225.spring2019.service.NoteService;
import com.csye6225.spring2019.service.RegisterService;
import com.csye6225.spring2019.util.FileUtil;
import com.csye6225.spring2019.util.S3Util;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;


@RestController
public class AttachmentController {
    @Autowired
    RegisterService registerService;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    NoteService noteService;

    @Autowired
    Environment environment;

    @GetMapping("/note/{noteId}/attachments")
    public Result<List<Attachment>> getAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable(value = "noteId") String noteId) throws IOException {
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
    public Result<Attachment> postAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("id") String noteId, @RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        // Basic Auth;
        Result<Attachment> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        // Exception test
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
            return res;
        } else {
            // check noteId with user
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
            // create Attachment;
            List<Attachment> list = new LinkedList<>();
            Attachment attachment = new Attachment();
            attachment.setNoteId(noteId);
            attachment.setFileName(multipartFile.getName());
            attachment.setFileSize(multipartFile.getSize());
            String[] name = multipartFile.getName().split("\\.");
            String fileType = name[name.length - 1];
            attachment.setFileType(fileType);
            //transfer  multipart file to file
            String fileName = multipartFile.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            final File file = File.createTempFile(UUID.randomUUID().toString(),prefix);
            multipartFile.transferTo(file);
            // local file
            if (environment.getProperty("csye6225.save.file.type").equals("local")) {
                String url = FileUtil.saveFileToLocal(file, environment.getProperty("csye6225.file.folder"));
                attachment.setUrl(url);
            }
            // aws file
            else if (environment.getProperty("csye6225.save.file.type").equals("aws")) {
                //Bucket b = S3Util.getBucket(environment.getProperty("csye6225.aws.bucket.name"));
                String url = S3Util.uploadFile(environment.getProperty("csye6225.aws.bucket.name"), environment.getProperty("csye6225.file.folder"), file, environment.getProperty("csye6225.aws.url.suffix"));
                attachment.setUrl(url);
            }

            // add File to DB;
            list.add(attachment);
            attachmentService.addAttachmentsToNote(list);
            // front end
            res.setData(attachment);
            res.setMessage("OK");
            res.setStatusCode(200);
            //file.delete();
        }
        return res;
    }

    @PutMapping("/note/{id}/attachments/{idAttachments}")
    public Result<Attachment> updateAttachments(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("id") String noteId, @PathVariable("idAttachments") String idAttachment, File file) throws IOException {
        // Basic Auth
        Result<Attachment> res = new Result<>();
        String auth = httpServletRequest.getHeader("Authorization");
        Account account = Verifier.isVerified(auth);
        // check account
        if (account == null || !registerService.checkAccount(account)) {
            httpServletResponse.setStatus(SC_UNAUTHORIZED);
            httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
            return res;
        } else {
            // check NoteId exist
            if (noteService.getNoteByNoteId(noteId) == null) {
                httpServletResponse.setStatus(SC_BAD_REQUEST);
                httpServletResponse.sendError(SC_BAD_REQUEST, "Bad Request");
                return res;
            }
            // check Note id belong to user id
            String userEmail = account.getEmailAddress();
            Account user = registerService.findByEmail(userEmail);
            if (user.getId() != noteService.getNoteByNoteId(noteId).getUserId()) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }

            // check Attachment id exist
            Attachment oldAttachment = attachmentService.getAttachmentById(idAttachment);
            if(oldAttachment == null){
                httpServletResponse.setStatus(SC_NOT_FOUND);
                httpServletResponse.sendError(SC_NOT_FOUND,"Not Found");
                return res;
            }

            if(noteService.getNoteByNoteId(noteId).getAttachments()!= oldAttachment){
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED,"Unauthorized");
                return res;
            }

            // put Attachment
            Attachment attachment = new Attachment();
            attachment.setNoteId(noteId);
            attachment.setFileName(file.getName());
            attachment.setFileSize(file.length());
            String[] name = file.getName().split("\\.");
            String fileType = name[name.length - 1];
            attachment.setFileType(fileType);
            // local file
            if (environment.getProperty("csye6225.save.file.type").equals("local")) {
                String url = FileUtil.saveFileToLocal(file, environment.getProperty("csye6225.file.folder"));
                attachment.setUrl(url);
            }
            // aws file
            else if (environment.getProperty("csye6225.save.file.type").equals("aws")) {
                //Bucket b = S3Util.getBucket(environment.getProperty("csye6225.aws.bucket.name"));
                String url = S3Util.uploadFile(environment.getProperty("csye6225.aws.bucket.name"), environment.getProperty("csye6225.file.folder"), file, environment.getProperty("csye6225.aws.url.suffix"));
                attachment.setUrl(url);
            }
            attachmentService.updateAttachment(attachment);
            httpServletResponse.setStatus(SC_NO_CONTENT);
            return res;
        }
    }

    @DeleteMapping("/note/{id}/attachments/{idAttachments}")
    public Result<String> deleteAttachments(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("id") String noteId, @PathVariable("idAttachments") String idAttachment) throws IOException {
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
            Attachment attachment = attachmentService.getAttachmentById(idAttachment);
            if (Strings.isNullOrEmpty(idAttachment) || noteService.getNoteByNoteId(noteId).getAttachments() != attachment) {
                httpServletResponse.setStatus(SC_UNAUTHORIZED);
                httpServletResponse.sendError(SC_UNAUTHORIZED, "Unauthorized");
                return res;
            }

            attachmentService.deleteAttachmentById(idAttachment);
            httpServletResponse.setStatus(SC_NO_CONTENT);
        }
        return res;
    }


}
