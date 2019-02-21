#!/bin/bash
set -e
echo "Input the stack name which you want to terminate"
read name
aws cloudformation delete-stack --stack-name $name
echo "Processing, please wait"
aws cloudformation wait stack-delete-complete --stack-name $name
aws cloudformation describe-stacks
echo "stack delete successfully"
