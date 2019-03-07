set -e
aws deploy create-application --application-name csye6225-webapp --compute-platform Server
echo "Please input the key name :"
read key
echo "Please input the value :"
read value
if [ -z "$key" ]; then
	key="csye6225_key"
fi
if [ -z "$value" ]; then
	value="csye6225_value"
fi
re=$(cat ec2-tag.json|sed "s/1/$key/g"|sed "s/csye6225_value/$value/g" )
role_arn=$(aws iam get-role --role-name CodeDeployServiceRole --query 'Role.Arn'|sed 's/\"//g')
aws deploy create-deployment-group --application-name csye6225-webapp --deployment-group-name csye6225-webapp-deployment --service-role-arn $role_arn --deployment-style deploymentType=IN_PLACE,deploymentOption=WITHOUT_TRAFFIC_CONTROL --deployment-config-name CodeDeployDefault.AllAtOnce --auto-rollback-configuration enabled=true,events=DEPLOYMENT_FAILURE --ec2-tag-set "$re"