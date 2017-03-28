package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

public class WByteField extends AbstractCacheField
{
    
    public WByteField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Byte b = (Byte) unsafe.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            b = fieldInterceptor.serialize(b);
        }
        if (b == null)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
            buf.put(b);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        byte b = buf.get();
        Byte value;
        if (b == 0)
        {
            value = null;
        }
        else
        {
            value = buf.get();
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        unsafe.putObject(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        byte b = buf.get();
        Byte value;
        if (b == 0)
        {
            value = null;
        }
        else
        {
            value = buf.get();
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        unsafe.putObject(holder, offset, value);
    }
    
}
