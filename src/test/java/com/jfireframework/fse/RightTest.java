package com.jfireframework.fse;

import com.jfireframework.baseutil.encrypt.AesUtil;
import com.jfireframework.fse.data.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static org.junit.Assert.*;

public class RightTest
{
    ByteArray buf = ByteArray.allocate();
    private boolean useCompile = false;

    @Test
    public void baseTypeTest() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException
    {
        buf.clear();
        // 创建需要序列化的对象
        BaseData baseData = new BaseData(1);
        // 构建lbse对象，该对象是非线程安全的，请注意
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        context.register(BaseData.class);
        // 进行序列化，返回的是一个buffer对象
        context.serialize(baseData, buf);
        // 传入二进制buffer对象，读取其中的 数据并且反序列化成对象
        BaseData result = (BaseData) context.deSerialize(buf);
        assertEquals(result.getA(), baseData.getA());
        assertEquals(result.isB(), baseData.isB());
        assertEquals(result.getC(), baseData.getC());
        assertEquals(result.getD(), baseData.getD());
        assertEquals(result.getE(), baseData.getE());
        assertEquals(result.getF(), baseData.getF());
        assertEquals(result.getG(), baseData.getG(), 0.01);
        assertEquals(result.getI(), baseData.getI());
        assertEquals(result.getH(), baseData.getH(), 0.01);
        for (int i = 0; i < result.getJ().length; i++)
        {
            assertEquals(result.getJ()[i], baseData.getJ()[i]);
        }
        for (int i = 0; i < result.getK().length; i++)
        {
            assertEquals(result.getK()[i], baseData.getK()[i]);
        }
        for (int i = 0; i < result.getL().length; i++)
        {
            assertEquals(result.getL()[i], baseData.getL()[i]);
        }
        for (int i = 0; i < result.getM().length; i++)
        {
            assertEquals(result.getM()[i], baseData.getM()[i]);
        }
        for (int i = 0; i < result.getN().length; i++)
        {
            assertEquals(result.getN()[i], baseData.getN()[i]);
        }
        for (int i = 0; i < result.getO().length; i++)
        {
            assertEquals(result.getO()[i], baseData.getO()[i]);
        }
        for (int i = 0; i < result.getP().length; i++)
        {
            assertEquals(result.getP()[i], baseData.getP()[i], 0.1);
        }
        for (int i = 0; i < result.getQ().length; i++)
        {
            assertEquals(result.getQ()[i], baseData.getQ()[i], 0.1);
        }
        for (int i = 0; i < result.getR().length; i++)
        {
            assertEquals(result.getR()[i], baseData.getR()[i]);
        }
        //
    }

    @Test
    public void wrapTest() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException
    {
        buf.clear();
        WrapData wrapData = new WrapData();
        Fse      context  = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        context.register(WrapData.class);
        context.serialize(wrapData, buf);
        WrapData result = (WrapData) context.deSerialize(buf);
        assertEquals(result.getA(), wrapData.getA());
        assertEquals(result.getB(), wrapData.getB());
        assertEquals(result.getC(), wrapData.getC());
        assertEquals(result.getD(), wrapData.getD());
        assertEquals(result.getE(), wrapData.getE());
        assertEquals(result.getF(), wrapData.getF());
        assertEquals(result.getG(), wrapData.getG(), 0.01);
        assertEquals(result.getH(), wrapData.getH(), 0.01);
        assertEquals(result.getI(), wrapData.getI());
        for (int i = 0; i < result.getJ().length; i++)
        {
            assertEquals(result.getJ()[i], wrapData.getJ()[i]);
        }
        for (int i = 0; i < result.getK().length; i++)
        {
            assertEquals(result.getK()[i], wrapData.getK()[i]);
        }
        for (int i = 0; i < result.getL().length; i++)
        {
            assertEquals(result.getL()[i], wrapData.getL()[i]);
        }
        for (int i = 0; i < result.getM().length; i++)
        {
            assertEquals(result.getM()[i], wrapData.getM()[i]);
        }
        for (int i = 0; i < result.getN().length; i++)
        {
            assertEquals(result.getN()[i], wrapData.getN()[i]);
        }
        for (int i = 0; i < result.getO().length; i++)
        {
            assertEquals(result.getO()[i], wrapData.getO()[i]);
        }
        for (int i = 0; i < result.getP().length; i++)
        {
            assertEquals(result.getP()[i], wrapData.getP()[i], 0.1);
        }
        for (int i = 0; i < result.getQ().length; i++)
        {
            assertEquals(result.getQ()[i], wrapData.getQ()[i], 0.1);
        }
        for (int i = 0; i < result.getR().length; i++)
        {
            assertEquals(result.getR()[i], wrapData.getR()[i]);
        }
        for (int i = 0; i < wrapData.getList().size(); i++)
        {
            Assert.assertTrue(wrapData.getList().get(i).equals(result.getList().get(i)));
        }
        for (int i = 0; i < wrapData.getMap().size(); i++)
        {
            Assert.assertTrue(wrapData.getMap().get(i).equals(result.getMap().get(i)));
        }
        for (int i = 0; i < wrapData.getW().length; i++)
        {
            for (int j = 0; j < wrapData.getW()[i].length; j++)
            {
                Assert.assertEquals(result.getW()[i][j], wrapData.getW()[i][j]);
            }
        }
    }

