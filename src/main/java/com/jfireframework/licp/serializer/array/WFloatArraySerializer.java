package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class WFloatArraySerializer extends AbstractArraySerializer<Float[]>
{
    
    public WFloatArraySerializer()
    {
        super(Float[].class);
    }
    
    @Override
    public void serialize(Float[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Float[] array = src;
        buf.writePositive(array.length);
        for (Float each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.writeFloat(each);
            }
        }
    }
    
    @Override
    public Float[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Float[] array = new Float[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.readFloat();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Float[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Float[] array = new Float[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = BufferUtil.readFloat(buf);
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
}
