package com.jfireframework.licp;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

public class Licp
{
    private final InternalLicp internalLicp = new InternalLicp();
    
    public void disableCycleSupport()
    {
        internalLicp.disableCycleSupport();
    }
    
    public void serialize(Object src, ByteBuf<?> buf)
    {
        internalLicp.serialize(src, buf);
    }
    
    public void register(Class<?>... types)
    {
        internalLicp.register(types);
    }
    
    public <T> T deserialize(ByteBuf<?> buf)
    {
        return internalLicp.deserialize(buf);
    }
    
    public <T> T deserialize(ByteBuffer buffer)
    {
        return internalLicp.deserialize(buffer);
    }
    
    public void addInterceptor(LicpFieldInterceptor licpInterceptor)
    {
        internalLicp.addFieldInterceptor(licpInterceptor);
    }
}
