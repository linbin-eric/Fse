package com.jfireframework.licp.interceptor;

public interface LicpFieldInterceptor
{
    /**
     * 需要拦截的字段。规则是类的全限定名+.+属性名
     * 
     * @return
     */
    public String rule();
    
    /**
     * 序列化一个属性值前进行拦截处理。这个属性值不是基本类
     * 
     * @param <V>
     * 
     * @param v
     * @return
     */
    public <V> V serialize(V v);
    
    public boolean serializeBoolean(boolean value);
    
    public byte serializeByte(byte value);
    
    public short serializeShort(short value);
    
    public int serializeInt(int value);
    
    public long serializeLong(long value);
    
    public float serializeFloat(float value);
    
    public double serializeDouble(double value);
    
    public char serializeChar(char value);
    
    /**
     * 序列化一个属性值前进行拦截处理。这个属性值不是基本类
     * 
     * @param <V>
     * 
     * @param v
     * @return
     */
    public <V> V deserialize(V v);
    
    public boolean deserializeBoolean(boolean value);
    
    public byte deserializeByte(byte value);
    
    public short deserializeShort(short value);
    
    public int deserializeInt(int value);
    
    public long deserializeLong(long value);
    
    public float deserializeFloat(float value);
    
    public double deserializeDouble(double value);
    
    public char deserializeChar(char value);
    
}
