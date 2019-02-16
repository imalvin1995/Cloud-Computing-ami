package com.csye6225.spring2019.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Note {
    private String id;
    private int userId;
    private String title;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private List<Attachment> attachments;

}
