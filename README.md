# AWS AMI for CSYE 6225

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
|Xuanshan Xiao |001474067|xiao.x@husky.neu.edu |
|Zehua Ma |001448271 |ma.zeh@husky.nue.edu |
|YuChiao Huang |001442969 |huang.yuc@husky.neu.edu |
|Yimu Jin| 001449259 | jin.yim@husky.neu.edu |

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
