## RocketMQ介绍及安装

### RocketMQ    
> 阿里开源的消息中间件，采用纯JAVA语言编写，高吞吐高可用，采用了Kafka的优点，优化了Kafka的缺点，优化了数据的可靠性，还有事务

### 特性
* 能够保证消息的顺序
* 提供丰富的消息拉取模式
* 高效的订阅者水平扩展能力
* 实时的消息订阅机制
* 亿级消息堆积能力

### 图解MQ
> 三个角色: Producer, Queue, Consumer
![mq图解](../resource/activemq/acitvemq-mq图解.png)

### MQ应用场景
* 服务解耦
  > 双11是购物狂节,用户下单后,订单系统需要通知库存系统，中间通过MQ来过渡处理
* 削峰填谷
  > 秒杀活动，一般会因为流量过大，导致应用挂掉,为了解决这个问题，一般在应用前端加入消息队列
* 异步化缓冲
  > 用户注册后，需要发注册邮件和注册短信，并行处理

### 应用思考点
* 生产端可靠投递性
* 消费端幂等性
* 高可用
* 低延迟
* 可靠性
* 扩展性
* 堆积能力

### 安装说明
