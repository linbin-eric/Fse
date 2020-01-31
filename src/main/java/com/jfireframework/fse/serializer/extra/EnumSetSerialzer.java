package com.jfireframework.fse.serializer.extra;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.fse.*;

import java.lang.reflect.Field;
import java.util.EnumSet;

public class EnumSetSerialzer extends CycleFlagSerializer implements FseSerializer
{
    Field elementType;

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
        try
        {
            elementType = EnumSet.class.getDeclaredField("elementType");
            elementType.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        Class type = null;
        try
        {
            type = (Class) elementType.get(o);
            byteArray.writeString(type.getName());
        }
        catch (IllegalAccessException e)
        {
            ReflectUtil.throwException(e);
        }
        EnumSet enumSet = (EnumSet) o;
        int     size    = enumSet.size();
        byteArray.writePositive(size);
        for (Object each : enumSet)
        {
            byteArray.writeString(((Enum) each).name());
        }
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        try
        {
            String  s        = byteArray.readString();
            Class   enumType = Class.forName(s);
            EnumSet enums    = EnumSet.noneOf(enumType);
            int     size     = byteArray.readPositive();
            for (int i = 0; i < size; i++)
            {
                String enumName = byteArray.readString();
                enums.add(Enum.valueOf(enumType, enumName));
            }
            return enums;
        }
        catch (Throwable e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        throw new UnsupportedOperationException();
    }
}
