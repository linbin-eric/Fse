package com.jfireframework.licp.serializer.base;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class DoubleSerializer implements LicpSerializer<Double>
{
    
    @Override
    public void serialize(Double src, ByteBuf<?> buf, InternalLicp licp)
    {
        buf.writeDouble(src);
    }
    
    @Override
    public Double deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        Double d = buf.readDouble();
        licp.putObject(d);
        return d;
    }
    
    @Override
    public Double deserialize(ByteBuffer buf, InternalLicp licp)
    {
        Double d = BufferUtil.readDouble(buf);
        licp.putObject(d);
        return d;
    }
    
}
