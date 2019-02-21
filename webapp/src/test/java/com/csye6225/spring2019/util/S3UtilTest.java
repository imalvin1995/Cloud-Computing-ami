package com.csye6225.spring2019.util;

import com.amazonaws.services.s3.model.Bucket;
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
import java.io.InputStream;

/** 
* S3Util Tester. 
* 
* @author <Authors name> 
* @since <pre>Feb 17, 2019</pre> 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S3UtilTest {
@Autowired
private Environment env;
String bucket = "";
@Before
public void before() throws Exception {
    bucket = env.getProperty("csye6225.aws.bucket.name");
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getBucket(String name) 
* 
*/ 
@Test
public void testGetBucket() throws Exception { 
//TODO: Test goes here...
    Bucket b = S3Util.getBucket(env.getProperty("csye6225.aws.bucket.name"));
    Assert.assertTrue(b!=null);
} 

/** 
* 
* Method: uploadFile(String bucketName, String keyName, FileInputStream input) 
* 
*/ 
@Test
public void testUploadFile() throws Exception { 
//TODO: Test goes here...
    File file = new File("pom.xml");
    InputStream inputStream = new FileInputStream(file);
    String url = S3Util.uploadFile(env.getProperty("csye6225.aws.bucket.name"),env.getProperty("csye6225.file.folder"),inputStream,env.getProperty("csye6225.aws.url.suffix"),"pom","xml");
    System.out.println(url);
    Assert.assertTrue(Strings.isNotEmpty(url));
} 


@Test
    public void testDeleteFile(){
    S3Util.deleteFile(bucket,"file/test-1550690615555.png");
}

} 
