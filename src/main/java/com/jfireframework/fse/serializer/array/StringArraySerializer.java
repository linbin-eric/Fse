package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class StringArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        writeElement((String[]) o, byteArray, 0);
    }

    private void writeElement(String[] o, InternalByteArray byteArray, int add)
    {
        String[] array = o;
        byteArray.writePositive(array.length + add);
        for (String each : array)
        {
            byteArray.writeString(each);
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        return readElement(byteArray, len);
    }

    private Object readElement(InternalByteArray byteArray, int len)
    {
        String[] array = new String[len];
        for (int i = 0; i < len; i++)
        {
            array[i] = byteArray.readString();
        }
        return array;
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put((byte) 0);
            return;
        }
        writeElement((String[]) o, byteArray, 1);
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        if (len == 0)
        {
            return null;
        }
        return readElement(byteArray, len - 1);
    }
}
