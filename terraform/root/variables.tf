variable "rds_dbname" {
  type = string
}

variable "rds_username" {
  type = string
}

variable "rds_password" {
  type = string
}

variable "security_group" {
  type = string
}

variable "vpc" {
  type = string
}

variable "subnets" {
  type = list(any)
}

variable "limbs_php_image" {
  type = string
}

variable "limbs_ruby_image" {
  type = string
}