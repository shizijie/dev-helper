1.redis底层数据结构 （参考连接 https://baijiahao.baidu.com/s?id=1651767862408344160&wfr=spider&for=pc）
    动态字符串sds组成：
        1.free 还剩多少空间
        2.len 字符串长度
        3.buf 存放的字符数组


2.redis客户端区别
    1.lettuce (赖贴死)
        连接是基于Netty的，连接实例（StatefulRedisConnection）可以在多个线程间并发访问，应为StatefulRedisConnection是线程安全的，
        所以一个连接实例（StatefulRedisConnection）就可以满足多线程环境下的并发访问，当然这个也是可伸缩的设计，一个连接实例不够的情况
        也可以按需增加连接实例（默认是一个线程连接实例）
    2.jedis
        Jedis在实现上是直接连接的redis server，如果在多线程环境下是非线程安全的，这个时候只有使用连接池，为每个Jedis实例增加物理连接