package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class IntegerArraySerializer extends AbstractArraySerializer<Integer[]>
{
    
    public IntegerArraySerializer()
    {
        super(Integer[].class);
    }
    
    @Override
    public void serialize(Integer[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Integer[] array = src;
        buf.writePositive(array.length);
        for (Integer each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeVarint(each);
            }
        }
    }
    
    @Override
    public Integer[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Integer[] array = new Integer[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readVarint();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Integer[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Integer[] array = new Integer[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readVarint(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
}
