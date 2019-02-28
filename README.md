基于springBoot2实现的一个简单的文件下载限流的demo

### 限流的主要实现思路：
定义一个chunk块以及允许的最大速率 maxRate(单位 KB/s)。  
通过maxRate我们可以算出，在maxRate的速率下，通过当前chunk块大小的字节流所需要的时间 `timeCostPerChunk`
在读取/写入字节时，我们维护一个已经读取/写入的字节量 `bytesWillBeSentOrReceive`。  
当bytesWillBeSentOrReceive的值达到chunk的大小时，检查期间过去的时间`(nowNanoTime-lastPieceSentOrReceiveTick)`  
如果过去的时间小于了 `timeCostPerChunk`的值，说明当前的速率已经超过了 maxRate的速率，这时候就需要休眠一会来限制流量  
如果速率没超过或者休眠完后 将 `bytesWillBeSentOrReceive=bytesWillBeSentOrReceive-chunk`  
之后继续检查

具体算法代码实现可以看`BandwidthLimiter`类