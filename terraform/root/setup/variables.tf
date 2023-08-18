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
