## String类型【redis-6】
redis文档地址：<http://redisdoc.com/string/index.html#> 
命令查看：help @string

### 字符
#### set key value [EX seconds|PX milliseconds|KEEPTTL] [NX|XX]
* 设置字符串键值对 since: 1.0.0

#### mset key value [key value ...]
* 批量设置key的值 since: 1.0.1
```shell
127.0.0.1:6379> mset gudao test name gg redis rr
OK
127.0.0.1:6379> mget gudao name redis
1) "test"
2) "gg"
3) "rr"
```

#### psetex key milliseconds value
* 设置key的值和有效期限（以毫秒为单位）since: 2.6.0
```shell
# 设置key的过期时间为5000ms【5秒】
127.0.0.1:6379> psetex gg 5000 test
OK
127.0.0.1:6379> get gg
"test"
127.0.0.1:6379> get gg
"test"
127.0.0.1:6379> get gg
(nil)
```

#### setex key seconds value
* 设置key的值和到期日期 since: 2.0.0
```shell
# 设置key的过期时间为4秒
127.0.0.1:6379> setex tank 4 name
OK
127.0.0.1:6379> get tank
"name"
127.0.0.1:6379> get tank
(nil)
```

#### setnx key value
* 仅当key不存在时设置键的值 since: 1.0.0
```shell
127.0.0.1:6379> keys *
1) "test"
2) "name"
3) "redis"
4) "gudao"
# gudao 的key已经存在，此次设值将不成功
127.0.0.1:6379> setnx gudao name
(integer) 0
127.0.0.1:6379> get gudao
"test"
127.0.0.1:6379> setnx gg value
(integer) 1
127.0.0.1:6379> get gg
"value"
```

#### msetnx key value [key value ...]
* 仅当所有设置的key都不存在时，才将多个键设置为多个值 since: 1.0.1
```shell
127.0.0.1:6379> keys *
1) "gg"
2) "test"
3) "name"
4) "redis"
5) "gudao"
# gg 的key已经存在，所以此次的设值都不会成功
127.0.0.1:6379> msetnx gg value mm value ss value
(integer) 0
127.0.0.1:6379> keys *
1) "gg"
2) "test"
3) "name"
4) "redis"
5) "gudao"
```

#### setrange key offset value
* 从指定偏移量开始的key处覆盖字符串的一部分 since: 2.2.0
```shell
127.0.0.1:6379> set key ggtestname
OK
127.0.0.1:6379> setrange key 3 gudao
(integer) 10
127.0.0.1:6379> get key
"ggtgudaome"
```

#### append key value
* 将值附加到key上 since: 2.0.0
```shell
127.0.0.1:6379> set intvalue 1
OK
127.0.0.1:6379> get intvalue
"1"
127.0.0.1:6379> append intvalue 4
(integer) 2
127.0.0.1:6379> get intvalue
"14"
```

#### get key
* 获取key的值 since: 1.0.0

#### mget key [key ...]
* 批量获取key的值 since: 1.0.0
```shell
127.0.0.1:6379> mset gudao test name gg redis rr
OK
127.0.0.1:6379> mget gudao name redis
1) "test"
2) "gg"
3) "rr"
```

#### getrange key start end
* 截取key中的值【并没有改变key的值】 since: 2.4.0
```shell
127.0.0.1:6379> set gudao name
OK
127.0.0.1:6379> getrange gudao 1 -1
"ame"
127.0.0.1:6379> get gudao
"name"
```

#### getset key value
* 设置key值并返回其旧值 since: 1.0.0
```shell
127.0.0.1:6379> get gudao
"name"
127.0.0.1:6379> getset gudao test
"name"
127.0.0.1:6379> get gudao
"test"
```

#### strlen key
* 获取key中存储的值的长度 since: 2.2.0 【返回的key值得字节个数】
```shell
127.0.0.1:6379> strlen intvalue
(integer) 2
```

#### stralgo LCS algo-specific-argument [algo-specific-argument ...]
* 对字符串运行算法(当前是LCS) since: 6.0.0

