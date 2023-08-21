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
  engine                 = "mariadb"
  identifier             = "limbs-db-instance"
  allocated_storage      = 20
  instance_class         = "db.t4g.micro"
  username               = var.rds_username
  password               = var.rds_password
  vpc_security_group_ids = [var.security_group]
  skip_final_snapshot    = true
  publicly_accessible    = true
}

output "limbs_db_instance" {
  value = aws_db_instance.limbs-db-instance
}

output "limbs_db_instance_address" {
  value = aws_db_instance.limbs-db-instance.address
}

resource "null_resource" "db_setup" {
  provisioner "local-exec" {
    command = "mysql --host=${aws_db_instance.limbs-db-instance.address} --port=3306 --user=${var.rds_username} --password=${var.rds_password} < /Users/waqqas/Downloads/limbs_20221127_2010AEST.sql"
  }
}

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