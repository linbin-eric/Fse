package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class ByteArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public ByteArraySerializer(boolean primitive)
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
            byte[] array = (byte[]) o;
            byteArray.writePositive(array.length);
            for (byte i : array)
            {
                byteArray.put(i);
            }
        }
        else
        {
            Byte[] array = (Byte[]) o;
            byteArray.writePositive(array.length);
            for (Byte each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.put(each);
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
            byte[] array = new byte[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.get();
            }
            return array;
        }
        else
        {
            Byte[] array = new Byte[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Byte.valueOf(byteArray.get());
                }
            }
            return array;
        }
    }
}
