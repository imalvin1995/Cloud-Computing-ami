# AWS AMI for CSYE 6225

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
|Xuanshan Xiao |001474067|xiao.x@husky.neu.edu |
|Zehua Ma |001448271 |ma.zeh@husky.nue.edu |
|YuChiao Huang |001442969 |huang.yuc@husky.neu.edu |
|Yimu Jin| 001449259 | jin.yim@husky.neu.edu |


## database
CREATE TABLE `attachment` (
  `id` varchar(36) NOT NULL,
  `note_id` varchar(36) NOT NULL,
  `url` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_size` bigint(32) default 0,
  `file_type` char(8) not null,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user(
   id INT PRIMARY KEY AUTO_INCREMENT,
   email_address VARCHAR(255),
   pwd_string VARCHAR(255)
)

CREATE TABLE csye6225.`note` ( 
   `id` varchar(36) NOT NULL ,  
   `user_id` int(11) DEFAULT '0',   
   `title` varchar(255) DEFAULT '',   
   `content` varchar(255) DEFAULT '',   
   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,  
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,   
   PRIMARY KEY (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

mysql -h [csye6225-spring2019.cznyz0wjytd7.us-east-1.rds.amazonaws.com] -P 3306 -u csye6225master -p

## Validate Template

```
packer validate ubuntu-ami.json
```

## Build AMI

```
packer build \
    -var 'aws_access_key=REDACTED' \
    -var 'aws_secret_key=REDACTED' \
    -var 'aws_region=us-east-1' \
    -var 'subnet_id=REDACTED' \
    ubuntu-ami.json
```

## Build Stacks
1. Open csye6225-aws-cf-create-stack.sh to create a networking stack, then open csye6225-aws-cf-create-application-stack.sh to create a application stack, input the application stack name and networking stack name.

2. Open csye6225-aws-cf-terminate-application-stack.sh to terminate the application stack, then open the csye6225-aws-cf-terminate-stack.sh to terminate the networking stack. 

