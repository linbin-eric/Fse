package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class WShortArraySerializer extends AbstractArraySerializer<Short[]>
{
    
    public WShortArraySerializer()
    {
        super(Short[].class);
    }
    
    @Override
    public void serialize(Short[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Short[] array = src;
        buf.writePositive(array.length);
        for (Short each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeShort(each);
            }
        }
    }
    
    @Override
    public Short[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Short[] array = new Short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readShort();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Short[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Short[] array = new Short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readShort(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
}
