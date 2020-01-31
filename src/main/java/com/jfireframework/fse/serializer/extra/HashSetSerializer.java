package com.jfireframework.fse.serializer.extra;

import com.jfireframework.fse.*;

import java.util.HashSet;

public class HashSetSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        HashSet set = (HashSet) o;
        byteArray.writePositive(set.size());
        for (Object each : set)
        {
            if (each == null)
            {
                byteArray.writeVarInt(0);
            }
            else
            {
                fseContext.serialize(each, byteArray, depth);
            }
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int     size = byteArray.readPositive();
        HashSet set  = new HashSet();
        fseContext.collectObject(set);
        for (int i = 0; i < size; i++)
        {
            int result = byteArray.readVarInt();
            if (result == 0)
            {
                set.add(null);
            }
            else if (result > 0)
            {
                Object element = fseContext.getClassRegistry(result).getSerializer().readBytes(byteArray, fseContext);
                set.add(element);
            }
            else
            {
                set.add(fseContext.getObjectByIndex(0 - result));
            }
        }
        return set;
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        throw new UnsupportedOperationException();
    }
}
