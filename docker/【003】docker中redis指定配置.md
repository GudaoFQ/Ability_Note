## Redis配置

### 拉取redis镜像
```shell
docker pull docker
```

### 取redis官方拉去配置文件 `redis.conf`
[redis官方配置地址](http://download.redis.io/redis-stable/redis.conf)

### 修改配置
```shell
# 注释掉这部分，这是限制redis只能本地访问
bind 127.0.0.1
# 默认yes，开启保护模式，限制为本地访问
protected-mode no
# 默认no，改为yes意为以守护进程方式启动，可后台运行，除非kill进程，改为yes会使配置文件方式启动redis失败
daemonize no

# 下面的配置可以不用
# 数据库个数（可选），我修改了这个只是查看是否生效
databases 16 
# 输入本地redis数据库存放文件夹（可选）
dir ./
# redis持久化（可选）
appendonly yes
```

### 执行创建Container：指定redis的配置文件
```shell
docker run -p 6379:6379 --name iredis -v /gudao/images/redis/redis.conf:/etc/redis/redis.conf -v /home/docker/redis/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes

# 说明
docker run -p 6379:6379 # 指定映射端口
           --name iredis # 指定容器名称
           -v /gudao/images/redis/redis.conf:/etc/redis/redis.conf # 拷贝配置文件到容器中【：前面的是本地服务器的redis.conf位置，：后面的是要将redis.conf拷贝到】
           -v /home/docker/redis/data:/data # 复制数据
           -d redis # 后台启动redis镜像
           redis-server /etc/redis/redis.conf # 通过指定的配置文件运行redis-server
           --appendonly yes # 开启持久化
```

### 参数详情
注意：在docker中启动redis一定要把：daemonize 设置为 no，这个很重要，如果不是no docker会一直启动失败，原因是docker本身需要后台运行，而这个配置选项也是以守护进程启动，两者会冲突

1. -p 6379:6379：
第一个端口是宿主机端口(服务器端口)，第二个端口是容器端口(容器是一个个沙箱外部不不能访问的)，将容器的6379端口映射到宿主机的6379端口上，这样可以通过访问宿主机6379来访问redis;

2. –name iredis：
> 容器的名字 iredis 方便以后操作容器

3. -v /gudao/images/redis/redis.conf:/etc/redis/redis.conf：
    > 挂载持久化配置<br>
    > /gudao/images/redis/redis.conf：是宿主机(服务器)你自己的redis.conf文件路径<br>
    > /etc/redis/redis.conf：容器内部的redis.conf文件路径，不用手动创建，容器启动时会把上边宿主机的redis.conf自动映射到改目录下. 这样在修改redis.conf文件时候就不用进入到容器内部去修改了

4. -v /home/docker/redis/data:/data：
    > 挂载持久化文件<br>
    > /home/docker/redis/data是宿主机中持久化文件的位置，/data是容器中持久化文件的位置

5. -d：
    > 后台启动

6. redis:5.0.5 redis-server /etc/redis/redis.conf：
    > redis:5.0.5：是镜像的名称+版本如下
