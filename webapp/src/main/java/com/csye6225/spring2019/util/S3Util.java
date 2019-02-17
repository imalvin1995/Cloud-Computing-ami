package com.csye6225.spring2019.util;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Log4j2
public class S3Util {
    private static AmazonS3 s3;
    static {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider());
        amazonS3ClientBuilder.setForceGlobalBucketAccessEnabled(true);
         s3 = amazonS3ClientBuilder.build();

    }
    // get bucket
    public static Bucket getBucket(String name){
        if(Strings.isEmpty(name))
            return null;
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            if(name.equals(b.getName()))
                return b;
        }
        return null;
    }

    // upload files
    public static boolean uploadFile(String bucketName,String keyName,FileInputStream input ){
        if(Strings.isEmpty(bucketName)||Strings.isEmpty(keyName)||input==null){
            log.error("Invalid params");
            return false;
        }
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .build();

        try {
            // TransferManager processes all transfers asynchronously,
            // so this call returns immediately.
            Upload upload = tm.upload(bucketName, keyName, input, new ObjectMetadata());
            log.info("Object upload started");
            // Optionally, wait for the upload to finish before continuing.
            upload.waitForCompletion();
            log.info("Object upload complete");
        }catch (InterruptedException e){
            log.error(e);
            return false;
        }
        return true;
    }




}
