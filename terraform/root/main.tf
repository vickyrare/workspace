terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "ap-southeast-2"
}

module "setup" {
  source         = "./setup"
  rds_dbname     = var.rds_dbname
  rds_username   = var.rds_username
  rds_password   = var.rds_password
  security_group = var.security_group
  subnets        = var.subnets
}

module "limbs-php" {
  source                    = "./limbs-php"
  limbs_php_image           = var.limbs_php_image
  rds_dbname                = var.rds_dbname
  rds_username              = var.rds_username
  rds_password              = var.rds_password
  security_group            = var.security_group
  subnets                   = var.subnets
  vpc                       = var.vpc
  limbs_db_instance         = module.setup.limbs_db_instance
  limbs_db_instance_address = module.setup.limbs_db_instance_address
  limbs_cluster_id          = module.setup.limbs-cluster
}

module "limbs-ruby" {
  source                    = "./limbs-ruby"
  limbs_ruby_image          = var.limbs_ruby_image
  rds_dbname                = var.rds_dbname
  rds_username              = var.rds_username
  rds_password              = var.rds_password
  security_group            = var.security_group
  subnets                   = var.subnets
  vpc                       = var.vpc
  limbs_db_instance         = module.setup.limbs_db_instance
  limbs_db_instance_address = module.setup.limbs_db_instance_address
  limbs_cluster_id          = module.setup.limbs-cluster
}