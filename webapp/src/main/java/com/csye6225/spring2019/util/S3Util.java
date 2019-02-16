package com.csye6225.spring2019.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class S3Util {
    private static AmazonS3 s3;
    static {
         s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    }
    public static void listBucket(){
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            log.info("* " + b.getName());
        }
    }

    public static void createBucket(String bucket_name){
        Bucket b = null;
        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("Bucket %s already exists.\n", bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
                log.info("create Successfully");
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
    }


}
