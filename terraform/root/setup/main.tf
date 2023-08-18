resource "aws_security_group_rule" "http-80" {
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = var.security_group
}

resource "aws_security_group_rule" "mysql-3306" {
  type              = "ingress"
  from_port         = 3306
  to_port           = 3306
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = var.security_group
}

resource "aws_db_instance" "limbs-db-instance" {
  engine                 = "mysql"
  identifier             = "limbs-db-instance"
  allocated_storage      = 20
  engine_version         = "5.7"
  instance_class         = "db.t2.micro"
  username               = var.rds_username
  password               = var.rds_password
  parameter_group_name   = "default.mysql5.7"
  vpc_security_group_ids = [var.security_group]
  skip_final_snapshot    = true
  publicly_accessible    = false
}

output "limbs_db_instance" {
  value = aws_db_instance.limbs-db-instance
}

output "limbs_db_instance_endpoint" {
  value = aws_db_instance.limbs-db-instance.endpoint
}

/*data "local_file" "sql_script" {
  filename = "/Users/waqqas.sharif/Downloads/limbs_20221127_2010AEST.sql"
}

resource "null_resource" "db_setup" {
  provisioner "local-exec" {
    command = "mysql --host=${aws_db_instance.limbs-db-instance.endpoint} --port=3306 --user=${var.rds_username} --password=${var.rds_password} --database=${var.rds_dbname} < ${data.local_file.sql_script.content}"
  }
}*/

resource "aws_ecs_cluster" "limbs-cluster" {
  name = "limbs-cluster"
}

output "limbs-cluster" {
  value = aws_ecs_cluster.limbs-cluster.id
}

resource "aws_ecs_cluster_capacity_providers" "limbs-cluster-capacity-providers" {
  cluster_name = aws_ecs_cluster.limbs-cluster.name

  capacity_providers = ["FARGATE"]
}