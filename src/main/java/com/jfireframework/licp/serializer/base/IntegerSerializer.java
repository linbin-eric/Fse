package com.jfireframework.licp.serializer.base;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

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
