package com.csye6225.spring2019.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
//@Table(name = "user")

public class Account {

    private int id;

    private String emailAddress;

    private String pwdString;


}
