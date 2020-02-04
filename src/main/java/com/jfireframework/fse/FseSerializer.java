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
     * @param o          必然不为null
     * @param classIndex 可能的值为1或者大于1的正数。为1的时候意味着没有注册类到上下文中。大于1时，意味着是类在上下文的序号
     * @param byteArray
     * @param fseContext
     * @param depth      -1 的时候意味着不支持循环依赖
     */
    void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth);

    /**
     * 使用该接口试行逆序列化，该接口仅序列化序号2的部分。
     *
     * @param byteArray
     * @param fseContext
     * @return
     */
    Object readBytes(InternalByteArray byteArray, FseContext fseContext);
}
