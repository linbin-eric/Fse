package com.jfireframework.licp.serializer.extra;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;
import java.sql.Date;

public class SqlDateSerializer implements LicpSerializer<Date>
{
    @Override
    public void serialize(Date src, ByteBuf<?> buf, InternalLicp licp)
    {
        Date date = src;
        buf.writeVarLong(date.getTime());
    }
    
    @Override
    public Date deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        long time = buf.readVarLong();
        Date result = new java.sql.Date(time);
        licp.putObject(result);
        return result;
    }
    
    @Override
    public Date deserialize(ByteBuffer buf, InternalLicp licp)
    {
        long time = BufferUtil.readVarLong(buf);
        Date result = new java.sql.Date(time);
        licp.putObject(result);
        return result;
    }
}
