resource "aws_lb" "limbs-lb" {
  name               = "limbs-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.security_group]
  subnets            = [var.subnets[0], var.subnets[1], var.subnets[2]]
  tags = {
    env = "dev"
  }
}

output "lb_endpoint" {
  value = aws_lb.limbs-lb.dns_name
}


resource "aws_lb_target_group" "limbs-lb-target-group" {
  name        = "limbs-lb-target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = var.vpc
}

resource "aws_lb_listener" "limbs-lb-listener" {
  load_balancer_arn = aws_lb.limbs-lb.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.limbs-lb-target-group.arn
  }
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
      name      = "limbs-task"
      image     = var.limbs_image
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
      ]
    }
  ])
  depends_on = [var.limbs_db_instance]
}

resource "aws_ecs_service" "limbs-service" {
  name            = "limbs-service"
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
    target_group_arn = aws_lb_target_group.limbs-lb-target-group.arn
    container_name   = "limbs-task"
    container_port   = 80
  }

  tags = {
    env = "dev"
  }
}