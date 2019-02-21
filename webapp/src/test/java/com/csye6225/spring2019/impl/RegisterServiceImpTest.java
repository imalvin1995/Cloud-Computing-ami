package com.csye6225.spring2019.impl;

import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.repository.UserRepository;
import com.csye6225.spring2019.service.RegisterService;
import net.bytebuddy.utility.RandomString;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RegisterServiceImp Tester.
 *
 * @author <Authors name>
 * @since <pre>Jan 28, 2019</pre>
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterServiceImpTest {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserRepository userRepository;
    /**
     *
     * Method: registerAccount(Account account)
     *
     */
    @Test
    public void testRegisterAccount() throws Exception {
        //test if account is null
        assertFalse(registerService.registerAccount(null));
        //test if email is empty
        Account a =new Account();
        a.setEmailAddress("");
        a.setPwdString("@Csye6225");
        assertFalse(registerService.registerAccount(a));
        //test if password is empty
        a.setEmailAddress("a@gmail.com");
        a.setPwdString("");
        assertFalse(registerService.registerAccount(a));
        //test if both are empty
        a.setEmailAddress("");
        a.setPwdString("");
        assertFalse(registerService.registerAccount(a));
        //test if both are not empty
        String s = RandomString.make(8);
        a.setEmailAddress(s);
        a.setPwdString("@Csye6225");
        assertTrue(registerService.registerAccount(a));

        //test if account was already existed
        a.setEmailAddress("a@gmail.com");
        userRepository.insertAccount(a);
        Account a1=new Account();
        a1.setEmailAddress("a@gmail.com");
        a1.setPwdString("123456");
        assertFalse(registerService.registerAccount(a1));
        //test if account is not exist
        String s1 = RandomString.make(8);
        a1.setEmailAddress(s1);
        assertTrue(registerService.registerAccount(a1));

    }

    /**
     *
     * Method: checkAccount(Account account)
     *
     */
    @Test
    public void testCheckAccount() throws Exception {
        //test if account is null
        assertFalse(registerService.checkAccount(null));
        //test if email is empty
        Account a=new Account();
        a.setEmailAddress("");
        a.setPwdString("@Csye6225");
        assertFalse(registerService.checkAccount(a));
        //test if password is empty
        a.setEmailAddress("a@gmail.com");
        a.setPwdString("");
        assertFalse(registerService.checkAccount(a));
        //test if both are empty
        a.setEmailAddress("");
        a.setPwdString("");
        assertFalse(registerService.checkAccount(a));

        //test password match
        String s= RandomString.make(8);
        a.setEmailAddress(s);
        a.setPwdString("@Csye6225");
        registerService.registerAccount(a);
        Account a1=new Account();
        a1.setEmailAddress(a.getEmailAddress());
        a1.setPwdString("@Csye6225");
        assertTrue(registerService.checkAccount(a1));

    }


} 
