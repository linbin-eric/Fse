package com.jfireframework.fse;

public interface FseSerializer
{
    void init(Class<?> type, SerializerFactory serializerFactory);

    boolean needSupportCycle();

    void supportCycle();

    /**
     * 将对象序列化到存储中.
     * 如果不需要将对象加入到对象集合中，则也需要将classIndex写入
     *
     * @param o
     * @param classIndex
     * @param byteArray
     * @param fseContext
     * @param depth      -1 的时候意味着不支持循环依赖
     */
    void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth);

    void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth);

    /**
     * 使用该接口试行逆序列化，该接口仅序列化序号2的部分。
     *
     * @param byteArray
     * @param fseContext
     * @return
     */
    Object readBytes(InternalByteArray byteArray, FseContext fseContext);

    Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext);
}
