package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class WByteArraySerializer extends AbstractArraySerializer<Byte[]>
{
    
    public WByteArraySerializer()
    {
        super(Byte[].class);
    }
    
    @Override
    public void serialize(Byte[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        Byte[] array = src;
        buf.writePositive(array.length);
        for (Byte each : array)
        {
            if (each == null)
            {
                buf.put((byte) 0);
            }
            else
            {
                buf.put((byte) 1);
                buf.put(each);
            }
        }
    }
    
    @Override
    public Byte[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        Byte[] array = new Byte[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.get();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
    
    @Override
    public Byte[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        Byte[] array = new Byte[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            boolean exist = buf.get() == 1 ? true : false;
            if (exist)
            {
                array[i] = buf.get();
            }
            else
            {
                array[i] = null;
            }
        }
        return array;
    }
}
