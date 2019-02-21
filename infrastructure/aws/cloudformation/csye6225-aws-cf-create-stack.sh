#! /bin/bash
set -e
echo "Input the stack name which you want to create"
read name
aws cloudformation create-stack --stack-name $name --template-body file://csye6225-cf-networking.yaml
echo "Processing, please wait"
aws cloudformation wait stack-create-complete --stack-name $name
aws cloudformation describe-stacks
echo "stack create successfully"
