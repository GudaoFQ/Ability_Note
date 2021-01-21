## Redis-String使用场景
```shell
# 单值缓存
set key value
get key

# 对象缓存
set user:1 value(json 数据)
get user:1

# 分布式锁
setnx product:1001 true # 返回 1 代表获取锁成功
setnx product:1001 true # 返回 0 代表获取锁失败
	# ... 执行业务操作
del product:1001 # 执行完业务释放锁
set product:1001 true ex 10 nx # 防止程序意外终止导致死锁

# 计数器
incr article:readcount:1001
get article:readcount:1001

# Web 集群 session 共享
spring session + redis 实现 session 共享

# 分布式系统全局序列号
incrby orderId 1000 # redis 批量生成序列号提升性能
```