package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class IntArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public IntArraySerializer(boolean primitive)
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
            int[] array = (int[]) o;
            byteArray.writePositive(array.length );
            for (int i : array)
            {
                byteArray.writeVarInt(i);
            }
        }
        else
        {
            Integer[] array = (Integer[]) o;
            byteArray.writePositive(array.length );
            for (Integer each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.writeVarInt(each);
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
            int[] array = new int[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.readVarInt();
            }
            return array;
        }
        else
        {
            Integer[] array = new Integer[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Integer.valueOf(byteArray.readVarInt());
                }
            }
            return array;
        }
    }

}
