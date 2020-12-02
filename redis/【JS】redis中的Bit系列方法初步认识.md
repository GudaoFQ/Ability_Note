## Redis中的Bit

#### 这个是SETBIT使用方法的简单说明
`SETBIT key offset value` : 设置或清除该位在存储在键的字符串值偏移
>设置或清除该位在存储在键的字符串值偏移

#### 例子
* 设置一个　key-value  ，键的名字叫“andy” 值为字符'a'
![redis-setKey方法.jpg](../resource/redis/redis-setKey方法.jpg)
* 'a' 的ASCII码是97，转换为二进制是：01100001。
* offset的学名叫做“偏移” ，二进制中的每一位就是offset值啦。
* 比如在这里  offset 0 等于 ‘0’ ，offset 1等于'1' ,offset2等于'1',offset 6 等于'0' 。
* offset是从左往右计数的，也就是从高位往低位。

#### 通过setBig将'a'设置为'b'
![redis-setBit方法.jpg](../resource/redis/redis-setBit方法.jpg)
* 每次SETBIT完毕之后，有一个（integer） 0或者（integer）1的返回值，这个是在你进行SETBIT 之前，该offset位的比特值。

#### 再次获取“andy”的值
![redis-getKey方法.jpg](../resource/redis/redis-getKey方法.jpg)
* 此时发现原先的值'a' 通过setBit修改后变成了值 'b'

