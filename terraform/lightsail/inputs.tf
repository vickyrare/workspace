variable "aws_region" {
  type        = string
  default     = "ap-southeast-2"
  description = "Set up our region, that we want to use"
}
variable "project_name" {
  description = "Club Retro Gamer"
  type        = string
}
variable "bundle_id" {
  type        = string
  default     = "nano_3_2"
  description = "Options for instance size"
}
variable "lightsail_blueprints" {
  type        = map(string)
  description = "The ID for a virtual private server image. A list of available blueprint AWS CLI command: aws lightsail get-blueprints"
}