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
# 2024-6-20  10:33-执行xxl-job库的构建脚本
SOURCE ${SQL_SCRIPT_DIR}/tables_xxl_job.sql;
# 2024-6-27  21:40-执行seata集成所需的必要SQL脚本
SOURCE ${SQL_SCRIPT_DIR}/config_seata.sql;

EOF
echo "执行SQL脚本完毕"