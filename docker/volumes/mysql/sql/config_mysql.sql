USE `ptp_web`;

# 2024-4-8  22:20-创建连接用户，并为其赋予最丰富的权限，后续将使用此用户进行数据库的操作
CREATE USER `ptp_backend`@`localhost` IDENTIFIED BY '7758521';
GRANT ALL PRIVILEGES ON `ptp_web`.* TO `ptp_backend`@`localhost` WITH GRANT OPTION;
FLUSH PRIVILEGES;