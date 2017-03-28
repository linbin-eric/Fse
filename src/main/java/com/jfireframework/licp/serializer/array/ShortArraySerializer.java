package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class ShortArraySerializer extends AbstractArraySerializer<short[]>
{
    
    public ShortArraySerializer()
    {
        super(short[].class);
    }
    
    @Override
    public void serialize(short[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        short[] array = src;
        buf.writePositive(array.length);
        for (short each : array)
        {
            buf.writeShort(each);
        }
    }
    
    @Override
    public short[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        short[] array = new short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readShort();
        }
        return array;
    }
    
    @Override
    public short[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        short[] array = new short[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readShort(buf);
        }
        return array;
    }
    
}
