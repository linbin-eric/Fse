package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.serializer.LicpSerializer;

import java.lang.reflect.Modifier;
import java.nio.charset.Charset;

public abstract class AbstractArraySerializer<T> implements LicpSerializer<T>
{
    protected static final Charset CHARSET = Charset.forName("utf8");
    protected final boolean        elementSameType;
    protected final Class<?>       elementType;
    
    public AbstractArraySerializer(Class<T> type)
    {
        elementType = type.getComponentType();
        if (Modifier.isFinal(elementType.getModifiers()))
        {
            elementSameType = true;
        }
        else
        {
            elementSameType = false;
        }
    }
    
}
