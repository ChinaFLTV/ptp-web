# 2024-10-7  23:22-该构建文件用于创建PTP微服务的容器镜像
# 2024-10-8  1:54-注意 : 请在运行该构建文件之前先分别生成每个模块对应的jar包到约定的位置上(具体参照项目的根POM文件)(建议采用IDEA内置的maven工具进行生成) , 同时还有先执行docker-compose文件以启动底层后台服务
# 2024-10-7  23:33-配置基础镜像
FROM openjdk:17-jdk-alpine

# 2024-10-8  3:10-配置镜像的元数据
LABEL maintainer="Dage" \
      description="PTP后台服务" \
      version="1.0"

# 2024-10-8  3:40-安装bash(后面需要在shell中运行服务启动脚本)
RUN apk add --no-cache bash

# 2024-10-8  2:02-配置当前的工作目录
WORKDIR /app

# 2024-10-8  2:12-拷贝jar包和启动脚本文件到容器中
COPY ./jars /app/jars
COPY ./scripts /app/scripts

# 2024-10-8  3:01-赋予服务启动脚本可执行权限(RUN命令仅在镜像构建时执行 , 且每执行一条RUN命令 , 就会在现有的镜像上增加一层镜像层 , 此外 , RUN命令不会像CMD、ENTRYPOINT那样可覆盖 , 它无法在容器执行时被覆盖掉)
RUN chmod +x /app/scripts/start_spring_cloud_services.sh

# 2024-10-8  2:15-暴露端口(仅作提醒 , 实际并不会发布端口 , 需要在容器运行时主动进行端口映射)
EXPOSE 8080 8090 8120 8150 8180 8210

# 2024-10-8  2:33-ENTRYPOINT指令仅在容器运行时执行 , 且不宜被覆盖 , CMD指令可以作为其参数使用
ENTRYPOINT ["/bin/bash", "-c", "/app/scripts/start_spring_cloud_services.sh"]

# 2024-10-8  3:48-开发测试用
# CMD ["/bin/bash","-c","tail -f /dev/null"]