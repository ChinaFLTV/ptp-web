@echo off
chcp 65001
rem 2024-6-7  22:28-改为你的JMeter的解压目录所在的驱动器
D:
rem 2024-6-6  22:49-换回你的JMeter的解压后的bin目录所在路径(路径不能含有中文字符!!!)
cd "D:\Programming-Tools\apache-jmeter-5.5\apache-jmeter-5.5\bin"
echo 正在启动JMeter，请稍候...
rem 2024-6-9  23:27-将JMX测试计划文件更改为该文件在你电脑的当前绝对路径
java -jar .\ApacheJMeter.jar -t "D:\JavaProjects\ptp-web-backend\jar\ptp-api-test-plan.jmx"

rem 2024-6-9  23:14-检查程序运行指令是否出错
if %ERRORLEVEL% NEQ 0 (

    echo 脚本运行出错：%ERRORLEVEL%

)else(

    echo 程序已正常退出...

)