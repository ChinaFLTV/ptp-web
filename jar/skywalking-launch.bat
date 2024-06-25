@echo off
chcp 65001
rem 2024-6-25  22:33-skywalking-java-agent发布包下载地址(下载distribution版本) : https://skywalking.apache.org/downloads/#Agents
rem 2024-6-25  22:29-改为你的skywalking的解压目录所在的驱动器
D:
rem 2024-6-25  22:29-换回你的skywalking的解压后的目录根路径处(路径不能含有中文字符!!!)
cd "D:\Programming-Tools\apache-skywalking-java-agent-9.2.0\skywalking-agent"
echo 正在启动skywalking-java-agent，请稍候...
rem java -javaagent:./skywalking-agent.jar -Dskywalking.agent.service_name=ptp-web-web -Dskywalking.collector.backend_service=127.0.0.1:11800 -jar 你的微服务模块打成的jar包

rem 2024-6-25  22:30-检查程序运行指令是否出错
if %ERRORLEVEL% NEQ 0 (

    echo 脚本运行出错：%ERRORLEVEL%

)else(

    echo 程序已正常退出...

)