package com.jfireframework.fse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.jfireframework.baseutil.StringUtil;
import com.jfireframework.fse.data.BaseData;
import com.jfireframework.fse.data.LongData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class LongTest
{
    private Logger logger = LoggerFactory.getLogger(LongTest.class);

    @Test
    public void longtest() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException
    {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        Output output = null;
        output = new Output(1, 15096);
        kryo.writeClassAndObject(output, new LongData());
        byte[] bb = output.toBytes();
        logger.info("LongData序列化：kryo基础数据长度：{}", bb.length);
        Fse       lbse = new Fse();
        ByteArray buf  = ByteArray.allocate(100);
        lbse.serialize(new LongData(), buf);
        logger.info("LongData序列化：lbse基础数据长度：" + buf.getWritePosi());
        logger.info("序列化长度减少{}", (bb.length - buf.getWritePosi()));
        output = new Output(1, 15096);
        kryo.writeClassAndObject(output, new BaseData(1));
        bb = output.toBytes();
        logger.info("basedata序列化：kryo基础数据长度：{}", bb.length);
        buf.clear();
        lbse.serialize(new BaseData(1), buf);
        logger.info("basedata序列化：lbse基础数据长度：" + buf.getWritePosi());
        logger.info("序列化长度减少{}", (bb.length - buf.getWritePosi()));
    }

    @Test
    public void longtest2() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException
    {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.register(LongData2.class);
        Output output = null;
        output = new Output(1, 15096);
        kryo.writeClassAndObject(output, new LongData2());
        byte[] bb = output.toBytes();
        logger.info("LongData序列化：kryo基础数据长度：{}", bb.length);
        System.out.println(StringUtil.toHexString(bb));
        Fse lbse = new Fse();
        lbse.register(LongData2.class);
        ByteArray buf = ByteArray.allocate(100);
        lbse.serialize(new LongData2(), buf);
        logger.info("LongData序列化：lbse基础数据长度：" + buf.getWritePosi());
        System.out.println(StringUtil.toHexString(buf.toArray()));
        logger.info("序列化长度减少{}", (bb.length - buf.getWritePosi()));
    }
}
