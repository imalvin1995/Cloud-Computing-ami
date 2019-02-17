package com.csye6225.spring2019.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.google.common.base.Splitter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
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
    public static String uploadFile(String bucketName,String path,File file,String awsSuffix){
        if(Strings.isEmpty(bucketName)||file==null||Strings.isEmpty(path)||Strings.isEmpty(awsSuffix)){
            log.error("Invalid params");
            return null;
        }
        String fileName = file.getName();
        if(Strings.isEmpty(fileName)){
            log.warn("No file name found");
            return null;
        }
        List<String> names = Splitter.on(".").trimResults().splitToList(fileName);
        if(names.size()!=2){
            log.warn(String.format("Invalid file name %s",fileName));
            return null;
        }
        String filePath = String.format("%s/%s-%d.%s",path,names.get(0)
                ,System.currentTimeMillis(),names.get(1));
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .build();

        try {
            // TransferManager processes all transfers asynchronously,
            // so this call returns immediately.
            Upload upload = tm.upload(bucketName, filePath, file);
            log.info("Object upload started");
            // Optionally, wait for the upload to finish before continuing.
            upload.waitForCompletion();

            log.info("Object upload complete");
        }catch (InterruptedException e){
            log.error(e);
            return null;
        }
        return String.format("%s%s%s",bucketName,awsSuffix,filePath);
    }




}
