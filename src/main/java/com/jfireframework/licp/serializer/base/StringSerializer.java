package com.jfireframework.licp.serializer.base;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class StringSerializer implements LicpSerializer<String>
{
    
    @Override
    public void serialize(String src, ByteBuf<?> buf, InternalLicp licp)
    {
        String value = src;
        buf.writeString(value);
    }
    
    @Override
    public String deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        String value = buf.readString();
        licp.putObject(value);
        return value;
    }
    
    @Override
    public String deserialize(ByteBuffer buf, InternalLicp licp)
    {
        String value = BufferUtil.readString(buf);
        licp.putObject(value);
        return value;
    }
    
}
