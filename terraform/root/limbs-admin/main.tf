resource "aws_lb" "limbs-admin-lb" {
  name               = "limbs-admin-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.security_group]
  subnets            = [var.subnets[0], var.subnets[1], var.subnets[2]]
  tags = {
    env = "dev"
  }
}

output "lb_admin_endpoint" {
  value = aws_lb.limbs-admin-lb.dns_name
}


resource "aws_lb_target_group" "limbs-admin-lb-target-group" {
  name        = "limbs-admin-lb-target-group"
  port        = 80
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = "vpc-0ab54f623133d9902"
}

resource "aws_lb_listener" "limbs-admin-lb-listener" {
  load_balancer_arn = aws_lb.limbs-admin-lb.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.limbs-admin-lb-target-group.arn
  }
}

resource "aws_ecs_task_definition" "limbs-admin-task-definition" {
  family                   = "service"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = "arn:aws:iam::430971056203:role/ecsTaskExecutionRole"
  task_role_arn            = "arn:aws:iam::430971056203:role/ecsTaskExecutionRole"
  container_definitions = jsonencode([
    {
      name  = "limbs-admin-task"
      image = var.limbs_admin_image
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
          "value" : "mysql2://${var.rds_username}:${var.rds_password}@l${var.limbs_db_instance_endpoint}"
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

resource "aws_ecs_service" "limbs-admin-service" {
  name            = "limbs-admin-service"
  cluster         = var.limbs_cluster_id
  task_definition = aws_ecs_task_definition.limbs-admin-task-definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [var.subnets[0], var.subnets[1], var.subnets[2]]
    security_groups  = [var.security_group]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.limbs-admin-lb-target-group.arn
    container_name   = "limbs-admin-task"
    container_port   = 80
  }

  tags = {
    env = "dev"
  }
}