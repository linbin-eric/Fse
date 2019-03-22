package com.jframework.licp.test.basetest;

import java.util.Date;

import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.buf.HeapByteBuf;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jfireframework.licp.InternalLicp;
import com.jframework.licp.test.basetest.data.Device;
import com.jframework.licp.test.basetest.data.SpeedData2;

public class PerTest
{
    private       Logger     logger  = LoggerFactory.getLogger(PerTest.class);
    public static int        testSum = 100;
    private       ByteBuf<?> buf     = HeapByteBuf.allocate(100);
    
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
        int testSum = 1000000;
        Object data = new SpeedData2();
        InternalLicp context = new InternalLicp();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            context.serialize(data, buf.clear());
        }
        long lbseCost = System.currentTimeMillis() - t0;
        logger.info("lbse序列化耗时：{}", lbseCost);
    }
    
    @Test
    public void test2()
    {
        Device device = Builder();
        InternalLicp context = new InternalLicp();
        context.serialize(device, buf);
        for (int i = 0; i < testSum; i++)
        {
            context.deserialize(buf);
            buf.readIndex(0);
        }
    }
}
