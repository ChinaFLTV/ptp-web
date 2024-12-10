#!/bin/bash

# 2024-10-8  2:44-该shell脚本主要负责启动各个SpringCloud微服务模块
echo "开始启动PTP后台服务..."

# 2024-10-8  2:56-定义一个存储jar包存放路径的变量 , 供后面完整路径使用
BASE_PATH="/app/jars"
# 2024-10-8  19:19-定义启动程序的模式 , 以便启用对应环境下的配置文件(可选值 : dev/prod)
START_MODE="test"
# 2024-10-99  16:05-该IP数据才是最终的应用内部使用到的本主机的实际物理IP地址 , 之所以这里有配置一遍是为了避免每次修改都需要调整每个微服务模块中的self-host的ip配置(优先级 : 本文件的IP配置 > 各个微服务模块的IP配置 > 127.0.0.1)
# 2024-10-9  12:32-应用所在机器的真实的物理IP
SELF_IP=${REAL_MACHINE_IP:-"127.0.0.1"}

# 2024-10-8  2:48-不再进行睡眠等待了 , 因为咱的各个微服务模块之间并不是强依赖启动关系~
# 2024-10-8  2:47-睡眠10s以等待前一个任务充分启动完成
# sleep 10
# echo "正在启动ES服务..."
# java -jar "${BASE_PATH}/ptp-web-service-elasticsearch-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
# echo "启动ES服务成功!"
echo "正在启动网关服务..."
java -jar "${BASE_PATH}/ptp-web-service-gateway-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
echo "启动网关服务成功!"
# echo "正在启动定时任务服务..."
# java -jar "${BASE_PATH}/ptp-web-service-job-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
# echo "启动定时任务服务成功!"
# echo "正在启动消息中间件服务..."
# java -jar "${BASE_PATH}/ptp-web-service-mq-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
# echo "启动消息中间件服务成功!"
# echo "正在启动OSS存储服务..."
# java -jar "${BASE_PATH}/ptp-web-service-store-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
# echo "启动OSS存储服务成功!"
echo "正在启动Web服务..."
# 2024-10-8  2:50-shell命令结尾加&意为后台启动/执行命令 , 不会同步阻塞住后续指令的执行(但是当前脚本被杀死后 , 其附属的后台进程可能也会被同步杀死)
java -jar "${BASE_PATH}/ptp-web-web-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
echo "启动Web服务成功!"
echo "正在启动监控服务..."
java -jar "${BASE_PATH}/ptp-web-service-monitor-1.0.jar" --spring.profiles.active=${START_MODE} --ip.physical.self-host="${SELF_IP}" &
echo "启动监控服务成功!"
echo "全部的PTP后台服务启动成功!!!"

# 2024-10-8  4:12-无限阻塞shell以防止容器过早退出
tail -f /dev/null