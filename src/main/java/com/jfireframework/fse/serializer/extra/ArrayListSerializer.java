package com.jfireframework.fse.serializer.extra;

import com.jfireframework.fse.*;

import java.util.ArrayList;

public class ArrayListSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writePositive(((ArrayList) o).size());
        for (Object each : ((ArrayList) o))
        {
            fseContext.serialize(each, byteArray, depth);
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int size = byteArray.readPositive();
        return readArrayList(byteArray, fseContext, size);
    }

    private Object readArrayList(InternalByteArray byteArray, FseContext fseContext, int size)
    {
        ArrayList arrayList = new ArrayList();
        fseContext.collectObject(arrayList);
        for (int i = 0; i < size; i++)
        {
            int result = byteArray.readVarInt();
            if (result == 0)
            {
                arrayList.add(null);
            }
            else if (result > 0)
            {
                Object element = fseContext.getClassRegistry(result).getSerializer().readBytes(byteArray, fseContext);
                arrayList.add(element);
            }
            else
            {
                arrayList.add(fseContext.getObjectByIndex(0 - result));
            }
        }
        return arrayList;
    }

}
