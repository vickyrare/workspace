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

variable "subnets" {
  type = list(any)
}

variable "limbs_image" {
  type = string
}

variable "limbs_admin_image" {
  type = string
}