### bitmaps【具体案例看【JS】redis中的Bit系列方法初步认识.md】
#### SETBIT key offset value
* 设置或清除键处存储的字符串值中偏移量的位（bit），位的设置或清除取决于value参数，可以是0也可以是1 since: 2.2.0

#### getbit key offset
* 对 key 所储存的字符串值，获取指定偏移量上的位(bit) since: 2.2.0
* 当 offset 比字符串值的长度大，或者 key 不存在时，返回 0 
```shell
redis> SETBIT bit 10086 1
(integer) 0
redis> GETBIT bit 10086
(integer) 1

# bit 默认被初始化为 0
redis> GETBIT bit 100   
(integer) 0
```

#### bitcount key [start end]
* 计算给定字符串中，被设置为 1 的bit的数量 since: 2.6.0
* 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
* start 和 end 参数的设置和 GETRANGE 命令类似，都可以使用负数值：比如 -1 表示最后一个位，而 -2 表示倒数第二个位，以此类推。
* 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 
```shell
127.0.0.1:6379> setbit gudao 0 1
(integer) 0
127.0.0.1:6379> setbit gudao 7 1
(integer) 0
127.0.0.1:6379> bitcount gudao 0 -1
(integer) 2
```

#### bitfield key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
* 对字符串执行任意位域整数运算 since: 3.2.0
* BITFIELD 命令后面可以跟 GET、SET、INCRBY三种命令，而 OVERFLOW 是用来指定设置越界以后的处理行为的
* BITFIELD 后面可以顺序跟着多个子命令，每个命令都会有一个返回值，最后形成一个返回值数组，GET 子命令会返回指定位置的值，SET 子命令会在设置成功以后返回设置之前的值，INCRBY 会在修改值之后返回新的值
```shell
# 参数i8表示要设置8位的有符号整数
127.0.0.1:6379> bitfield gudao set i8 0 1
1) (integer) 0
127.0.0.1:6379> bitfield gudao get i8 0
1) (integer) 1
127.0.0.1:6379> get gudao
"\x01"
``` 
* 第一个条命令是设置成了1，第二个命令是读取出来还是1，查询整个字符串的的值是"\x01"，转化成二进制其实就是1000 0000

#### bitop operation destkey key [key ...]
* 对一个或多个保存二进制位的字符串 key 进行位元操作，并将结果保存到 destkey 上 since: 2.6.0
* operation 可以是 AND 、 OR 、 NOT 、 XOR 这四种操作中的任意一种：
    * BITOP AND destkey key [key ...] ，对一个或多个 key 求逻辑并，并将结果保存到 destkey 。
    * BITOP OR destkey key [key ...] ，对一个或多个 key 求逻辑或，并将结果保存到 destkey 。
    * BITOP XOR destkey key [key ...] ，对一个或多个 key 求逻辑异或，并将结果保存到 destkey 。
    * BITOP NOT destkey key ，对给定 key 求逻辑非，并将结果保存到 destkey 。
* 除了 NOT 操作之外，其他操作都可以接受一个或多个 key 作为输入。
* 处理不同长度的字符串
* 当 BITOP 处理不同长度的字符串时，较短的那个字符串所缺少的部分会被看作 0 。
* 空的 key 也被看作是包含 0 的字符串序列。
```shell
# 0100 0000
127.0.0.1:6379> setbit k1 1 1
(integer) 0
# 0100 0000
127.0.0.1:6379> setbit k1 5 0
(integer) 0

# 0100 0000
127.0.0.1:6379> setbit k2 1 1
(integer) 0
# 0100 1000
127.0.0.1:6379> setbit k2 4 1
(integer) 0

# 通过逻辑与对key1、key2进行计算
127.0.0.1:6379> bitop and andkey k1 k2
(integer) 1
# 0100 0000
127.0.0.1:6379> getbit andkey 1
(integer) 1
127.0.0.1:6379> getbit andkey 5
(integer) 0
127.0.0.1:6379> getbit andkey 4
(integer) 0
```

