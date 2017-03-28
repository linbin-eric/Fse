package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class WBooleanArraySerializer extends AbstractArraySerializer<Boolean[]>
{
    
    public WBooleanArraySerializer()
    {
        super(Boolean[].class);
    }
    
    @Override
    public void serialize(Boolean[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Boolean[] array = src;
        buf.writePositive(array.length);
        for (Boolean each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else if (each == true)
            {
                buf.put((byte) 1);
            }
            else
            {
                buf.put((byte) 2);
            }
        }
    }
    
    @Override
    public Boolean[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Boolean[] array = new Boolean[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            byte b = buf.get();
            if (b == 0)
            {
                array[i] = null;
            }
            else if (b == 1)
            {
                array[i] = true;
            }
            else
            {
                array[i] = false;
            }
        }
        return array;
    }
    
    @Override
    public Boolean[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Boolean[] array = new Boolean[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            byte b = buf.get();
            if (b == 0)
            {
                array[i] = null;
            }
            else if (b == 1)
            {
                array[i] = true;
            }
            else
            {
                array[i] = false;
            }
        }
        return array;
    }
}
