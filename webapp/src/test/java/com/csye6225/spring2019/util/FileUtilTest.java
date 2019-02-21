package com.csye6225.spring2019.util;

import com.amazonaws.services.s3.internal.InputSubstream;
import org.apache.logging.log4j.util.Strings;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 
* FileUtil Tester. 
* 
* @author <Authors name> 
* @since <pre>Feb 17, 2019</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileUtilTest { 
@Autowired
private Environment env;
@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: inputSteamToFile(File file, String folderPath) 
* 
*/ 
@Test
public void testInputSteamToFile() throws Exception { 
//TODO: Test goes here...
    File file = new File("pom.xml");
    String url = FileUtil.saveFileToLocal(new FileInputStream(file),env.getProperty("csye6225.file.folder"),"pom","xml");
    System.out.println(url);
    Assert.assertTrue(Strings.isNotEmpty(url));

} 


/** 
* 
* Method: createfolder(String folderPath) 
* 
*/ 
@Test
public void testCreatefolder() throws Exception { 
//TODO: Test goes here...
    boolean re = FileUtil.createfolder(env.getProperty("csye6225.file.folder"));

} 

} 
