USE `ptp_web`;

# 2024-4-8  22:20-创建连接用户，并为其赋予最丰富的权限，后续将使用此用户进行数据库的操作
CREATE USER `ptp_backend`@`%` IDENTIFIED BY '7758521';
GRANT ALL PRIVILEGES ON `ptp_web`.* TO `ptp_backend`@`%` WITH GRANT OPTION;
# 2024-6-20  11:39-也要赋予ptp_backend管理员的xxl_job数据库操作相关的权限，以便XXL_JOB库能够正常运行
GRANT ALL PRIVILEGES ON `xxl_job`.* TO `ptp_backend`@`%` WITH GRANT OPTION;
# 2024-6-28  00:01-也要赋予ptp_backend管理员的seata数据库操作相关的权限，以便seata-server库能够正常运行
GRANT ALL PRIVILEGES ON `seata`.* TO `ptp_backend`@`%` WITH GRANT OPTION;
FLUSH PRIVILEGES;