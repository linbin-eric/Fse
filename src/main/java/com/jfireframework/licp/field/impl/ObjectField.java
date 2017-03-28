package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.serializer.LicpSerializer;

public class ObjectField extends AbstractCacheField
{
    private LicpSerializer<?> serializer;
    
    public ObjectField(Field field, InternalLicp licp, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
        if (finalField)
        {
            serializer = licp._getSerializer(field.getType());
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Object value = unsafe.getObject(holder, offset);
        if (finalField)
        {
            licp._serialize(value, buf, (LicpSerializer<Object>) serializer);
        }
        else
        {
            licp._serialize(value, buf);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Object value;
        if (finalField)
        {
            value = licp._deserialize(buf, serializer);
        }
        else
        {
            value = licp._deserialize(buf);
        }
        unsafe.putObject(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        Object value;
        if (finalField)
        {
            value = licp._deserialize(buf, serializer);
        }
        else
        {
            value = licp._deserialize(buf);
        }
        unsafe.putObject(holder, offset, value);
    }
    
}
