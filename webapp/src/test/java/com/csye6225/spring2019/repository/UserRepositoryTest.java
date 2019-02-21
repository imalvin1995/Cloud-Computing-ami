package com.csye6225.spring2019.repository;

import com.csye6225.spring2019.entity.Account;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
@Before
public void before() throws Exception {

} 

@After
public void after() throws Exception { 
} 






@Test
public void testFindAll() throws Exception {
//TODO: Test goes here...
    System.out.println("testFindAll()");
    List<Account> accounts = userRepository.findAll();
    System.out.println(Arrays.toString(accounts.toArray()));

} 








@Test
public void testFindByEmailAddress() throws Exception {
//TODO: Test goes here...
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
        int randomLimitedInt = leftLimit + (int)
                (random.nextFloat() * (rightLimit - leftLimit + 1));
        buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();

    System.out.println(generatedString);
    Account account1 = new Account();
    account1.setEmailAddress(generatedString);
    account1.setPwdString("test");
    int i = userRepository.insertAccount(account1);
    Account account = userRepository.findByEmailAddress(generatedString);
    System.out.println(account.getEmailAddress() + account.getPwdString());
    Assert.assertTrue(account.getEmailAddress().equals(generatedString));
    Assert.assertTrue(account.getPwdString().equals("test"));
}






/** 
* 
* Method: checkAccount(Account account) 
* 
*/




@Test
public void testQueryAccountByInfo() throws Exception {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
        int randomLimitedInt = leftLimit + (int)
                (random.nextFloat() * (rightLimit - leftLimit + 1));
        buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();

    Account account1 = new Account();
    account1.setEmailAddress(generatedString);
    account1.setPwdString("test");
    int i = userRepository.insertAccount(account1);
    Account account = userRepository.queryAccountByInfo(generatedString,"test");
    //System.out.println(account.getEmailAddress() + account.getPwdString());
    Assert.assertTrue(account.getEmailAddress().equals(generatedString));
    Assert.assertTrue(account.getPwdString().equals("test"));
}





@Test
public void testInsertAccount() throws Exception{
    Account account = new Account();
    account.setEmailAddress("test");
    account.setPwdString("test");
    int i = userRepository.insertAccount(account);
    //System.out.println(i);
    Assert.assertTrue(i == 1);
}


} 
