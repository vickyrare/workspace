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
  alias  = "ap-southeast-1"
  region = "ap-southeast-1"
}

resource "aws_vpc" "my-personal-web" {
  provider = aws.ap-southeast-1
  cidr_block = "10.0.0.0/16"

  tags = {
    env = "dev"
  }
}

resource "aws_internet_gateway" "gw" {
  provider = aws.ap-southeast-1
  vpc_id = aws_vpc.my-personal-web.id

  tags = {
    env = "dev"
  }
}

resource "aws_route_table" "route-table" {
  provider = aws.ap-southeast-1
  vpc_id = aws_vpc.my-personal-web.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
     env = "dev"
  }
}

resource "aws_subnet" "my-personal-web" {
  provider = aws.ap-southeast-1
  vpc_id            = aws_vpc.my-personal-web.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "ap-southeast-1a"

  tags = {
    env = "dev"
  }
}

resource "aws_subnet" "my-personal-web-1" {
  provider = aws.ap-southeast-1
  vpc_id            = aws_vpc.my-personal-web.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "ap-southeast-1b"

  tags = {
    env = "dev"
  }
}

resource "aws_security_group" "my-personal-web" {
  provider = aws.ap-southeast-1

  name        = "allow_http"
  description = "Allow HTTP inbound traffic"
  vpc_id      = aws_vpc.my-personal-web.id

  ingress {
    description = "Allow HTTP for all"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb" "my-personal-web" {
  provider = aws.ap-southeast-1

  name               = "my-personal-web-lb-tf"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.my-personal-web.id]
  subnets            = [aws_subnet.my-personal-web.id, aws_subnet.my-personal-web-1.id]

  tags = {
    env = "dev"
  }
}

resource "aws_lb_target_group" "my-personal-web" {
  provider = aws.ap-southeast-1

  name        = "tf-my-personal-web-lb-tg"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.my-personal-web.id
}

resource "aws_lb_listener" "my-personal-web" {
  provider = aws.ap-southeast-1

  load_balancer_arn = aws_lb.my-personal-web.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.my-personal-web.arn
  }
}

resource "aws_ecs_cluster" "my-personal-web" {
  provider = aws.ap-southeast-1
  name     = "my-personal-web-api-cluster"
}

resource "aws_ecs_cluster_capacity_providers" "my-personal-web" {
  provider = aws.ap-southeast-1

  cluster_name = aws_ecs_cluster.my-personal-web.name

  capacity_providers = ["FARGATE"]
}

resource "aws_ecs_task_definition" "my-personal-web" {
  provider = aws.ap-southeast-1

  family                   = "service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 1024
  memory                   = 2048
  container_definitions = jsonencode([
    {
      name      = "my-personal-web-api"
      image     = "nginx"
      cpu       = 1024
      memory    = 2048
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "my-personal-web" {
  provider = aws.ap-southeast-1

  name            = "my-personal-web"
  cluster         = aws_ecs_cluster.my-personal-web.id
  task_definition = aws_ecs_task_definition.my-personal-web.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.my-personal-web.id, aws_subnet.my-personal-web-1.id]
    security_groups  = [aws_security_group.my-personal-web.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.my-personal-web.arn
    container_name   = "my-personal-web-api"
    container_port   = 80
  }

  tags = {
    env = "dev"
  }
}

resource "aws_cloudwatch_log_group" "logs" {
  provider = aws.ap-southeast-1
  name              = "/fargate/service/app.log"
  retention_in_days = 1
  tags = {
    env = "dev"
  }
}