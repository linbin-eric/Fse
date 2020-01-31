package com.jfireframework.fse.serializer.base;

import com.jfireframework.fse.*;

public class StringSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        byteArray.writeString((String) o);
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        return byteArray.readString();
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        String value = (String) o;
        if (o == null)
        {
            byteArray.put((byte) 0);
            return;
        }
        int length = value.length();
        byteArray.writePositive(length + 1);
        for (int i = 0; i < length; ++i)
        {
            byteArray.writeVarChar(value.charAt(i));
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
        return new String(str);
    }
}
