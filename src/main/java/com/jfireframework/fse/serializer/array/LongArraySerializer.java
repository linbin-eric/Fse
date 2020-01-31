package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class LongArraySerializer extends CycleFlagSerializer implements FseSerializer
{

    private final boolean primitive;

    public LongArraySerializer(boolean primitive)
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
        writeElement(o, byteArray, 0);
    }

    private void writeElement(Object o, InternalByteArray byteArray, int add)
    {
        if (primitive)
        {
            long[] array = (long[]) o;
            byteArray.writePositive(array.length + add);
            for (long i : array)
            {
                byteArray.writeVarLong(i);
            }
        }
        else
        {
            Long[] array = (Long[]) o;
            byteArray.writePositive(array.length + add);
            for (Long each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.writeVarLong(each);
                }
            }
        }
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put((byte) 0);
            return;
        }
        writeElement(o, byteArray, 1);
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
            long[] array = new long[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.readVarLong();
            }
            return array;
        }
        else
        {
            Long[] array = new Long[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Long.valueOf(byteArray.readVarLong());
                }
            }
            return array;
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
        return readElement(byteArray, len - 1);
    }
}
