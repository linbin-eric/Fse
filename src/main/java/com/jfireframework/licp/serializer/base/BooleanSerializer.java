package com.jfireframework.licp.serializer.base;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.LicpSerializer;

public class BooleanSerializer implements LicpSerializer<Boolean>
{
    
    @Override
    public void serialize(Boolean src, ByteBuf<?> buf, InternalLicp licp)
    {
        if (src)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
        }
    }
    
    @Override
    public Boolean deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        if (buf.get() == 1)
        {
            licp.putObject(true);
            return true;
        }
        else
        {
            licp.putObject(false);
            return false;
        }
    }
    
    @Override
    public Boolean deserialize(ByteBuffer buf, InternalLicp licp)
    {
        if (buf.get() == 1)
        {
            licp.putObject(true);
            return true;
        }
        else
        {
            licp.putObject(false);
            return false;
        }
    }
    
}
