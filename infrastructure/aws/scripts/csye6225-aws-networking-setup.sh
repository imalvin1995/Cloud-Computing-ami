set -e
##create VPC
re=$( aws ec2 create-vpc --cidr-block "10.0.0.0/16" )

echo "Successfully creating vpc: "$re
id=$( echo $re | jq '.Vpc.VpcId' | sed 's/\"//g' )
if [ -z "$id" ];then
	exit 0
fi
echo $id
echo "Please input the VPC name prefix, the whole name will be PREFIX-csye6225-vpc"
read name
if [ -z "$name" ];then
	echo "VPC name cannot be empty"
	read name
fi
name=$name"-csye6225-"
vpc_name=$name"vpc"
## add VPC tag name
aws ec2 create-tags --resources $id --tags Key=Name,Value=$vpc_name
## create subnet1
subnet_id1=$( aws ec2 create-subnet --availability-zone us-east-1a --cidr-block 10.0.1.0/24 --vpc-id $id | jq '.Subnet.SubnetId' | sed 's/\"//g' )
if [ -z "$subnet_id1" ];then
	exit 0
fi
echo "Successfully create subnet1 : " $subnet_id1
## create subnet2
subnet_id2=$( aws ec2 create-subnet --availability-zone us-east-1b --cidr-block 10.0.2.0/24 --vpc-id $id | jq '.Subnet.SubnetId' | sed 's/\"//g' )
if [ -z "$subnet_id2" ];then
	exit 0
fi
echo "Successfully create subnet2 : " $subnet_id2
##create subnet3
subnet_id3=$( aws ec2 create-subnet --availability-zone us-east-1c --cidr-block 10.0.3.0/24 --vpc-id $id | jq '.Subnet.SubnetId' | sed 's/\"//g' )
if [ -z "$subnet_id3" ];then
	exit 0
fi
echo "Successfully create subnet3 : " $subnet_id3
gateway_id=$( aws ec2 create-internet-gateway | jq '.InternetGateway.InternetGatewayId' | sed 's/\"//g')
if [ -z "$gateway_id" ];then
	exit 0
fi
echo "Successfully create Internet gateway : " $gateway_id
aws ec2 attach-internet-gateway --internet-gateway-id $gateway_id --vpc-id $id
echo "Successfully attach gateway to vpc "
route_table_id=$( aws ec2 create-route-table --vpc-id $id | jq '.RouteTable.RouteTableId' | sed 's/\"//g')
if [ -z "$route_table_id" ];then
	exit 0
fi
echo "Successfully create route table :" $route_table_id
associate_route_table(){
aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $1
echo "Successfully associate route table with subnet :"$1

}
associate_route_table $subnet_id1
associate_route_table $subnet_id2
associate_route_table $subnet_id3
# aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet_id2
# echo "Successfully associate route table with subnet2"
# aws ec2 associate-route-table --route-table-id $route_table_id --subnet-id $subnet_id3
# echo "Successfully associate route table with subnet3"
aws ec2 create-route --route-table-id $route_table_id --destination-cidr-block 0.0.0.0/0 --gateway-id $gateway_id
echo "Successfully create route"
sgi=`aws ec2 describe-security-groups --filters Name=vpc-id,Values=$id --query "SecurityGroups[0].GroupId" | sed 's/\"//g'`

#delete the default security group rule
#echo "Please input the security_group_id:"
#read sgi
json=`aws ec2 describe-security-groups --group-id ${sgi} --query "SecurityGroups[0].IpPermissions"`
aws ec2 revoke-security-group-ingress --cli-input-json "{\"GroupId\":\"${sgi}\",\"IpPermissions\":$json}"
jsonOut=`aws ec2 describe-security-groups --group-id ${sgi} --query "SecurityGroups[0].IpPermissionsEgress"`
aws ec2 revoke-security-group-egress --cli-input-json "{\"GroupId\":\"${sgi}\",\"IpPermissions\":$jsonOut}"
echo "Seuccessfully delete the default security group rule"
#add a new rule
aws ec2 authorize-security-group-ingress --group-id ${sgi} --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id ${sgi} --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-egress --group-id ${sgi} --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-egress --group-id ${sgi} --protocol tcp --port 80 --cidr 0.0.0.0/0
echo "Successfully create two new rules: protocol: tcp, port: 22 and 80, cidr:0.0.0.0/0."
echo "Goodbye."
