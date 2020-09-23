## 问题总结

#### zk的节点中只能直接存储字符吗？

```shell 
zk中是不能直接存储字符串的，它默认存储的都是字节数组；因此zk的节点中就能存储任何东西【但不推荐存储图片等数据量大的东西】
```

#### zk中有哪几种节点类型，分别是什么？

```shell
PERSISTENT            持久节点{create path}
PERSISTENT_SEQUENTIAL 持久序号节点{create -s path}
EPHEMERAL             临时节点【不能再有子节点】{create -e path}
EPHEMERAL_SEQUENTIAL  临时序号节点【不能再有子节点】{create -e -s path}
```