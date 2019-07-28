# <center>通过Lua脚本批量插入数据到布隆过滤器</center>

有关布隆过滤器和这个项目说明写了两篇博客

1、 [布隆过滤器原理](https://www.cnblogs.com/qdhxhz/p/11237246.html)

2、[通过Lua脚本批量插入数据到Redis布隆过滤器](https://www.cnblogs.com/qdhxhz/p/11259078.html)

下面就这个项目做个整体简单介绍。

## <font color=#FFD700>一、项目概述</font>

#### 1、技术架构

项目总体技术选型

```
SpringBoot2.1.6 + Maven3.5.4 +Redis +lombok(插件)+Stopwatch(计时工具)
```



## <font color=#FFD700>二、项目实现功能</font>

#### 1对以下方法进行性能测试比较

​     1）、List的 contains 方法

​     2）、Map的 containsKey 方法

​     3）、Google布隆过滤器 mightContain 方法

#### 2、SpringBoot整合Redis布隆过滤器

   实现通过Lua脚本批量插入数据到redis布隆过滤器 

   并判断当前key值在redis布隆过滤器中是否存在。

`补充` 

既然可以通过Lua脚本可以实现批量插入数据到Redis服务器中，那么在实际开发过程中，如果涉及到批量插入数据到Redis服务器，那么就可以考虑通过Lua脚本实现。