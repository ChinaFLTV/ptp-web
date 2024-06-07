@echo off
chcp 65001
rem 2024-6-7  22:28-改为你的JMeter的解压目录所在的驱动器
D:
rem 2024-6-6  22:49-换回你的JMeter的解压后的bin目录所在路径
cd "D:\编程工具\apache-jmeter-5.5\apache-jmeter-5.5\bin"
echo "正在启动JMeter，请稍候..."
java -jar .\ApacheJMeter.jar