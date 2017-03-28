package com.jfireframework.licp.serializer.array;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.util.BufferUtil;

public class LongArraySerializer extends AbstractArraySerializer<long[]>
{
    
    public LongArraySerializer()
    {
        super(long[].class);
    }
    
    @Override
    public void serialize(long[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        long[] array = src;
        buf.writePositive(array.length);
        for (long each : array)
        {
            buf.writeVarLong(each);
        }
    }
    
    @Override
    public long[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        long[] array = new long[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readVarLong();
        }
        return array;
    }
    
    @Override
    public long[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        long[] array = new long[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readVarLong(buf);
        }
        return array;
    }
    
}
