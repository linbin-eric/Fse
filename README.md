# fse

## 介绍

高性能Java序列化框架，可以任意Java对象序列化为字节数组并且完成反序列化。支持任意Java类型，序列化的对象不需要特殊接口即可。

## 性能测试

使用业务场景下常见对象进行性能验证，对象内部包含基本属性，字符串，以及其构成的数组和List、Map接口等，在收集性能数据之前首先进行一次序列化操作保证预热。对比数据如下

![](https://markdownpic-1251577930.cos.ap-chengdu.myqcloud.com/20200203132939.png)



## 使用说明

首先在Pom文件中引入依赖，如下

```xml
<dependency>
	<groupId>com.jfireframework</groupId>
    <artifactId>fse</artifactId>
    <version>aegean-1.0</version>
</dependency>
```

API 使用方式如下

```java
Fse fse = new Fse();
TestData data = new TestData();
//创建一个二进制数组容器，用于容纳序列化后的输出。容器大小会在需要时自动扩大，入参仅决定初始化大小。
ByteArray buf = ByteArray.allocate(100);
//执行序列化，会将序列化对象序列化到二进制数组容器之中。
fse.serialize(data, buf);
//得到序列化后的二进制数组结果
byte[] resultBytes = buf.toArray();
//清空容器内容，可以反复使用该容器
buf.clear();
//填入数据，准备进行反序列化
buf.put(resultBytes);
TestData result = (TestData) fse.deSerialize(buf);
assertTrue(result.equals(data));
```