    @Test
    public void referenceTest() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException
    {
        Person person  = new Person("linbin", 25);
        Person tPerson = new Person("zhangshi[in", 30);
        person.setLeader(tPerson);
        tPerson.setLeader(person);
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(person, buf);
        Person result = (Person) context.deSerialize(buf);
        assertEquals("zhangshi[in", result.getLeader().getName());
    }

    @Test
    public void objectTest() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        Calendar calendar = Calendar.getInstance();
        buf.clear();
        context.serialize(calendar, buf);
        Calendar reCalendar = (Calendar) context.deSerialize(buf);
        Assert.assertTrue(reCalendar.equals(calendar));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void listTest() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        ArrayList<BaseData> list = new ArrayList<BaseData>();
        for (int i = 0; i < 5; i++)
        {
            list.add(new BaseData(i));
        }
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(list, buf);
        ArrayList<BaseData> result = (ArrayList<BaseData>) context.deSerialize(buf);
        Assert.assertTrue(list.equals(result));
    }

    @Test
    public void baseDataTest()
    {
        Fse fse = new Fse();
        if (useCompile)
        {
            fse.useCompile();
        }
        buf.clear();
        BaseData baseData = new BaseData();
        fse.serialize(baseData, buf);
        BaseData result = (BaseData) fse.deSerialize(buf);
        assertTrue(result.equals(baseData));
    }

    @Test
    public void objectArrayTest()
    {
        Object[] array = new Object[4];
        array[0] = new Person();
        array[1] = new BaseData();
        array[2] = new LongData();
        array[3] = new WrapData();
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(array, buf);
        Object[] result = (Object[]) context.deSerialize(buf);
        Assert.assertTrue(((Person) result[0]).equals(array[0]));
    }

    @Test
    public void byteArrayTest()
    {
        byte[] array   = new byte[]{1, 2, 5, 6, 8, 9};
        Fse    context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(array, buf);
        byte[] result = (byte[]) context.deSerialize(buf);
        for (int i = 0; i < array.length; i++)
        {
            assertEquals(array[i], result[i]);
        }
    }

    @Test
    public void booleanArrayTest()
    {
        boolean[] array   = new boolean[]{true, false, false, true, true, true};
        Fse       context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        context.serialize(array, buf);
        boolean[] result = (boolean[]) context.deSerialize(buf);
        for (int i = 0; i < array.length; i++)
        {
            assertEquals(array[i], result[i]);
        }
    }

    @Test
    public void arrayDataTest()
    {
        Fse context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(new ArrayData(), buf);
        context.deSerialize(buf);
    }

    @Test
    public void objectArrTest()
    {
        Random random = new Random();
        byte[] key    = new byte[16];
        random.nextBytes(key);
        AesUtil  aesUtil = new AesUtil(key);
        Object[] data    = new Object[]{Integer.valueOf(14), new BaseData[]{new BaseData(), new BaseData()}};
        Fse      context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(data, buf);
        byte[] aesResult = aesUtil.encrypt(buf.toArray());
        buf.clear();
        buf.put(aesUtil.decrypt(aesResult));
        Object[] result = (Object[]) context.deSerialize(buf);
        assertEquals(14, result[0]);
        assertEquals(((BaseData[]) data[1])[0], ((BaseData[]) result[1])[0]);
        assertEquals(((BaseData[]) data[1])[1], ((BaseData[]) result[1])[1]);
    }

    /**
     * 不注册类型直接序列化时，数组实例多次被引用能够正确序列化
     */
    @Test
    public void arryaNotRegisterClassSeri()
    {
        ArrayRefenceHolder holder  = new ArrayRefenceHolder();
        Fse                context = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(holder, buf);
        ArrayRefenceHolder result = (ArrayRefenceHolder) context.deSerialize(buf);
        assertArrayEquals(new int[]{1, 2}, holder.getA()[0]);
        assertArrayEquals(new int[]{3, 4}, holder.getA()[1]);
        assertArrayEquals(new int[]{1, 2}, holder.getB()[0]);
        assertArrayEquals(new int[]{3, 4}, holder.getB()[1]);
    }

    @Test
    public void methodObjectTest() throws NoSuchMethodException
    {
        Method methodObjectTest = this.getClass().getDeclaredMethod("methodObjectTest");
        Fse    context          = new Fse();
        if (useCompile)
        {
            context.useCompile();
        }
        buf.clear();
        context.serialize(methodObjectTest, buf);
        Method method = (Method) context.deSerialize(buf);
        System.out.println(method.equals(methodObjectTest));
        assertEquals(methodObjectTest, method);
    }
}
