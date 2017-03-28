package com.jfireframework.licp.field;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;

public interface CacheField
{
    public String getName();
    
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp);
    
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp);
    
    public void read(Object holder, ByteBuffer buf, InternalLicp licp);
    
}
