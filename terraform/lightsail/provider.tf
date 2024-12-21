provider "aws" {
  region  = var.aws_region
  default_tags {
    tags = {
      Project = "club-retro-gamer-woocommerce"
    }
  }
}