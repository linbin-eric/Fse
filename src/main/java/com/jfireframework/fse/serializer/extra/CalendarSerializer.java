package com.jfireframework.fse.serializer.extra;

import com.jfireframework.fse.*;

import java.util.Calendar;

public class CalendarSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        byteArray.writeVarLong(((Calendar) o).getTimeInMillis());
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.put((byte) 0);
            return;
        }
        byteArray.writeVarLong(((Calendar) o).getTimeInMillis() + 1);
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        long     l        = byteArray.readVarLong();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(l);
        return instance;
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        long time = byteArray.readVarLong();
        if (time == 0)
        {
            return null;
        }
        time -= 1;
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance;
    }
}
