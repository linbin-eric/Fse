//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jfireframework.licp.buf;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.baseutil.exception.UnSupportException;
import com.jfireframework.baseutil.reflect.ReflectUtil;
import sun.misc.Cleaner;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DirectByteBuf extends ByteBuf<ByteBuffer>
{
    private static Method cleaner;

    static
    {
        try
        {
            cleaner = ByteBuffer.allocateDirect(1).getClass().getDeclaredMethod("cleaner");
        } catch (NoSuchMethodException e)
        {
            ReflectUtil.throwException(e);
        }
    }

    public DirectByteBuf(ByteBuffer memory)
    {
        this.init(memory);
    }

    public void init(ByteBuffer memory)
    {
        this.memory = memory;
        this.maskRead = this.maskWrite = this.readIndex = this.writeIndex = 0;
        this.capacity = memory.capacity();
    }

    public void release()
    {
        try
        {
            ((Cleaner) cleaner.invoke(this.memory, (Object[]) null)).clean();
        } catch (Exception var2)
        {
            ReflectUtil.throwException(var2);
        }
    }

    protected DirectByteBuf changeToWriteState()
    {
        ((ByteBuffer) this.memory).limit(this.capacity).position(this.writeIndex);
        return this;
    }

    protected DirectByteBuf changeToReadState()
    {
        ((ByteBuffer) this.memory).limit(this.writeIndex).position(this.readIndex);
        return this;
    }

    protected void _put(ByteBuffer buffer, int length)
    {
        this.changeToWriteState();
        int limit = buffer.limit();
        buffer.limit(buffer.position() + length);
        ((ByteBuffer) this.memory).put(buffer);
        buffer.limit(limit);
    }

    protected void _put(int offset, byte b)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).put(offset, b);
    }

    protected void _put(byte[] content, int off, int len)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).put(content, off, len);
    }

    public byte get(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).get(index);
    }

    public byte get()
    {
        this.changeToReadState();
        byte result = ((ByteBuffer) this.memory).get();
        ++this.readIndex;
        return result;
    }

    public byte[] toArray()
    {
        this.changeToReadState();
        byte[] tmp = new byte[this.remainRead()];
        ((ByteBuffer) this.memory).get(tmp);
        this.readIndex = this.writeIndex;
        return tmp;
    }

    public DirectByteBuf compact()
    {
        if (this.readIndex != 0)
        {
            this.changeToReadState();
            ((ByteBuffer) this.memory).compact();
            this.writeIndex -= this.readIndex;
            this.readIndex = 0;
        }
        return this;
    }

    protected void _get(byte[] content, int off, int length)
    {
        this.changeToReadState();
        ((ByteBuffer) this.memory).get(content, off, length);
        this.readIndex += length;
    }

    public String toString(Charset charset, int length)
    {
        byte[] src = new byte[length];
        this.changeToReadState();
        ((ByteBuffer) this.memory).get(src);
        return new String(src, charset);
    }

    protected void _expend(int size)
    {
        this.cachedNioBuffer = null;
        int        newSize = (newSize = this.capacity << 1) > size ? newSize : size + newSize;
        ByteBuffer tmp     = ByteBuffer.allocateDirect(newSize);
        tmp.put(this.nioBuffer());
        this.capacity = newSize;
        this.release();
        this.memory = tmp;
    }

    public ByteBuffer nioBuffer()
    {
        this.cachedNioBuffer = (ByteBuffer) this.changeToReadState().memory;
        return this.cachedNioBuffer;
    }

    protected void _put(ByteBuf<?> byteBuf, int length)
    {
        this.changeToWriteState();
        if (byteBuf instanceof HeapByteBuf)
        {
            ((ByteBuffer) this.memory).put((byte[]) ((byte[]) byteBuf.memory), byteBuf.readIndex, length);
        }
        else
        {
            ByteBuffer buffer = byteBuf.nioBuffer();
            int        posi   = buffer.position();
            int        limit  = buffer.limit();
            buffer.limit(posi + length);
            ((ByteBuffer) this.memory).put(buffer);
            buffer.limit(limit);
            buffer.position(posi);
        }
    }

    public void _writeInt(int index, int i)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).putInt(index, i);
    }

    protected void _writeShort(int index, short s)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).putShort(index, s);
    }

    protected void _writeLong(int index, long l)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).putLong(index, l);
    }

    protected void _writeChar(int index, char c)
    {
        this.changeToWriteState();
        ((ByteBuffer) this.memory).putChar(index, c);
    }

    protected void _writeBoolean(int index, boolean b)
    {
        this.changeToWriteState();
        if (b)
        {
            ((ByteBuffer) this.memory).put(index, (byte) 1);
        }
        else
        {
            ((ByteBuffer) this.memory).put(index, (byte) 0);
        }
    }

    public int readInt()
    {
        this.changeToReadState();
        int result = ((ByteBuffer) this.memory).getInt();
        this.readIndex += 4;
        return result;
    }

    public short readShort()
    {
        this.changeToReadState();
        short s = ((ByteBuffer) this.memory).getShort();
        this.readIndex += 2;
        return s;
    }

    public long readLong()
    {
        this.changeToReadState();
        long l = ((ByteBuffer) this.memory).getLong();
        this.readIndex += 8;
        return l;
    }

    public char readChar()
    {
        this.changeToReadState();
        char c = ((ByteBuffer) this.memory).getChar();
        this.readIndex += 2;
        return c;
    }

    public float readFloat()
    {
        this.changeToReadState();
        float f = ((ByteBuffer) this.memory).getFloat();
        this.readIndex += 4;
        return f;
    }

    public double readDouble()
    {
        this.changeToReadState();
        double d = ((ByteBuffer) this.memory).getDouble();
        this.readIndex += 8;
        return d;
    }

    public boolean readBoolean()
    {
        this.changeToReadState();
        if (((ByteBuffer) this.memory).get() == 0)
        {
            ++this.readIndex;
            return false;
        }
        else
        {
            ++this.readIndex;
            return true;
        }
    }

    public int readInt(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getInt(index);
    }

    public short readShort(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getShort(index);
    }

    public long readLong(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getLong(index);
    }

    public char readChar(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getChar(index);
    }

    public float readFloat(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getFloat(index);
    }

    public double readDouble(int index)
    {
        this.changeToReadState();
        return ((ByteBuffer) this.memory).getDouble(index);
    }

    public String hexString()
    {
        this.changeToReadState();
        StringCache cache = new StringCache(this.remainRead());
        for (int i = this.readIndex; i < this.writeIndex; ++i)
        {
            cache.append(DIGITS_LOWER[(((ByteBuffer) this.memory).get(i) & 240) >>> 4]);
            cache.append(DIGITS_LOWER[((ByteBuffer) this.memory).get(i) & 15]);
        }
        return cache.toString();
    }

    public void writePositive(int positive)
    {
        if (positive < 0)
        {
            throw new UnsupportedOperationException();
        }
        else
        {
            this.changeToWriteState();
            if (positive <= 251)
            {
                this.ensureCapacity(this.writeIndex + 1);
                ((ByteBuffer) this.memory).put(this.writeIndex, (byte) positive);
                ++this.writeIndex;
            }
            else if (positive <= 255)
            {
                this.ensureCapacity(this.writeIndex + 2);
                ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -4);
                ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) positive);
                this.writeIndex += 2;
            }
            else if (positive <= 65535)
            {
                this.ensureCapacity(this.writeIndex + 3);
                ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -3);
                ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) (positive >> 8));
                ((ByteBuffer) this.memory).put(this.writeIndex + 2, (byte) positive);
                this.writeIndex += 3;
            }
            else if (positive <= 16777215)
            {
                this.ensureCapacity(this.writeIndex + 4);
                ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -2);
                ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) (positive >> 16));
                ((ByteBuffer) this.memory).put(this.writeIndex + 2, (byte) (positive >> 8));
                ((ByteBuffer) this.memory).put(this.writeIndex + 3, (byte) positive);
                this.writeIndex += 4;
            }
            else
            {
                this.ensureCapacity(this.writeIndex + 5);
                ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -1);
                ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) (positive >> 24));
                ((ByteBuffer) this.memory).put(this.writeIndex + 2, (byte) (positive >> 16));
                ((ByteBuffer) this.memory).put(this.writeIndex + 3, (byte) (positive >> 8));
                ((ByteBuffer) this.memory).put(this.writeIndex + 4, (byte) positive);
                this.writeIndex += 5;
            }
        }
    }

    public int readPositive()
    {
        this.changeToReadState();
        int length = ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
        if (length <= 251)
        {
            return length;
        }
        else if (length == 252)
        {
            length = ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return length;
        }
        else if (length == 253)
        {
            length = (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 8;
            length |= ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return length;
        }
        else if (length == 254)
        {
            length = (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 16;
            length |= (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 8;
            length |= ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return length;
        }
        else if (length == 255)
        {
            length = (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 24;
            length |= (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 16;
            length |= (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 8;
            length |= ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return length;
        }
        else
        {
            throw new RuntimeException("wrong data");
        }
    }

    public static DirectByteBuf allocate(int size)
    {
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        return new DirectByteBuf(buffer);
    }

    public DirectByteBuf writeVarint(int i)
    {
        if (i >= -120 && i <= 127)
        {
            this.ensureCapacity(this.writeIndex + 1);
            this.changeToWriteState();
            ((ByteBuffer) this.memory).put((byte) i);
            ++this.writeIndex;
            return this;
        }
        else
        {
            int head = -120;
            if (i < 0)
            {
                i = ~i;
                head = -124;
            }
            if (i <= 255)
            {
                this.ensureCapacity(this.writeIndex + 2);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 1)).put((byte) i);
                this.writeIndex += 2;
            }
            else if (i <= 65535)
            {
                this.ensureCapacity(this.writeIndex + 3);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 2)).put((byte) (i >>> 8)).put((byte) i);
                this.writeIndex += 3;
            }
            else if (i <= 16777215)
            {
                this.ensureCapacity(this.writeIndex + 4);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 3)).put((byte) (i >>> 16)).put((byte) (i >>> 8)).put((byte) i);
                this.writeIndex += 4;
            }
            else
            {
                this.ensureCapacity(this.writeIndex + 5);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 4)).put((byte) (i >>> 24)).put((byte) (i >>> 16)).put((byte) (i >>> 8)).put((byte) i);
                this.writeIndex += 5;
            }
            return this;
        }
    }

    public int readVarint()
    {
        this.changeToReadState();
        byte b = ((ByteBuffer) this.memory).get();
        if (b >= -120 && b <= 127)
        {
            ++this.readIndex;
            return b;
        }
        else
        {
            switch (b)
            {
                case -128:
                    this.readIndex += 5;
                    return ~((((ByteBuffer) this.memory).get() & 255) << 24 | (((ByteBuffer) this.memory).get() & 255) << 16 | (((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255);
                case -127:
                    this.readIndex += 4;
                    return ~((((ByteBuffer) this.memory).get() & 255) << 16 | (((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255);
                case -126:
                    this.readIndex += 3;
                    return ~((((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255);
                case -125:
                    this.readIndex += 2;
                    return ~(((ByteBuffer) this.memory).get() & 255);
                case -124:
                    this.readIndex += 5;
                    return (((ByteBuffer) this.memory).get() & 255) << 24 | (((ByteBuffer) this.memory).get() & 255) << 16 | (((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255;
                case -123:
                    this.readIndex += 4;
                    return (((ByteBuffer) this.memory).get() & 255) << 16 | (((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255;
                case -122:
                    this.readIndex += 3;
                    return (((ByteBuffer) this.memory).get() & 255) << 8 | ((ByteBuffer) this.memory).get() & 255;
                case -121:
                    this.readIndex += 2;
                    return ((ByteBuffer) this.memory).get() & 255;
                default:
                    throw new UnSupportException("not here");
            }
        }
    }

    public DirectByteBuf writeVarLong(long i)
    {
        if (i >= -112L && i <= 127L)
        {
            this.ensureCapacity(this.writeIndex + 1);
            this.changeToWriteState();
            ((ByteBuffer) this.memory).put((byte) ((int) i));
            ++this.writeIndex;
            return this;
        }
        else
        {
            int head = -112;
            if (i < 0L)
            {
                i = ~i;
                head = -120;
            }
            if (i <= 255L)
            {
                this.ensureCapacity(this.writeIndex + 2);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 1)).put((byte) ((int) i));
                this.writeIndex += 2;
            }
            else if (i <= 65535L)
            {
                this.ensureCapacity(this.writeIndex + 3);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 2)).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 3;
            }
            else if (i <= 16777215L)
            {
                this.ensureCapacity(this.writeIndex + 4);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 3)).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 4;
            }
            else if (i <= 4294967295L)
            {
                this.ensureCapacity(this.writeIndex + 5);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 4)).put((byte) ((int) (i >>> 24))).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 5;
            }
            else if (i <= 1099511627775L)
            {
                this.ensureCapacity(this.writeIndex + 6);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 5)).put((byte) ((int) (i >>> 32))).put((byte) ((int) (i >>> 24))).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 6;
            }
            else if (i <= 281474976710655L)
            {
                this.ensureCapacity(this.writeIndex + 7);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 6)).put((byte) ((int) (i >>> 40))).put((byte) ((int) (i >>> 32))).put((byte) ((int) (i >>> 24))).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 7;
            }
            else if (i <= 72057594037927935L)
            {
                this.ensureCapacity(this.writeIndex + 8);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 7)).put((byte) ((int) (i >>> 48))).put((byte) ((int) (i >>> 40))).put((byte) ((int) (i >>> 32))).put((byte) ((int) (i >>> 24))).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 8;
            }
            else
            {
                this.ensureCapacity(this.writeIndex + 9);
                this.changeToWriteState();
                ((ByteBuffer) this.memory).put((byte) (head - 8)).put((byte) ((int) (i >>> 56))).put((byte) ((int) (i >>> 48))).put((byte) ((int) (i >>> 40))).put((byte) ((int) (i >>> 32))).put((byte) ((int) (i >>> 24))).put((byte) ((int) (i >>> 16))).put((byte) ((int) (i >>> 8))).put((byte) ((int) i));
                this.writeIndex += 9;
            }
            return this;
        }
    }

    public long readVarLong()
    {
        this.changeToReadState();
        byte b = ((ByteBuffer) this.memory).get();
        if (b >= -112 && b <= 127)
        {
            ++this.readIndex;
            return (long) b;
        }
        else
        {
            switch (b)
            {
                case -128:
                    this.readIndex += 9;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 56 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 48 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -127:
                    this.readIndex += 8;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 48 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -126:
                    this.readIndex += 7;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -125:
                    this.readIndex += 6;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -124:
                    this.readIndex += 5;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -123:
                    this.readIndex += 4;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -122:
                    this.readIndex += 3;
                    return ~(((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L);
                case -121:
                    this.readIndex += 2;
                    return ~((long) ((ByteBuffer) this.memory).get() & 255L);
                case -120:
                    this.readIndex += 9;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 56 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 48 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -119:
                    this.readIndex += 8;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 48 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -118:
                    this.readIndex += 7;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 40 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -117:
                    this.readIndex += 6;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 32 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -116:
                    this.readIndex += 5;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 24 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -115:
                    this.readIndex += 4;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 16 | ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -114:
                    this.readIndex += 3;
                    return ((long) ((ByteBuffer) this.memory).get() & 255L) << 8 | (long) ((ByteBuffer) this.memory).get() & 255L;
                case -113:
                    this.readIndex += 2;
                    return (long) ((ByteBuffer) this.memory).get() & 255L;
                default:
                    throw new UnSupportException("not here");
            }
        }
    }

    public ByteBuf<ByteBuffer> writeVarChar(char c)
    {
        this.changeToWriteState();
        this.ensureCapacity(this.writeIndex + 3);
        this._writeVarChar(c);
        return this;
    }

    private ByteBuf<ByteBuffer> _writeVarChar(char c)
    {
        if (c <= 251)
        {
            ((ByteBuffer) this.memory).put(this.writeIndex, (byte) c);
            ++this.writeIndex;
        }
        else if (c <= 255)
        {
            ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -4);
            ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) c);
            this.writeIndex += 2;
        }
        else if (c <= '\uffff')
        {
            ((ByteBuffer) this.memory).put(this.writeIndex, (byte) -3);
            ((ByteBuffer) this.memory).put(this.writeIndex + 1, (byte) (c >>> 8));
            ((ByteBuffer) this.memory).put(this.writeIndex + 2, (byte) c);
            this.writeIndex += 3;
        }
        return this;
    }

    public char readVarChar()
    {
        this.changeToReadState();
        int length = ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
        if (length <= 251)
        {
            return (char) length;
        }
        else if (length == 252)
        {
            length = ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return (char) length;
        }
        else if (length == 253)
        {
            length = (((ByteBuffer) this.memory).get(this.readIndex++) & 255) << 8;
            length |= ((ByteBuffer) this.memory).get(this.readIndex++) & 255;
            return (char) length;
        }
        else
        {
            throw new UnSupportException("not here");
        }
    }

    public ByteBuf<ByteBuffer> writeString(String value)
    {
        if (value == null)
        {
            throw new NullPointerException();
        }
        else
        {
            int length = value.length();
            this.writePositive(length);
            this.changeToWriteState();
            this.ensureCapacity(this.writeIndex + length * 3);
            for (int i = 0; i < length; ++i)
            {
                this._writeVarChar(value.charAt(i));
            }
            return this;
        }
    }

    public String readString()
    {
        int length = this.readPositive();
        if (length == 0)
        {
            return "";
        }
        else
        {
            char[] src = new char[length];
            for (int i = 0; i < length; ++i)
            {
                src[i] = this.readVarChar();
            }
            return new String(src);
        }
    }
}
