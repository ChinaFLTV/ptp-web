@echo off
chcp 65001
echo 正在启动Sentinel，请稍候...
rem 2024-5-15  22:21-下面的指令必须放在CMD(不是PowerShell，否则会报 找不到或无法加载主类 的错误)中运行
java -Dserver.port=9888 -Dcsp.sentinel.dashboard.server=localhost:9888 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.7.jar
rem 2024-5-16  21:08-客户端/微服务启动时必须要添加参数 -Dcsp.sentinel.dashboard.server=127.0.0.1:9888 作为虚拟机参数

rem 2024-6-9  23:15-检查程序运行指令是否出错
if %ERRORLEVEL% NEQ 0 (

    echo 脚本运行出错：%ERRORLEVEL%

)else(

    echo 程序已正常退出...

)