package com.jfireframework.licp.serializer.base;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class LongSerializer implements LicpSerializer<Long>
{
    
    @Override
    public void serialize(Long src, ByteBuf<?> buf, InternalLicp licp)
    {
        buf.writeVarLong(src);
    }
    
    @Override
    public Long deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        Long l = buf.readVarLong();
        licp.putObject(l);
        return l;
    }
    
    @Override
    public Long deserialize(ByteBuffer buf, InternalLicp licp)
    {
        Long l = BufferUtil.readVarLong(buf);
        licp.putObject(l);
        return l;
    }
    
}
