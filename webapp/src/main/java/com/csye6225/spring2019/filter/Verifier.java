package com.csye6225.spring2019.filter;

import com.csye6225.spring2019.entity.Account;
import com.google.common.base.Splitter;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Log4j2
public class Verifier {
    public static Account isVerified(String auth)  {
        if(StringUtils.isEmpty(auth)) return null;
        String basic = "Basic";
        if(!auth.contains(basic)) return null;
        List<String> list ;
        String accpString = auth.substring(basic.length()).trim();
        try {
            accpString = new String(Base64.getDecoder().decode(accpString));
            list = Splitter.on(":").trimResults().splitToList(accpString);
        }catch (Exception e){
            log.info(String.format("Invalid input:%s",accpString),e);
            return null;
        }
        if(list.size() !=2){
            log.warn(String.format(Arrays.toString(list.toArray())));
            return null;
        }
        Account account = new Account();
        account.setEmailAddress(list.get(0));
        account.setPwdString(list.get(1));
        return account;
    }

}
