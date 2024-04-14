#!/bin/bash
# 2024-4-14  18:43-使MySQL初始化程序能够按序执行SQL脚本
CUSTOM_MYSQL_USER=root
CUSTOM_MYSQL_PASSWORD=root
SQL_SCRIPT_DIR="/docker-entrypoint-initdb.d/sql"

echo "开始执行SQL脚本"
mysql -u$CUSTOM_MYSQL_USER -p$CUSTOM_MYSQL_PASSWORD <<EOF

SOURCE ${SQL_SCRIPT_DIR}/create_table.sql;
SOURCE ${SQL_SCRIPT_DIR}/config_mysql.sql;
SOURCE ${SQL_SCRIPT_DIR}/insert_data.sql;

EOF
echo "执行SQL脚本完毕"