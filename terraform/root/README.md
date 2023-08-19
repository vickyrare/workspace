aws ec2 create-default-vpc --region ap-southeast-1

aws ec2 describe-subnets --region ap-southeast-1 | grep SubnetId

aws ec2 describe-vpcs --region ap-southeast-1 | grep VpcId