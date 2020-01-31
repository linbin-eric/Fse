package com.jfireframework.fse.serializer.base;

import com.jfireframework.fse.*;

public class BooleanSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        if (((Boolean) o))
        {
            byteArray.put((byte) 1);
        }
        else
        {
            byteArray.put((byte) 2);
        }
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put((byte) 0);
        }
        else if (o == Boolean.TRUE)
        {
            byteArray.put((byte) 1);
        }
        else
        {
            byteArray.put((byte) 2);
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        byte flag = byteArray.get();
        if (flag == 0)
        {
            return null;
        }
        else if (flag == 1)
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        byte flag = byteArray.get();
        if (flag == 0)
        {
            return null;
        }
        else if (flag == 1)
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }
}
