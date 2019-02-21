package com.csye6225.spring2019.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result<T> {
    public T data;
    public int statusCode;
    public String message;
}
