#! /bin/bash
set -e
amiId=`aws ec2 describe-images --owners self --filters "Name=root-device-type,Values=ebs" | grep -o '"ImageId": *"[^"]*"' | grep -o '"[^"]*"$' | sed 's/\"//g' | head -n 1`
echo "Input the stack name which you want to create"
read name
echo "Input the name of stack you want to refer"
read refStackName
aws cloudformation create-stack --stack-name $name --template-body file://csye6225-cf-application.yaml --parameters "ParameterKey=refStackName,ParameterValue=$refStackName" "ParameterKey=amiId,ParameterValue=$amiId"
echo "Processing, please wait"
aws cloudformation wait stack-create-complete --stack-name $name
aws cloudformation describe-stacks
echo "stack create successfully"
