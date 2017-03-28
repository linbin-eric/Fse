package com.jfireframework.licp.serializer.base;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

public class FloatSerializer implements LicpSerializer<Float>
{
    
    @Override
    public void serialize(Float src, ByteBuf<?> buf, InternalLicp licp)
    {
        buf.writeFloat(src);
    }
    
    @Override
    public Float deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        Float f = buf.readFloat();
        licp.putObject(f);
        return f;
    }
    
    @Override
    public Float deserialize(ByteBuffer buf, InternalLicp licp)
    {
        Float f = BufferUtil.readFloat(buf);
        licp.putObject(f);
        return f;
    }
    
}
