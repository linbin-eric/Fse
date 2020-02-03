package com.jfireframework.fse;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SimpleTest
{
    @Test
    public void test()
    {
        SimplePerson person = new SimplePerson();
        person.setName("林斌123as");
        person.setAge(27);
        person.setWeight(15.65f);
        System.out.println(person);
        Fse       licp = new Fse();
        ByteArray buf  = ByteArray.allocate(100);
        licp.serialize(person, buf);
        SimplePerson result = (SimplePerson) licp.deSerialize(buf);
        System.out.println(result);
        assertTrue(person.equals(result));
    }

    @Test
    public void test2()
    {
        Fse      fse  = new Fse();
        TestData data = new TestData();
        //创建一个二进制数组容器，用于容纳序列化后的输出。容器大小会在需要时自动扩大，入参仅决定初始化大小。
        ByteArray buf  = ByteArray.allocate(100);
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
    }
}
