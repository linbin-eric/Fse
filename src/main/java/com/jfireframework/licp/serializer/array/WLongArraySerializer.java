package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class WLongArraySerializer extends AbstractArraySerializer<Long[]>
{
    
    public WLongArraySerializer()
    {
        super(Long[].class);
    }
    
    @Override
    public void serialize(Long[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Long[] array = src;
        buf.writePositive(array.length);
        for (Long each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeVarLong(each);
            }
        }
    }
    
    @Override
    public Long[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Long[] array = new Long[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readVarLong();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Long[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Long[] array = new Long[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readVarLong(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
}
