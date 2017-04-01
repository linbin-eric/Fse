package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class WDoubleField extends AbstractCacheField
{
    
    public WDoubleField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Double d = (Double) unsafe.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            d = fieldInterceptor.serialize(d);
        }
        if (d == null)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
            buf.writeDouble(d);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        Double value;
        if (exist)
        {
            value = buf.readDouble();
        }
        else
        {
            value = null;
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
        boolean exist = buf.get() == 1 ? true : false;
        Double value;
        if (exist)
        {
            value = BufferUtil.readDouble(buf);
        }
        else
        {
            value = null;
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        unsafe.putObject(holder, offset, value);
    }
    
}