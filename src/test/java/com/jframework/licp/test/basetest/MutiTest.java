package com.jframework.licp.test.basetest;

import org.junit.Rule;
import org.junit.Test;
import com.jfire.framework.ex.test.rule.CustomRule;
import com.jfire.framework.ex.test.rule.MutiThreadTest;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.baseutil.collection.buffer.HeapByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jframework.licp.test.basetest.data.Person;

public class MutiTest
{
    @Rule
    public CustomRule rule = new CustomRule();
    
    @Test
    @MutiThreadTest(repeatTimes = 30, threadNums = 10)
    public void test()
    {
        ByteBuf<?> buf = HeapByteBuf.allocate(100);
        InternalLicp lbse = new InternalLicp();
        Person person = new Person();
        lbse.serialize(person, buf);
        lbse.deserialize(buf, Person.class);
    }
}
