## 使用案例
[GitHub-ActiveMQ](https://github.com/GudaoFQ/ActiveMQ_Demo)

### 点对点
#### 运行Producer端，向服务器mq注册message
![点对点推送消息](../resource/activemq/activemq-点对点推送消息.png)

#### 运行Comsumer端
![点对点消费服务](../resource/activemq/acitvemq-点对点消费服务.png)

### 发布/订阅
#### 先运行Consumer端，然后在运行Producer端
> 只有订阅服务开启，发布的消息才能推送到订阅的服务器上，不然订阅服务就会接收不到
![消息订阅消息界面](../resource/activemq/activemq-消息订阅消息界面.png)
