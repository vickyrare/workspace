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
  region = "ap-southeast-1"
}

module "setup" {
  source         = "./setup"
  rds_dbname     = var.rds_dbname
  rds_username   = var.rds_username
  rds_password   = var.rds_password
  security_group = var.security_group
  subnets        = var.subnets
}

module "limbs" {
  source                     = "./limbs"
  limbs_image                = var.limbs_image
  rds_dbname                 = var.rds_dbname
  rds_username               = var.rds_username
  rds_password               = var.rds_password
  security_group             = var.security_group
  subnets                    = var.subnets
  limbs_db_instance          = module.setup.limbs_db_instance
  limbs_db_instance_address = module.setup.limbs_db_instance_address
  limbs_cluster_id           = module.setup.limbs-cluster
}

module "limbs-admin" {
  source                     = "./limbs-admin"
  limbs_admin_image          = var.limbs_admin_image
  rds_dbname                 = var.rds_dbname
  rds_username               = var.rds_username
  rds_password               = var.rds_password
  security_group             = var.security_group
  subnets                    = var.subnets
  limbs_db_instance          = module.setup.limbs_db_instance
  limbs_db_instance_address = module.setup.limbs_db_instance_address
  limbs_cluster_id           = module.setup.limbs-cluster
}