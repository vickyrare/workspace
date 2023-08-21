resource "aws_lb" "limbs-ruby-lb" {
  name               = "limbs-ruby-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.security_group]
  subnets            = [var.subnets[0], var.subnets[1], var.subnets[2]]
  tags = {
    env = "dev"
  }
}

output "lb_ruby_endpoint" {
  value = aws_lb.limbs-ruby-lb.dns_name
}


resource "aws_lb_target_group" "limbs-ruby-lb-target-group" {
  name        = "limbs-ruby-lb-target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = var.vpc
}

resource "aws_lb_listener" "limbs-ruby-lb-listener" {
  load_balancer_arn = aws_lb.limbs-ruby-lb.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.limbs-ruby-lb-target-group.arn
  }
}

resource "aws_ecs_task_definition" "limbs-ruby-task-definition" {
  family                   = "service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = "arn:aws:iam::458490389489:role/ecsTaskExecutionRole"
  task_role_arn            = "arn:aws:iam::458490389489:role/ecsTaskExecutionRole"
  container_definitions = jsonencode([
    {
      name  = "limbs-ruby-task"
      image = var.limbs_ruby_image
      command = [
        "bin/rails", "s", "-b", "0.0.0.0", "-p", "80"
      ],
      cpu       = 512
      memory    = 1024
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ],
      environment = [
        {
          "name" : "DATABASE_URL",
          "value" : "mysql2://${var.rds_username}:${var.rds_password}@${var.limbs_db_instance_address}"
        },
        {
          "name" : "PHP_SESSION_URI",
          "value" : "http://staging-php.littleimages.com.au/read_session.php?id="
        },
        {
          "name" : "FTP_URL",
          "value" : "http://ftp.littleimages.com.au/ftpcheck/index.php"
        }
      ]
    }
  ])
  depends_on = [var.limbs_db_instance]
}

resource "aws_ecs_service" "limbs-ruby-service" {
  name            = "limbs-ruby-service"
  cluster         = var.limbs_cluster_id
  task_definition = aws_ecs_task_definition.limbs-ruby-task-definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [var.subnets[0], var.subnets[1], var.subnets[2]]
    security_groups  = [var.security_group]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.limbs-ruby-lb-target-group.arn
    container_name   = "limbs-ruby-task"
    container_port   = 80
  }

  tags = {
    env = "dev"
  }
}