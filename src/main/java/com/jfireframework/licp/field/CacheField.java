package com.jfireframework.licp.field;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;

import java.nio.ByteBuffer;

public interface CacheField
{
    public String getName();
    
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp);
    
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp);
    
    public void read(Object holder, ByteBuffer buf, InternalLicp licp);
    
}
