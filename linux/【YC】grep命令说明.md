## grep 命令说明

### 总体命令说明
```shell
# 查找指定进程
ps -ef | grep docker

# 查找指定进程个数
ps -ef | grep docker -c

# 从文件中读取关键词进行搜索
grep "17:09:40.989" catalina.out

# 从文件中读取关键词匹配个数
grep "17:09" catalina.out | wc -l

# 从文件中读取关键词进行搜索（除了显示符合搜索条件的那一列之外，并显示该行之**前**的10行内容）
grep "17:09:40.989" catalina.out -B 10

# 从文件中读取关键词进行搜索（除了显示符合搜索条件的那一列之外，并显示该行之**后**的10行内容）
grep "17:09:40.989" catalina.out -A 10

# 同时搜索满足条件以上两个条件的结果，并向下在打印剩下的10行
grep "17:09" catalina.out | grep "40.989" -A 10
```

### 查找指定**进程**
```shell
# 命令
ps -ef | grep 需要搜索的信息

# 示例
ps -ef | grep docker
# 显示
root     18233     1  0 Oct28 ?        00:07:13 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
root     25587 22330  0 10:59 pts/1    00:00:00 grep --color=auto docker

# 说明：第一条记录是查找出的进程；第二条结果是grep进程本身，并非真正要找的进程
```

### 查找指定进程个数
```shell
# 命令
ps -ef | grep 需要搜索的信息 -c

# 示例
ps -ef | grep docker -c
# 显示
2

# 说明：通过 -c 统计数量
```

### 从**文件**中读取关键词进行搜索
```text
# catalina.out信息
17:05:00.494 [schedule-pool-2] INFO  c.c.f.s.w.s.OnlineWebSessionManager - [validateSessions,161] - Finished invalidation session. No sessions were stopped.
17:09:40.977 [http-nio-8080-exec-7] ERROR c.a.d.p.DruidAbstractDataSource - [testConnectionInternal,1481] - discard long time none received connection. , jdbcUrl : jdbc:mysql://127.0.0.1:3306/db?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8, jdbcUrl : jdbc:mysql://127.0.0.1:3306/sca?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8, lastPacketReceivedIdleMillis : 280483
17:09:40.989 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]
17:09:40.990 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]
```
```shell
# 命令
grep "关键信息" 搜索文件

# 示例
grep "17:09:40.989" catalina.out
# 显示
17:09:40.989 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]

# 说明：显示与关键字匹配的一行信息
```

### 从**文件**中读取关键词匹配个数
```shell
# 命令
grep "关键信息" 搜索文件 | wc -l

# 示例
grep "17:09" catalina.out | wc -l
# 显示
3

# 说明：显示与关键字匹配满足次数
```

### 从**文件**中读取关键词进行搜索（除了显示符合搜索条件的那一列之外，并显示该行之**前**的10行内容）
```shell
# 命令
grep "关键信息" 搜索文件 -B 行数

# 示例
grep "17:09:40.989" catalina.out -A 10
# 显示
17:05:00.494 [schedule-pool-2] INFO  c.c.f.s.w.s.OnlineWebSessionManager - [validateSessions,161] - Finished invalidation session. No sessions were stopped.
17:09:40.977 [http-nio-8080-exec-7] ERROR c.a.d.p.DruidAbstractDataSource - [testConnectionInternal,1481] - discard long time none received connection. , jdbcUrl : jdbc:mysql://127.0.0.1:3306/db?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8, jdbcUrl : jdbc:mysql://127.0.0.1:3306/sca?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8, lastPacketReceivedIdleMillis : 280483
17:09:40.989 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]
```

### 从**文件**中读取关键词进行搜索（除了显示符合搜索条件的那一列之外，并显示该行之**后**的10行内容）
```shell
# 命令
grep "关键信息" 搜索文件 -A 行数

# 示例
grep "17:09:40.989" catalina.out -A 10
# 显示
17:09:40.989 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]
17:09:40.990 [http-nio-8080-exec-7] INFO  o.a.s.c.e.EhCacheManager - [getCache,169] - Using existing EHCache named [sys-config]
```