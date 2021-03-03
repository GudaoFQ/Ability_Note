参考redis单线程模型.png
![](amsg-redis单线程模型.png)

### 说明
服务器与redis服务器简历连接，通过socket发送连接请求，redis的server socket监控到这个连接请求，然后