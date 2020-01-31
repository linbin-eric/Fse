package com.jfireframework.fse;

public class Helper
{

    public static Object deSerialize(InternalByteArray byteArray, FseContext fseContext)
    {
        int flag = byteArray.readVarInt();
        if (flag == 0)
        {
            return null;
        }
        else if (flag > 0)
        {
            return fseContext.getClassRegistry(flag).getSerializer().readBytes(byteArray, fseContext);
        }
        else
        {
            return fseContext.getObjectByIndex(0 - flag);
        }
    }
}
