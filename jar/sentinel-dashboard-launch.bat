rem 2024-5-15  22:21-下面的指令必须放在CMD(不是PowerShell，否则会报 找不到或无法加载主类 的错误)中运行
java -Dserver.port=9888 -Dcsp.sentinel.dashboard.server=localhost:9888 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.7.jar
rem 2024-5-16  21:08-客户端/微服务启动时要添加参数 -Dproject.name=xxxx -Dcsp.sentinel.dashboard.server=127.0.0.1:9888 作为虚拟机参数