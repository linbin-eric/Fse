package com.jfireframework.fse;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianOutput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jfireframework.fse.data.Device;
import com.jfireframework.fse.data.Person;
import com.jfireframework.fse.data.WrapData;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class SpeedTest
{
    public static int    testSum = 1000;
    private       Logger logger  = LoggerFactory.getLogger(SpeedTest.class);

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
    public void longtest()
    {
        Object data    = new WrapData();
        Person person  = new Person("linbin", 25);
        Person tPerson = new Person("zhangshi[in", 30);
        person.setLeader(tPerson);
        tPerson.setLeader(person);
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        Output output = null;
        output = new Output(4096, 109096);
        kryo.writeClassAndObject(output, data);
        System.out.println(output.toBytes().length);
        Fse       licp = new Fse();
        ByteArray buf  = ByteArray.allocate(4058);
        licp.serialize(data, buf);
        System.out.println(buf.toArray().length);
    }

    @Test
    public void serialize() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException
    {
        Person person  = new Person("linbin", 25);
        Person tPerson = new Person("zhangshi[in", 30);
        person.setLeader(tPerson);
        doSerializeTest(person, 40000);
    }

    @Test
    public void serializeSmall() throws IOException
    {
        TestData testData = new TestData();
        doSerializeTest(testData, 400000);
    }

    private void doSerializeTest(Object obj, int testSum) throws IOException
    {
        Fse       context   = new Fse();
        ByteArray byteArray = ByteArray.allocate();
        byteArray.clear();
        context.serialize(obj, byteArray);
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            byteArray.clear();
            context.serialize(obj, byteArray);
        }
        long fseCose = System.currentTimeMillis() - t0;
        logger.info("fse序列化耗时：{}", fseCose);
        Kryo   kryo   = new Kryo();
        Output output = null;
        output = new Output(4096, 109096);
        output.clear();
        kryo.writeClassAndObject(output, obj);
        t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            output.clear();
            kryo.writeClassAndObject(output, obj);
        }
        long kryoCost = System.currentTimeMillis() - t0;
        logger.info("kryo序列化耗时{}", kryoCost);
        logger.info("fse比kryo快{}毫秒,速度比kryo快{}倍", (kryoCost - fseCose), ((float) kryoCost / fseCose) - 1);
        ByteOutputStream   byteOutputStream   = new ByteOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(obj);
        byteOutputStream.reset();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            objectOutputStream.reset();
            byteOutputStream.reset();
            objectOutputStream.writeObject(obj);
        }
        long jdkCost = System.currentTimeMillis() - t0;
        logger.debug("Java原生序列化耗时{}", jdkCost);
        logger.info("fse比原生快{}倍", ((float) jdkCost / fseCose) - 1);
        byteOutputStream = new ByteOutputStream();
        Hessian2Output hessianOutput = new Hessian2Output(byteOutputStream);
        hessianOutput.writeObject(obj);
        byteOutputStream.reset();
        t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            byteOutputStream.reset();
            hessianOutput.resetReferences();
            hessianOutput.writeObject(obj);
        }
        long hessisonCost = System.currentTimeMillis() - t0;
        logger.info("hession 序列化耗时：{}", hessisonCost);
        logger.info("fse比hession快{}倍", ((float) hessisonCost / fseCose) - 1);
    }

    @Test
    public void deserialize()
    {
        int    testSum = 3000000;
        Person person  = new Person("linbin", 25);
        Person tPerson = new Person("zhangshi[in", 30);
        person.setLeader(tPerson);
        Fse       context   = new Fse();
        Device    device    = Builder();
        ByteArray byteArray = ByteArray.allocate();
        context.serialize(device, byteArray);
        logger.debug("fse输出长度:{}", byteArray.getWritePosi());
        byteArray.setReadPosi(0);
        context.deSerialize(byteArray);
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            byteArray.setReadPosi(0);
            context.deSerialize(byteArray);
        }
        long fseCost = System.currentTimeMillis() - t0;
        logger.info("fse逆序列化耗时：{}", fseCost);
        Kryo   kryo   = new Kryo();
        Output output = null;
        output = new Output(4096, 109096);
        output.clear();
        kryo.writeClassAndObject(output, device);
        byte[] bb = output.toBytes();
        logger.debug("kryo输出长度:{}", bb.length);
        Input input = null;
        input = new Input(bb);
        input.setPosition(0);
        kryo.readClassAndObject(input);
        t0 = System.currentTimeMillis();
        for (int i = 0; i < testSum; i++)
        {
            input.setPosition(0);
            kryo.readClassAndObject(input);
        }
        long kryoCost = System.currentTimeMillis() - t0;
        logger.info("kryo逆序列化耗时{}", kryoCost);
        logger.info("fse比kryo快{},性能比是{}倍", (kryoCost - fseCost), ((float) kryoCost / fseCost));
    }
}
