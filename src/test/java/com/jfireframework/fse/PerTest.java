package com.jfireframework.fse;

import com.jfireframework.fse.data.Device;
import com.jfireframework.fse.data.SpeedData2;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PerTest
{
    public static int    testSum = 100;
    private       Logger logger  = LoggerFactory.getLogger(PerTest.class);

    private Device Builder()
    {
        Device device = new Device();
        device.setActivationTime(new Date());
        device.setBound(true);
        device.setBuildVersion(1);
        device.setId(9876543210L);
        device.setIdfa("照片没问wqeqw");
        device.setImei("照片没wewqe问");
        device.setMac("照qw片没问");
        device.setMajorVersion(3);
        device.setMinorVersion(6);
        device.setOpenUdid(RandomString.randomString(48));
        device.setOs(3);
        device.setOsVersion("照qwqw片没问");
        device.setPromoPlatformCode(94000000);
        device.setUuid("照片没qq问");
        device.setSn(device.getOpenUdid() + "_" + device.getUuid());
        device.setUserId(1234567890L);
        return device;
    }

    @Test
    public void longTest()
    {
    }

    @Test
    public void test()
    {
        int       testSum   = 1000000;
        Object    data      = new SpeedData2();
        Fse       context   = new Fse();
        ByteArray byteArray = ByteArray.allocate();
        long      t0        = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            byteArray.clear();
            context.serialize(data, byteArray);
        }
        long lbseCost = System.currentTimeMillis() - t0;
        logger.info("lbse序列化耗时：{}", lbseCost);
    }

    @Test
    public void test2()
    {
        Device    device    = Builder();
        Fse       context   = new Fse();
        ByteArray byteArray = ByteArray.allocate();
        context.serialize(device, byteArray);
        for (int i = 0; i < testSum; i++)
        {
            context.deSerialize(byteArray);
            byteArray.setReadPosi(0);
        }
    }
}
