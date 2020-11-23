## CopyOnWriteArrayList
> 【写时复制】写操作加锁，内部直接复制原来的list，进行容量+1；读操作不加锁。用在读特别多，写特别少的情况下