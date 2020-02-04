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

}
