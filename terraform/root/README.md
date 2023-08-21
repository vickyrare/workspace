export AWS_PROFILE=little_images

aws ec2 create-default-vpc --region ap-southeast-2

aws ec2 describe-security-groups --region ap-southeast-2 | grep GroupId

aws ec2 describe-vpcs --region ap-southeast-2 | grep VpcId

aws ec2 describe-subnets --region ap-southeast-2 | grep SubnetId

terraform state list

terraform state show module.limbs-php.aws_lb.limbs-php-lb

terraform state show module.limbs-ruby.aws_lb.limbs-ruby-lb
