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
        writeElement((String[]) o, byteArray);
    }

    private void writeElement(String[] o, InternalByteArray byteArray)
    {
        String[] array = o;
        byteArray.writePositive(array.length );
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

}
