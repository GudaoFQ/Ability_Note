* 数据类型方面：
    > redis支持5大类型：String List Hash Set ZSet，这样就支持更多的业务场景，memcached支持的比较少

* 性能方面：
    > redis是单线程处理数据，memcached是多线程处理数据；所以在处理大的数据的时候，memcached性能比较高；但是现在redis也在对大类型数据处理进行改善，跟memcached差不多了

* 集群方面：
    > memcached是原生是不支持集群的，必须通过客户端向集群服务中分片写入数据；redis官方就支持redis cluster集群模式
    