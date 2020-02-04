package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class CharArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public CharArraySerializer(boolean primitive)
    {
        this.primitive = primitive;
    }

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        writeElement(o, byteArray);
    }

    private void writeElement(Object o, InternalByteArray byteArray)
    {
        if (primitive)
        {
            char[] array = (char[]) o;
            byteArray.writePositive(array.length );
            for (char i : array)
            {
                byteArray.writeVarChar(i);
            }
        }
        else
        {
            Character[] array = (Character[]) o;
            byteArray.writePositive(array.length );
            for (Character each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.writeVarChar(each);
                }
            }
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
        if (primitive)
        {
            char[] array = new char[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.readVarChar();
            }
            return array;
        }
        else
        {
            Character[] array = new Character[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Character.valueOf(byteArray.readVarChar());
                }
            }
            return array;
        }
    }
}
