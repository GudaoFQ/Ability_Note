## List类型【redis-6】
redis文档地址：<http://redisdoc.com/string/index.html#> 
命令查看：help @list

#### lpush key element [element ...]
* 将一个或多个值 value 插入到列表 key 的表头 since: 1.0.0
* 如果有多个 value 值，那么各个 value 值按**从左到右**的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令
```shell
127.0.0.1:6379> lpush gudao java
(integer) 1

# 加入重复数据
127.0.0.1:6379> lpush gudao java
(integer) 2

# 列表允许重复元素
127.0.0.1:6379> lrange gudao 0 -1
1) "java"
2) "java"

# 加入多个元素 插入结果 c->b->a
127.0.0.1:6379> lpush name a b c
(integer) 3
127.0.0.1:6379> lrange name 0 -1
1) "c"
2) "b"
3) "a"
```

#### lpushx key element [element ...]
* 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表 since: 2.2.0
* 和 LPUSH key value [value …] 命令相反，当 key 不存在时， LPUSHX 命令什么也不做
```shell
# 对非空列表执行 LPUSHX
127.0.0.1:6379> lpushx gudao 1
(integer) 3
127.0.0.1:6379> lrange gudao 0 -1
1) "1"
2) "java"
3) "java"
127.0.0.1:6379> lpush test 1
(integer) 1

# 尝试 LPUSHX，失败，因为key列表为空
127.0.0.1:6379> lpushx key element
(integer) 0
127.0.0.1:6379> lrange key
(error) ERR wrong number of arguments for 'lrange' command
```

#### rpush key element [element ...]
summary: Append one or multiple elements to a list
since: 1.0.0

#### rpushx key element [element ...]
summary: Append an element to a list, only if the list exists
since: 2.2.0

#### blpop key [key ...] timeout
summary: Remove and get the first element in a list, or block until one is available
since: 2.0.0

#### brpop key [key ...] timeout
summary: Remove and get the last element in a list, or block until one is available
since: 2.0.0

#### brpoplpushsource destination timeout
summary: Pop an element from a list, push it to another list and return it; or block until one is available
since: 2.2.0

#### lindex key index
summary: Get an element from a list by its index
since: 1.0.0

#### linsert key BEFORE|AFTER pivot element
summary: Insert an element before or after another element in a list
since: 2.2.0

#### llen key
summary: Get the length of a list
since: 1.0.0

#### lpop key
summary: Remove and get the first element in a list
since: 1.0.0

#### lpos key element [RANK rank] [COUNT num-matches] [MAXLEN len]
summary: Return the index of matching elements on a list
since: 6.0.6

#### lrange key start stop
summary: Get a range of elements from a list
since: 1.0.0

#### lrem key count element
summary: Remove elements from a list
since: 1.0.0

#### lset key index element
summary: Set the value of an element in a list by its index
since: 1.0.0

#### ltrim key start stop
summary: Trim a list to the specified range
since: 1.0.0

#### rpop key
summary: Remove and get the last element in a list
since: 1.0.0

#### rpoplpush source destination
summary: Remove the last element in a list, prepend it to another list and return it
since: 1.2.0


