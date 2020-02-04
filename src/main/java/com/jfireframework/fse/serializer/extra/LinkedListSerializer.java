package com.jfireframework.fse.serializer.extra;

import com.jfireframework.fse.*;

import java.util.LinkedList;

public class LinkedListSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writePositive(((LinkedList) o).size());
        for (Object each : ((LinkedList) o))
        {
            if (each == null)
            {
                byteArray.writeVarInt(-1);
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
        LinkedList list = new LinkedList();
        fseContext.collectObject(list);
        int size = byteArray.readPositive();
        for (int i = 0; i < size; i++)
        {
            int result = byteArray.readVarInt();
            if (result == 0)
            {
                list.add(null);
            }
            else if (result > 0)
            {
                Object element = fseContext.getClassRegistry(result).getSerializer().readBytes(byteArray, fseContext);
                list.add(element);
            }
            else
            {
                list.add(fseContext.getObjectByIndex(0 - result));
            }
        }
        return list;
    }

}
