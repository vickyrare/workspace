resource "aws_lb" "limbs-php-lb" {
  name               = "limbs-php-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.security_group]
  subnets            = [var.subnets[0], var.subnets[1], var.subnets[2]]
  tags = {
    env = "dev"
  }
}

output "lb_endpoint" {
  value = aws_lb.limbs-php-lb.dns_name
}

resource "aws_lb_target_group" "limbs-php-lb-target-group" {
  name        = "limbs-lb-target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = var.vpc
}

resource "aws_lb_listener" "limbs-php-lb-listener" {
  load_balancer_arn = aws_lb.limbs-php-lb.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.limbs-php-lb-target-group.arn
  }
}

resource "aws_cloudwatch_log_group" "limbs-php-cloud-watch-group" {
  name = "limbs-php-cloud-watch-group"
}

resource "aws_ecs_task_definition" "limbs-task-definition" {
  family                   = "service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = "arn:aws:iam::458490389489:role/ecsTaskExecutionRole"
  task_role_arn            = "arn:aws:iam::458490389489:role/ecsTaskExecutionRole"
  container_definitions = jsonencode([
    {
      name      = "limbs-php-task"
      image     = var.limbs_php_image
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
          "value" : var.rds_username
        },
        {
          "name" : "DB_PASSWORD",
          "value" : var.rds_password
        },
        {
          "name" : "DB_NAME",
          "value" : var.rds_dbname
        },
        {
          "name" : "DB_PORT",
          "value" : "3306"
        },
        {
          "name" : "DB_HOST",
          "value" : var.limbs_db_instance_address
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
      ],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          awslogs-group = aws_cloudwatch_log_group.limbs-php-cloud-watch-group.name,
          awslogs-region = "ap-southeast-2",
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
  depends_on = [var.limbs_db_instance]
}

resource "aws_ecs_service" "limbs-php-service" {
  name            = "limbs-php-service"
  cluster         = var.limbs_cluster_id
  task_definition = aws_ecs_task_definition.limbs-task-definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [var.subnets[0], var.subnets[1], var.subnets[2]]
    security_groups  = [var.security_group]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.limbs-php-lb-target-group.arn
    container_name   = "limbs-php-task"
    container_port   = 80
  }

  tags = {
    env = "dev"
  }
}