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

resource "aws_security_group_rule" "example" {
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = "sg-065526e90c6b89749"
}

resource "aws_security_group_rule" "example-2" {
  type              = "ingress"
  from_port         = 3306
  to_port           = 3306
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = "sg-065526e90c6b89749"
}

resource "aws_db_instance" "myinstance" {
  engine                 = "mysql"
  identifier             = "limbs-db-instance"
  allocated_storage      = 20
  engine_version         = "5.7"
  instance_class         = "db.t2.micro"
  username               = ""
  password               = ""
  parameter_group_name   = "default.mysql5.7"
  vpc_security_group_ids = ["sg-065526e90c6b89749"]
  skip_final_snapshot    = true
  publicly_accessible    = false
}

resource "aws_lb" "my-personal-web" {
  name               = "my-personal-web-lb-tf"
  internal           = false
  load_balancer_type = "application"
  security_groups    = ["sg-065526e90c6b89749"]
  subnets            = ["subnet-0debef2d90c3ee03f", "subnet-0d1b2de0405787aab", "subnet-0e8e26b7707d2ce98"]
  tags = {
    env = "dev"
  }
}

resource "aws_lb_target_group" "my-personal-web" {
  name        = "tf-my-personal-web-lb-tg"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = "vpc-0ab54f623133d9902"
}

resource "aws_lb_listener" "my-personal-web" {
  load_balancer_arn = aws_lb.my-personal-web.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.my-personal-web.arn
  }
}

resource "aws_ecs_cluster" "my-personal-web" {
  name = "my-personal-web-api-cluster"
}

resource "aws_ecs_cluster_capacity_providers" "my-personal-web" {
  cluster_name = aws_ecs_cluster.my-personal-web.name

  capacity_providers = ["FARGATE"]
}

resource "aws_ecs_task_definition" "my-personal-web" {
  family                   = "service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = "arn:aws:iam::430971056203:role/ecsTaskExecutionRole"
  task_role_arn            = "arn:aws:iam::430971056203:role/ecsTaskExecutionRole"
  container_definitions = jsonencode([
    {
      name      = "my-personal-web-api"
      image     = "430971056203.dkr.ecr.ap-southeast-1.amazonaws.com/limbs:latest"
      cpu       = 256
      memory    = 512
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ],
      environment = [
        {
          "name" : "DB_USER",
          "value" : ""
        },
        {
          "name" : "DB_PASSWORD",
          "value" : ""
        },
        {
          "name" : "DB_NAME",
          "value" : "limbs"
        },
        {
          "name" : "DB_PORT",
          "value" : "3306"
        },
        {
          "name" : "DB_HOST",
          "value" : "limbs-db-instance.cfnxzdkkl5os.ap-southeast-1.rds.amazonaws.com"
        },
        {
          "name" : "HOME_DOMAIN",
          "value" : "staging-php.littleimages.com.au"
        },
        {
          "name" : "RAILS_URL",
          "value" : "staging-ruby.littleimages.com.au"
        },
        {
          "name" : "RAILS_API",
          "value" : "staging-ruby.littleimages.com.au"
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "my-personal-web" {
  name            = "my-personal-web"
  cluster         = aws_ecs_cluster.my-personal-web.id
  task_definition = aws_ecs_task_definition.my-personal-web.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = ["subnet-0debef2d90c3ee03f", "subnet-0d1b2de0405787aab", "subnet-0e8e26b7707d2ce98"]
    security_groups  = ["sg-065526e90c6b89749"]
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