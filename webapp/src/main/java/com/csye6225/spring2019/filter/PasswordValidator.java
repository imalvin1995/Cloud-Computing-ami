package com.csye6225.spring2019.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_REGEX =
                    "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,30}$";

    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_REGEX);
    }

    public boolean validate(final String password){
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
