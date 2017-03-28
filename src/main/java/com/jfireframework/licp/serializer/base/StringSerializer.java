package com.jfireframework.licp.serializer.base;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

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
