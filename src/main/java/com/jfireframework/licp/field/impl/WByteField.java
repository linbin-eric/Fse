package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class WByteField extends AbstractCacheField
{
    
    public WByteField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Byte b = (Byte) UNSAFE.getObject(holder, offset);
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
        UNSAFE.putObject(holder, offset, value);
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
        UNSAFE.putObject(holder, offset, value);
    }
    
}
