package com.jfireframework.licp;

import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.nio.ByteBuffer;

public class Licp
{
    private final InternalLicp internalLicp = new InternalLicp();

    public Licp disableCycleSupport()
    {
        internalLicp.disableCycleSupport();
        return this;
    }

    public void serialize(Object src, ByteBuf<?> buf)
    {
        internalLicp.serialize(src, buf);
    }

    public Licp register(Class<?>... types)
    {
        internalLicp.register(types);
        return this;
    }

    public <T> T deserialize(ByteBuf<?> buf)
    {
        return internalLicp.deserialize(buf);
    }

    public <T> T deserialize(ByteBuffer buffer)
    {
        return internalLicp.deserialize(buffer);
    }

    public Licp addInterceptor(LicpFieldInterceptor licpInterceptor)
    {
        internalLicp.addFieldInterceptor(licpInterceptor);
        return this;
    }
}
