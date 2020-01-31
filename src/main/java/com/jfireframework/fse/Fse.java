package com.jfireframework.fse;

public class Fse
{
    public static final byte       WITH_CYCLE    = (byte) 0Xc1;
    public static final byte       WITHOUT_CYCLE = (byte) 0xc2;
    public static final int        SUPPORT_CYCLE = -1;
    public static final byte       NULL          = 0;
    public static final int        maxDepth;

    static
    {
        maxDepth = 64;
    }

    private             FseContext fseContext    = new FseContext();

    public void serialize(Object o, ByteArray byteArray)
    {
        if (o == null || byteArray == null || byteArray instanceof InternalByteArray == false)
        {
            throw new IllegalArgumentException();
        }
        byteArray.put(WITH_CYCLE);
        ((InternalByteArray) byteArray).skipWrite(4);
        fseContext.startSerilaize(o, (InternalByteArray) byteArray);
        int mark = byteArray.getWritePosi();
        byteArray.setWritePosi(1);
        ((InternalByteArray) byteArray).writeInt(mark);
        byteArray.setWritePosi(mark);
        fseContext.serialzeClass((InternalByteArray) byteArray);
        fseContext.clear();
    }

    public Object deSerialize(ByteArray byteArray)
    {
        if (byteArray == null || byteArray instanceof InternalByteArray == false)
        {
            throw new IllegalArgumentException();
        }
        byte flag = byteArray.get();
        if (flag != WITH_CYCLE && flag != WITHOUT_CYCLE)
        {
            throw new IllegalArgumentException();
        }
        fseContext.setWithCycle(flag == WITH_CYCLE);
        InternalByteArray internalByteArray = (InternalByteArray) byteArray;
        int               mark              = internalByteArray.readInt();
        internalByteArray.setReadPosi(mark);
        fseContext.deSerializeClass(internalByteArray);
        internalByteArray.setReadPosi(5);
        int           classIndex = internalByteArray.readVarInt();
        FseSerializer serializer = fseContext.getClassRegistry(classIndex).getSerializer();
        Object        result     = serializer.readBytes(internalByteArray, fseContext);
        fseContext.clear();
        return result;
    }

    public void register(Class ckass)
    {
        fseContext.registerClass(ckass);
    }

    /**
     * 启用编译模式，对于序列化对象，要求提供无参构造方法，属性均提供get和set方法
     */
    public void useCompile()
    {
        fseContext.useCompile();
    }

    public void registerFseSerializer(Class ckass, FseSerializer fseSerializer)
    {
        fseContext.registerFseSerializer(ckass, fseSerializer);
    }
}
