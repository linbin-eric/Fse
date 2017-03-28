package com.jfireframework.licp.serializer.base;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

public class IntegerSerializer implements LicpSerializer<Integer>
{
    
    @Override
    public void serialize(Integer src, ByteBuf<?> buf, InternalLicp licp)
    {
        buf.writeVarint(src);
    }
    
    @Override
    public Integer deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        Integer i = buf.readVarint();
        licp.putObject(i);
        return i;
    }
    
    @Override
    public Integer deserialize(ByteBuffer buf, InternalLicp licp)
    {
        Integer i = BufferUtil.readVarint(buf);
        licp.putObject(i);
        return i;
    }
    
}
