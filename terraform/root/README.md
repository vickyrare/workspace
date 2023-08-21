export AWS_PROFILE=little_images

aws ec2 create-default-vpc --region ap-southeast-2

aws ec2 describe-security-groups --region ap-southeast-2 | grep GroupId

aws ec2 describe-vpcs --region ap-southeast-2 | grep VpcId

aws ec2 describe-subnets --region ap-southeast-2 | grep SubnetId

terraform state list

terraform state show module.limbs.aws_lb.limbs-lb

terraform state show module.limbs-admin.aws_lb.limbs-admin-lb
