package com.jframework.licp.test.basetest;

import static org.junit.Assert.*;
import org.junit.Test;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.baseutil.collection.buffer.HeapByteBuf;
import com.jfireframework.licp.InternalLicp;

public class SimpleTest
{
    @Test
    public void test()
    {
        Person person = new Person();
        person.setName("林斌123as");
        person.setAge(27);
        person.setWeight(15.65f);
        InternalLicp licp = new InternalLicp();
        ByteBuf<?> buf = HeapByteBuf.allocate(100);
        licp.serialize(person, buf);
        Person result = (Person) licp.deserialize(buf);
        assertTrue(person.equals(result));
    }
    
    @Test
    public void test2()
    {
        InternalLicp licp = new InternalLicp();
        TestData data = new TestData();
        ByteBuf<?> buf = HeapByteBuf.allocate(100);
        licp.serialize(data, buf);
        TestData result = (TestData) licp.deserialize(buf);
        assertTrue(result.equals(data));
    }
}
