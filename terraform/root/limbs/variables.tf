variable "rds_dbname" {
  type = string
}

variable "rds_username" {
  type = string
}

variable "rds_password" {
  type = string
}

variable "limbs_db_instance" {
  type = any
}

variable "limbs_db_instance_address" {
  type = string
}

variable "security_group" {
  type = string
}

variable "subnets" {
  type = list(any)
}

variable "limbs_cluster_id" {
  type = string
}

variable "limbs_image" {
  type = string
}

