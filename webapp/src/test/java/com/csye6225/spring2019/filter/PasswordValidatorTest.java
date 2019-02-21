package com.csye6225.spring2019.filter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* PasswordValidator Tester. 
* 
* @author <Authors name> 
* @since <pre>Jan 29, 2019</pre> 
* @version 1.0 
*/ 
public class PasswordValidatorTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: validate(final String password)
* 
*/ 
@Test
public void testValidate() throws Exception { 
//TODO: Test goes here...
    PasswordValidator passwordValidator = new PasswordValidator();
    String test1 = "ilovejava";
    String test2 = "6668888888";
    String test3 = "ilovejava666";
    String test4 = "ilovejava$$";
    String test5 = "ilovejava666##";
    String test6 = "Ilovejava888";
    String test7 = "123";

    Assert.assertFalse(passwordValidator.validate(test1));
    Assert.assertFalse(passwordValidator.validate(test2));
    Assert.assertFalse(passwordValidator.validate(test3));
    Assert.assertFalse(passwordValidator.validate(test4));
    Assert.assertTrue(passwordValidator.validate(test5));
    Assert.assertTrue(passwordValidator.validate(test6));
    Assert.assertFalse(passwordValidator.validate(test7));

} 


} 
