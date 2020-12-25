## Hash类型【redis-6】
redis文档地址：<http://redisdoc.com/hash/index.html> 
命令查看：help @hash

#### hset key field value [field value ...]
* 将哈希表 hash 中域 field 的值设置为 value since: 2.0.0
* 如果域 field 已经存在于哈希表中， 那么它的旧值将被新值 value 覆盖
```shell
127.0.0.1:6379> hset hash gudao value
(integer) 1
127.0.0.1:6379> hget hash gudao
"value"
```

#### hmset key field value [field value ...]
* 同时将多个 field-value (域-值)对设置到哈希表 key 中 since: 2.0.0
* 此命令会覆盖哈希表中已存在的域。
* 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作
```shell
127.0.0.1:6379> hmset hash gudao value test value
OK
# 获取hash中的gudao test的value
127.0.0.1:6379> hmget hash gudao test
1) "value"
2) "value"
127.0.0.1:6379>
```

#### hsetnx key field value
* 当且仅当域 field 尚未存在于哈希表的情况下，将它的值设置为 value since: 2.0.0
* 如果给定域已经存在于哈希表当中， 那么命令将放弃执行设置操作
* 如果哈希表 hash 不存在， 那么一个新的哈希表将被创建并执行 HSETNX 命令
```shell
# 当hash中的field不存在，设值成功
127.0.0.1:6379> hsetnx hash gudao value
(integer) 1
# 当hash中的field已存在，设值失败
127.0.0.1:6379> hsetnx hash gudao name
(integer) 0
127.0.0.1:6379> hget hash gudao
"value"
```

#### hget key field 【例子在上面】
* 返回哈希表中给定域的值 since: 2.0.0

#### hmget key field [field ...] 【例子在上面】
* 返回哈希表 key 中，一个或多个给定域的值 since: 2.0.0
* 如果给定的域不存在于哈希表，那么返回一个 nil 值。
* 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。

#### hdel key field [field ...]
* 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略 since: 2.0.0
```shell
127.0.0.1:6379> hset table gudao value
(integer) 1
127.0.0.1:6379> hdel table gudao
(integer) 1
127.0.0.1:6379> hget table gudao
(nil)
```

#### hexists key field
* 检查给定域 field 是否存在于哈希表 hash 当中 since: 2.0.0
```shell
127.0.0.1:6379> hset table name value
(integer) 1
# 存在返回1
127.0.0.1:6379> hexists table name
(integer) 1
# 不存在返回0
127.0.0.1:6379> hexists table gudao
(integer) 0
```

#### hincrby key field increment
* 为哈希表 key 中的域 field 的值加上增量 increment since: 2.0.0
* 增量也可以为负数，相当于对给定域进行减法操作
* 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令
* 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 
* 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误
* 本操作的值被限制在 64 位(bit)有符号数字表示之内
```shell
127.0.0.1:6379> hset table gudao 10
(integer) 0
127.0.0.1:6379> hincrby table gudao 1
(integer) 11
127.0.0.1:6379> hget table gudao
"11"
```

#### hincrbyfloat key field increment
* 为哈希表 key 中的域 field 加上浮点数增量 increment since: 2.6.0
* 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作
* 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作
```shell
127.0.0.1:6379> hset table gudao 1
(integer) 1
127.0.0.1:6379> hincrbyfloat table gudao 0.3
"1.3"
127.0.0.1:6379> hget table gudao
"1.3"
```
* 报错
    * 域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
    * 域 field 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point number)

#### hkeys key
* 返回哈希表 key 中的所有域 since: 2.0.0
```shell
127.0.0.1:6379> hmset hash gudao value name value key vlaue
OK
127.0.0.1:6379> hkeys hash
1) "gudao"
2) "name"
3) "key"
```

#### hvals key
* 返回哈希表 key 中所有域的值 since: 2.0.0
```shell
127.0.0.1:6379> hmset hash gudao value name value key vlaue
OK
127.0.0.1:6379> hvals hash
1) "value"
2) "value"
3) "vlaue"
```

#### hgetall key
* 返回哈希表 key 中，所有的域和值 since: 2.0.0
* 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
```shell
127.0.0.1:6379> hmset hash gudao value name value key vlaue
OK
127.0.0.1:6379> hgetall hash
1) "gudao"
2) "value"
3) "name"
4) "value"
5) "key"
6) "vlaue"
```

#### hlen key
* 返回哈希表 key 中域的数量 since: 2.0.0
```shell
127.0.0.1:6379> hmset hash gudao value name value key vlaue
OK
127.0.0.1:6379> hlen hash
(integer) 3
```

#### hstrlen key field
* 返回哈希表 key 中， 与给定域 field 相关联的值的字符串长度（string length） since: 3.2.0
* 如果给定的键或者域不存在， 那么命令返回 0 
```shell
127.0.0.1:6379> hmset table gudao helloworld name test
OK
127.0.0.1:6379> hstrlen table gudao
(integer) 10
127.0.0.1:6379> hstrlen table name
(integer) 4
```

#### hscan key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate hash fields and associated values
since: 2.8.0
