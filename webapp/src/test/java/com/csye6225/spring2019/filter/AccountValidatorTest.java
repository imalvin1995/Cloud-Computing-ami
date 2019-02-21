package com.csye6225.spring2019.filter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* AccountValidator Tester. 
* 
* @author <Authors name> 
* @since <pre>Jan 29, 2019</pre> 
* @version 1.0 
*/ 
public class AccountValidatorTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: validate(final String account) 
* 
*/ 
@Test
public void testValidate() throws Exception {
    AccountValidator accountValidator = new AccountValidator();
    String test1 = "Ilovejava";
    String test2 = "Ilovejava@google";
    String test3 = "123888";
    String test4 = "Ilovejava123@google..com";
    String test5 = "Ilovejava@google.com";
    String test6 = "Ilovejava123@163.com";

    Assert.assertFalse(accountValidator.validate(test1));
    Assert.assertFalse(accountValidator.validate(test2));
    Assert.assertFalse(accountValidator.validate(test3));
    Assert.assertFalse(accountValidator.validate(test4));
    Assert.assertTrue(accountValidator.validate(test5));
    Assert.assertTrue(accountValidator.validate(test6));


} 


} 
