package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class DoubleArraySerializer extends AbstractArraySerializer<double[]>
{
    
    public DoubleArraySerializer()
    {
        super(double[].class);
    }
    
    @Override
    public void serialize(double[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        double[] array = src;
        buf.writePositive(array.length);
        for (double each : array)
        {
            buf.writeDouble(each);
        }
    }
    
    @Override
    public double[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        double[] array = new double[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readDouble();
        }
        return array;
    }
    
    @Override
    public double[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        double[] array = new double[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readDouble(buf);
        }
        return array;
    }
    
}
