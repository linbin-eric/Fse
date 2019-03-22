package com.jfireframework.licp.serializer.extra;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.Calendar;

public class CalendarSerializer implements LicpSerializer<Calendar>
{
    
    @Override
    public void serialize(Calendar src, ByteBuf<?> buf, InternalLicp licp)
    {
        long time = src.getTimeInMillis();
        buf.writeVarLong(time);
    }
    
    @Override
    public Calendar deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        long time = buf.readVarLong();
        Calendar calendar = Calendar.getInstance();
        licp.putObject(calendar);
        calendar.setTimeInMillis(time);
        return calendar;
    }
    
    @Override
    public Calendar deserialize(ByteBuffer buf, InternalLicp licp)
    {
        long time = BufferUtil.readVarLong(buf);
        Calendar calendar = Calendar.getInstance();
        licp.putObject(calendar);
        calendar.setTimeInMillis(time);
        return calendar;
    }
    
}
