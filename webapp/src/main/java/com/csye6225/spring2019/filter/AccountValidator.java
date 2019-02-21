package com.csye6225.spring2019.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_REGEX =
            "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public AccountValidator() {
        pattern = Pattern.compile(PASSWORD_REGEX);
    }

    public boolean validate(final String account) {
        matcher = pattern.matcher(account);
        return matcher.matches();
    }
}
