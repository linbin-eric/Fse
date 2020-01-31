package com.jfireframework.fse;

import org.junit.Assert;
import org.junit.Test;

public class BaseTest
{
    @Test
    public void test()
    {
        ByteArray byteArray = ByteArray.allocate();
        Fse       fse       = new Fse();
        User      user      = new User();
        user.setAge(123);
        user.setName("aaa");
        Home home = new Home();
        home.setAddress("ssss");
        home.setUser(user);
        user.setHome(home);
        fse.serialize(user, byteArray);
        User another = (User) fse.deSerialize(byteArray);
        Assert.assertEquals(user.getAge(), another.getAge());
        Assert.assertEquals(user.getName(), another.getName());
        Home home1 = user.getHome();
        Assert.assertEquals(home.getAddress(), home1.getAddress());
    }
}