#### bitpos key bit [start] [end]
* 返回位图中第一个值为 bit 的二进制位的位置 since: 2.8.7
* 在默认情况下， 命令将检测整个位图， 但用户也可以通过可选的 start 参数和 end 参数指定要检测的范围。
```shell
# 0000 0010 0000 0010
127.0.0.1:6379> setbit gudao 1 1
(integer) 0
127.0.0.1:6379> setbit gudao 9 1
(integer) 0
127.0.0.1:6379> bitpos gudao 1
(integer) 1
# 此处的start end是第二个字节[8-15]
127.0.0.1:6379> bitpos gudao 1 1 1
(integer) 9
# 此处的start end是第一到第二个字节[0-15]
127.0.0.1:6379> bitpos gudao 1 0 -1
(integer) 1
```

### 数值
#### decr key
* 为键 key 储存的数字值减去一 since: 1.0.0
* 如果键 key 不存在， 那么键 key 的值会先被初始化为0，然后再执行 DECR 操作
* 如果键 key 储存的值不能被解释为数字， 那么 DECR 命令将返回一个错误
```shell
# 当key存在
127.0.0.1:6379> set gudao 1
OK
127.0.0.1:6379> decr gudao
(integer) 0
127.0.0.1:6379> get gudao
"0"

# 当key不存在
127.0.0.1:6379> decr test
(integer) -1
127.0.0.1:6379> get test
"-1"
```

#### dectby key decrement
* 将键 key 储存的整数值减去减量 decrement since: 1.0.0
* 如果键 key 不存在， 那么键 key 的值会先被初始化为 0 ， 然后再执行 DECRBY 命令
* 如果键 key 储存的值不能被解释为数字， 那么 DECRBY 命令将返回一个错误
```shell
# 当key存在
127.0.0.1:6379> set gudao 1
OK
127.0.0.1:6379> decrby gudao 10
(integer) -9
127.0.0.1:6379> get gudao
"-9"

# 当key不存在
127.0.0.1:6379> decrby test 10
(integer) -10
127.0.0.1:6379> get test
"-10"
```

#### incr key
* 为键 key 储存的数字值加上一 since: 1.0.0
* 如果键 key 不存在，那么它的值会先被初始化为0，然后再执行 INCR 命令
* 如果键 key 储存的值不能被解释为数字，那么 INCR 命令将返回一个错误
```shell
# 当key存在
127.0.0.1:6379> set gudao 1
OK
127.0.0.1:6379> incr gudao
(integer) 2
127.0.0.1:6379> get gudao
"2"

# 当key不存在
127.0.0.1:6379> incr test
(integer) 1
127.0.0.1:6379> get test
"1"
```

#### incrby key increment
* 为键 key 储存的数字值加上增量 increment since: 1.0.0
* 如果键 key 不存在， 那么键 key 的值会先被初始化为 0 ， 然后再执行 INCRBY 命令
* 如果键 key 储存的值不能被解释为数字， 那么 INCRBY 命令将返回一个错误
```shell
# 当key存在
127.0.0.1:6379> set gudao 50
OK
127.0.0.1:6379> incrby gudao 100
(integer) 150
127.0.0.1:6379> get gudao
"150"

# 当key不存在
127.0.0.1:6379> incrby test 100
(integer) 100
127.0.0.1:6379> get test
"100"
```

#### incrbyfloat key increment
* 为键 key 储存的值加上浮点数增量increment since: 2.6.0
* 如果键 key 不存在， 那么 INCRBYFLOAT 会先将键 key 的值设为0， 然后再执行加法操作
* 如果命令执行成功， 那么键 key 的值会被更新为执行加法计算之后的新值，并且新值会以字符串的形式返回给调用者
```shell
127.0.0.1:6379> set gudao 10
OK
# 加法计算
127.0.0.1:6379> incrbyfloat gudao 6.3
"16.3"
127.0.0.1:6379> get gudao
"16.3"
# 减法计算
127.0.0.1:6379> incrbyfloat gudao -1.1
"15.2"
```
* 报错
    * 键 key 的值不是字符串类型(因为 Redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
    * 键 key 当前的值或者给定的增量 increment 不能被解释(parse)为双精度浮点数