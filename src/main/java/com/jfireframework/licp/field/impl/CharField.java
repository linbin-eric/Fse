package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class CharField extends AbstractCacheField
{
    
    public CharField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        char c = unsafe.getChar(holder, offset);
        if (fieldInterceptor != null)
        {
            c = fieldInterceptor.serializeChar(c);
        }
        buf.writeVarChar(c);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        char c = buf.readVarChar();
        if (fieldInterceptor != null)
        {
            c = fieldInterceptor.deserializeChar(c);
        }
        unsafe.putChar(holder, offset, c);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        char c = BufferUtil.readVarChar(buf);
        if (fieldInterceptor != null)
        {
            c = fieldInterceptor.deserializeChar(c);
        }
        unsafe.putChar(holder, offset, c);
    }
    
}
