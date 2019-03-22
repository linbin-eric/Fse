package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class IntArraySerializer extends AbstractArraySerializer<int[]>
{
    
    public IntArraySerializer()
    {
        super(int[].class);
    }
    
    @Override
    public void serialize(int[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        int[] array = src;
        buf.writePositive(array.length);
        for (int each : array)
        {
            buf.writeVarint(each);
        }
    }
    
    @Override
    public int[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        int[] array = new int[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readVarint();
        }
        return array;
    }
    
    @Override
    public int[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        int[] array = new int[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readVarint(buf);
        }
        return array;
    }
    
}
