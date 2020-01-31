package com.jfireframework.fse.serializer.extra;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.fse.*;

import java.util.HashMap;
import java.util.Map;

public class ClassSerializer extends CycleFlagSerializer implements FseSerializer
{
    private Map<String, Class> nameToClass = new HashMap<String, Class>();

    public ClassSerializer()
    {
        nameToClass.put("void", void.class);
    }

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put(Fse.NULL);
            return;
        }
        byteArray.writeVarInt(classIndex);
        byteArray.writeString(((Class) o).getName());
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put(Fse.NULL);
            return;
        }
        String name   = ((Class) o).getName();
        int    length = name.length();
        byteArray.writePositive(length + 1);
        for (int i = 0; i < length; ++i)
        {
            byteArray.writeVarChar(name.charAt(i));
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        String className = byteArray.readString();
        Class  ckass     = nameToClass.get(className);
        if (ckass != null)
        {
            return ckass;
        }
        try
        {
            ckass = Class.forName(className);
            nameToClass.put(className, ckass);
            return ckass;
        }
        catch (ClassNotFoundException e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        if (len == 0)
        {
            return null;
        }
        len = len - 1;
        char[] str = new char[len];
        for (int i = 0; i < len; i++)
        {
            str[i] = byteArray.readVarChar();
        }
        String name  = new String(str);
        Class  ckass = nameToClass.get(name);
        if (ckass != null)
        {
            return ckass;
        }
        try
        {
            ckass = Class.forName(name);
            nameToClass.put(name, ckass);
            return ckass;
        }
        catch (ClassNotFoundException e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }
}
