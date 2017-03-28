package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class LongField extends AbstractCacheField
{
    
    public LongField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        long value = unsafe.getLong(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeLong(value);
        }
        buf.writeVarLong(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        long value = buf.readVarLong();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeLong(value);
        }
        unsafe.putLong(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        long value = BufferUtil.readVarLong(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeLong(value);
        }
        unsafe.putLong(holder, offset, value);
    }
    
}
