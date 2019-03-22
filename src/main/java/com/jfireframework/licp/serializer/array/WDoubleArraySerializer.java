package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class WDoubleArraySerializer extends AbstractArraySerializer<Double[]>
{
    
    public WDoubleArraySerializer()
    {
        super(Double[].class);
    }
    
    @Override
    public void serialize(Double[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Double[] array = src;
        buf.writePositive(array.length);
        for (Double each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeDouble(each);
            }
        }
    }
    
    @Override
    public Double[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Double[] array = new Double[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readDouble();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Double[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        
        int length = BufferUtil.readPositive(buf);
        Double[] array = new Double[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readDouble(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
}
