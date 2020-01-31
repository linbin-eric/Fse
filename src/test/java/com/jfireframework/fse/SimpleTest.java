package com.jfireframework.fse;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SimpleTest
{
    @Test
    public void test()
    {
        Person person = new Person();
        person.setName("林斌123as");
        person.setAge(27);
        person.setWeight(15.65f);
        System.out.println(person);
        Fse       licp = new Fse();
        ByteArray buf  = ByteArray.allocate(100);
        licp.serialize(person, buf);
        Person result = (Person) licp.deSerialize(buf);
        System.out.println(result);
        assertTrue(person.equals(result));
    }

    @Test
    public void test2()
    {
        Fse       licp = new Fse();
        TestData  data = new TestData();
        ByteArray buf  = ByteArray.allocate(100);
        licp.serialize(data, buf);
        TestData result = (TestData) licp.deSerialize(buf);
        assertTrue(result.equals(data));
    }
}
