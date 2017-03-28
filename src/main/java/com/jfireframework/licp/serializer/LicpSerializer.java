package com.jfireframework.licp.serializer;

import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;

public interface LicpSerializer<T>
{
    
    /**
     * 将对象src序列化到cache中。
     * 这里存在一个约束，当走到这一步开始序列话的时候，src不是null
     * 
     * @param src
     * @param cache
     * @param collect
     * @param register TODO
     */
    public void serialize(T src, ByteBuf<?> buf, InternalLicp licp);
    
    /**
     * 反序列化二进制字节到对象中去
     * 这里存在一个约束，如果使用这个接口开始反序列化，意味着类型已知，并且不是null
     * 
     * @param target
     * @param cache
     * @param collect
     * @param register TODO
     */
    public T deserialize(ByteBuf<?> buf, InternalLicp licp);
    
    public T deserialize(ByteBuffer buf, InternalLicp licp);
    
}
