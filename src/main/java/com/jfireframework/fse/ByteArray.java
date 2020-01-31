package com.jfireframework.fse;

public class ByteArray
{
    protected byte[]  array;
    protected int     writePosi = 0;
    protected int     readIndex = 0;
    protected boolean needCheck = true;

    protected ByteArray(int size)
    {
        array = new byte[size];
    }

    protected ByteArray(byte[] array)
    {
        this.array = array;
    }

    public static ByteArray allocate(int size)
    {
        return new InternalByteArray(size);
    }

    public static ByteArray allocate()
    {
        return new InternalByteArray(1024);
    }

    public static ByteArray wrap(byte[] array)
    {
        return new InternalByteArray(array);
    }

    public void setNeedCheck(boolean needCheck)
    {
        this.needCheck = needCheck;
    }

    protected void ensureCapacity(int len)
    {
        if (needCheck && len > array.length - writePosi)
        {
            int    newLen = len + writePosi > (array.length << 1) ? len + writePosi + array.length : (array.length << 1);
            byte[] tmp    = new byte[newLen];
            System.arraycopy(array, 0, tmp, 0, array.length);
            array = tmp;
        }
    }

    public void clear()
    {
        writePosi = readIndex = 0;
    }

    public void put(byte value)
    {
        ensureCapacity(1);
        array[writePosi] = value;
        writePosi += 1;
    }

    public void setByte(int off, byte b)
    {
        array[off] = b;
    }

    public void put(byte[] data)
    {
        ensureCapacity(data.length);
        System.arraycopy(data, 0, array, writePosi, data.length);
        writePosi += data.length;
    }

    public byte get()
    {
        byte result = array[readIndex];
        readIndex += 1;
        return result;
    }

    public byte[] toArray()
    {
        byte[] result = new byte[writePosi];
        System.arraycopy(array, 0, result, 0, writePosi);
        return result;
    }

    public int getWritePosi()
    {
        return writePosi;
    }

    public void setWritePosi(int writePosi)
    {
        this.writePosi = writePosi;
    }

    public void setReadPosi(int readPosi)
    {
        this.readIndex = readPosi;
    }
}
