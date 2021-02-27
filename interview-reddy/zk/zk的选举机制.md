注：半数机制（Paxos协议）：当集群中的服务器存活数量大于集群数量的一般，认为集群可用，因此zk适合奇数的集群数

### 选举机制中重要的两个属性：
* myid：唯一标识
* zxid：zxid有epoch和计数两部份；每次重新选出leader后，就会产生一个新的epoch

### 启动选举阶段（使用三台服务：server1、server2、server3，默认依次启动）
1. server1启动，发起投票，投给自己，投票包含自己的服务器ID和最新事务ID(ZXID)`(myid,zxid)`，此时只有一台服务启动，它发出去的报文没有任何响应，所以它的选举状态一直都是looking
2. server2启动，发起投票，投给自己，与最开始启动的服务器1进行通信，互相交换自己的选举结果，server1发现自己的zxid没有server2的大，重新发起投票，投票给当前最大的zxid的server；server1就会投票给server2；server2此时2票，通过半数机制发现，此时的票数没有超过一半，所以还是looking状态
3. server3启动，发起投票，和前两个server一样，投给自己，同时与之前启动的服务器1,2交换信息,但此时的server2已经有2票，超过了所有票数的一半，所以server2被选为leader，server3只能成为follower

### 运行选举阶段
1. 此时的leader服务器server2宕机了，此时两个follower就会发起新的选举，server3的zxid比server1的zxid大，所以server1投票给server3，server3成为leader
