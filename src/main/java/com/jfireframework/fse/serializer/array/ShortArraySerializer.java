package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class ShortArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public ShortArraySerializer(boolean primitive)
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
            short[] array = (short[]) o;
            byteArray.writePositive(array.length + add);
            for (short v : array)
            {
                byteArray.writeShort(v);
            }
        }
        else
        {
            Short[] array = (Short[]) o;
            byteArray.writePositive(array.length + add);
            for (Short v : array)
            {
                if (v == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.writeShort(v);
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
            short[] array = new short[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.readShort();
            }
            return array;
        }
        else
        {
            Short[] array = new Short[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Short.valueOf(byteArray.readShort());
                }
            }
            return array;
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
