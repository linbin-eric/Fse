//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jfireframework.licp.buf;

import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.baseutil.exception.UnSupportException;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class HeapByteBuf extends ByteBuf<byte[]> {
    public HeapByteBuf(byte[] array) {
        this.init(array);
    }

    public void init(byte[] array) {
        this.memory = array;
        this.maskRead = this.maskWrite = this.readIndex = this.writeIndex = 0;
    }

    public void release() {
    }

    protected void _expend(int size) {
        this.cachedNioBuffer = null;
        int newSize = (newSize = this.capacity << 1) > size ? newSize : size + newSize;
        byte[] des = new byte[newSize];
        System.arraycopy(this.memory, 0, des, 0, this.capacity);
        this.capacity = newSize;
        this.memory = des;
    }

    protected void _put(ByteBuffer buffer, int length) {
        buffer.get((byte[])this.memory, this.writeIndex, length);
    }

    protected void _put(int offset, byte b) {
        ((byte[])this.memory)[offset] = b;
    }

    protected void _put(byte[] content, int off, int len) {
        System.arraycopy(content, off, this.memory, this.writeIndex, len);
    }

    public byte get(int index) {
        return ((byte[])this.memory)[index];
    }

    public byte get() {
        return ((byte[])this.memory)[this.readIndex++];
    }

    public byte[] toArray() {
        byte[] tmp = new byte[this.remainRead()];
        System.arraycopy(this.memory, this.readIndex, tmp, 0, tmp.length);
        return tmp;
    }

    public HeapByteBuf compact() {
        if (this.readIndex != 0) {
            System.arraycopy(this.memory, this.readIndex, this.memory, 0, this.remainRead());
            this.writeIndex -= this.readIndex;
            this.readIndex = 0;
        }

        return this;
    }

    protected void _get(byte[] content, int off, int length) {
        System.arraycopy(this.memory, this.readIndex, content, off, length);
        this.readIndex += length;
    }

    public String toString(Charset charset, int length) {
        return new String((byte[])this.memory, this.readIndex, this.readIndex + length, charset);
    }

    public ByteBuffer nioBuffer() {
        this.cachedNioBuffer = ByteBuffer.wrap((byte[])this.memory, this.readIndex, this.remainRead());
        return this.cachedNioBuffer;
    }

    protected void _put(ByteBuf<?> byteBuf, int length) {
        if (byteBuf instanceof HeapByteBuf) {
            System.arraycopy(byteBuf.memory, byteBuf.readIndex, this.memory, this.writeIndex, length);
        } else {
            ByteBuffer buffer = byteBuf.nioBuffer();
            int posi = buffer.position();
            buffer.get((byte[])this.memory, this.writeIndex, length);
            buffer.position(posi);
        }

    }

    public void _writeInt(int index, int i) {
        ((byte[])this.memory)[index] = (byte)(i >> 24);
        ((byte[])this.memory)[index + 1] = (byte)(i >> 16);
        ((byte[])this.memory)[index + 2] = (byte)(i >> 8);
        ((byte[])this.memory)[index + 3] = (byte)i;
    }

    protected void _writeShort(int index, short s) {
        ((byte[])this.memory)[index] = (byte)(s >> 8);
        ((byte[])this.memory)[index + 1] = (byte)s;
    }

    protected void _writeLong(int index, long l) {
        ((byte[])this.memory)[index] = (byte)((int)(l >> 56));
        ((byte[])this.memory)[index + 1] = (byte)((int)(l >> 48));
        ((byte[])this.memory)[index + 2] = (byte)((int)(l >> 40));
        ((byte[])this.memory)[index + 3] = (byte)((int)(l >> 32));
        ((byte[])this.memory)[index + 4] = (byte)((int)(l >> 24));
        ((byte[])this.memory)[index + 5] = (byte)((int)(l >> 16));
        ((byte[])this.memory)[index + 6] = (byte)((int)(l >> 8));
        ((byte[])this.memory)[index + 7] = (byte)((int)l);
    }

    protected void _writeChar(int index, char c) {
        ((byte[])this.memory)[index] = (byte)(c >> 8);
        ((byte[])this.memory)[index + 1] = (byte)c;
    }

    protected void _writeBoolean(int index, boolean b) {
        if (b) {
            ((byte[])this.memory)[index] = 1;
        } else {
            ((byte[])this.memory)[index] = 0;
        }

    }

    public int readInt() {
        int i = (((byte[])this.memory)[this.readIndex] & 255) << 24;
        i |= (((byte[])this.memory)[this.readIndex + 1] & 255) << 16;
        i |= (((byte[])this.memory)[this.readIndex + 2] & 255) << 8;
        i |= ((byte[])this.memory)[this.readIndex + 3] & 255;
        this.readIndex += 4;
        return i;
    }

    public short readShort() {
        short s = (short)((((byte[])this.memory)[this.readIndex] & 255) << 8);
        s = (short)(s | ((byte[])this.memory)[this.readIndex + 1] & 255);
        this.readIndex += 2;
        return s;
    }

    public long readLong() {
        long l = (long)((byte[])this.memory)[this.readIndex] << 56 | ((long)((byte[])this.memory)[this.readIndex + 1] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex + 2] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex + 3] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex + 4] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex + 5] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex + 6] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex + 7] & 255L;
        this.readIndex += 8;
        return l;
    }

    public char readChar() {
        char c = (char)(((byte[])this.memory)[this.readIndex] << 8);
        c = (char)(c | ((byte[])this.memory)[this.readIndex + 1] & 255);
        this.readIndex += 2;
        return c;
    }

    public float readFloat() {
        int i = this.readInt();
        return Float.intBitsToFloat(i);
    }

    public double readDouble() {
        long l = this.readLong();
        return Double.longBitsToDouble(l);
    }

    public boolean readBoolean() {
        if (((byte[])this.memory)[this.readIndex] == 0) {
            ++this.readIndex;
            return false;
        } else {
            ++this.readIndex;
            return true;
        }
    }

    public int readInt(int index) {
        int i = (((byte[])this.memory)[index] & 255) << 24;
        i |= (((byte[])this.memory)[index + 1] & 255) << 16;
        i |= (((byte[])this.memory)[index + 2] & 255) << 8;
        i |= ((byte[])this.memory)[index + 3];
        return i;
    }

    public short readShort(int index) {
        short s = (short)((((byte[])this.memory)[index] & 255) << 8);
        s = (short)(s | ((byte[])this.memory)[index + 1]);
        return s;
    }

    public long readLong(int index) {
        long l = (long)((byte[])this.memory)[this.readIndex] << 56 | ((long)((byte[])this.memory)[this.readIndex + 1] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex + 2] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex + 3] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex + 4] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex + 5] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex + 6] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex + 7] & 255L;
        return l;
    }

    public char readChar(int index) {
        char c = (char)(((byte[])this.memory)[index] << 8);
        c = (char)(c | ((byte[])this.memory)[index + 1]);
        return c;
    }

    public float readFloat(int index) {
        int i = this.readInt(index);
        return Float.intBitsToFloat(i);
    }

    public double readDouble(int index) {
        long l = this.readLong(index);
        return Double.longBitsToDouble(l);
    }

    public String hexString() {
        StringCache cache = new StringCache(this.remainRead());

        for(int i = this.readIndex; i < this.writeIndex; ++i) {
            cache.append(DIGITS_LOWER[(((byte[])this.memory)[i] & 240) >>> 4]);
            cache.append(DIGITS_LOWER[((byte[])this.memory)[i] & 15]);
        }

        return cache.toString();
    }

    public void writePositive(int positive) {
        if (positive < 0) {
            throw new UnsupportedOperationException();
        } else {
            if (positive <= 251) {
                this.ensureCapacity(this.writeIndex + 1);
                ((byte[])this.memory)[this.writeIndex] = (byte)positive;
                ++this.writeIndex;
            } else if (positive <= 255) {
                this.ensureCapacity(this.writeIndex + 2);
                ((byte[])this.memory)[this.writeIndex] = -4;
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)positive;
                this.writeIndex += 2;
            } else if (positive <= 65535) {
                this.ensureCapacity(this.writeIndex + 3);
                ((byte[])this.memory)[this.writeIndex] = -3;
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(positive >>> 8);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)positive;
                this.writeIndex += 3;
            } else if (positive <= 16777215) {
                this.ensureCapacity(this.writeIndex + 4);
                ((byte[])this.memory)[this.writeIndex] = -2;
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(positive >>> 16);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)(positive >>> 8);
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)positive;
                this.writeIndex += 4;
            } else {
                this.ensureCapacity(this.writeIndex + 5);
                ((byte[])this.memory)[this.writeIndex] = -1;
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(positive >>> 24);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)(positive >>> 16);
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)(positive >>> 8);
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)positive;
                this.writeIndex += 5;
            }

        }
    }

    public int readPositive() {
        int length = ((byte[])this.memory)[this.readIndex++] & 255;
        if (length <= 251) {
            return length;
        } else if (length == 252) {
            length = ((byte[])this.memory)[this.readIndex++] & 255;
            return length;
        } else if (length == 253) {
            length = (((byte[])this.memory)[this.readIndex++] & 255) << 8;
            length |= ((byte[])this.memory)[this.readIndex++] & 255;
            return length;
        } else if (length == 254) {
            length = (((byte[])this.memory)[this.readIndex++] & 255) << 16;
            length |= (((byte[])this.memory)[this.readIndex++] & 255) << 8;
            length |= ((byte[])this.memory)[this.readIndex++] & 255;
            return length;
        } else if (length == 255) {
            length = (((byte[])this.memory)[this.readIndex++] & 255) << 24;
            length |= (((byte[])this.memory)[this.readIndex++] & 255) << 16;
            length |= (((byte[])this.memory)[this.readIndex++] & 255) << 8;
            length |= ((byte[])this.memory)[this.readIndex++] & 255;
            return length;
        } else {
            throw new RuntimeException("wrong data");
        }
    }

    public static HeapByteBuf allocate(int size) {
        return new HeapByteBuf(new byte[size]);
    }

    public HeapByteBuf writeVarint(int i) {
        if (i >= -120 && i <= 127) {
            this.ensureCapacity(this.writeIndex + 1);
            ((byte[])this.memory)[this.writeIndex] = (byte)i;
            ++this.writeIndex;
            return this;
        } else {
            int head = -120;
            if (i < 0) {
                i = ~i;
                head = -124;
            }

            if (i <= 255) {
                this.ensureCapacity(this.writeIndex + 2);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 1);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)i;
                this.writeIndex += 2;
            } else if (i <= 65535) {
                this.ensureCapacity(this.writeIndex + 3);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 2);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(i >>> 8);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)i;
                this.writeIndex += 3;
            } else if (i <= 16777215) {
                this.ensureCapacity(this.writeIndex + 4);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 3);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(i >>> 16);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)(i >>> 8);
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)i;
                this.writeIndex += 4;
            } else {
                this.ensureCapacity(this.writeIndex + 5);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 4);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)(i >>> 24);
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)(i >>> 16);
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)(i >>> 8);
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)i;
                this.writeIndex += 5;
            }

            return this;
        }
    }

    public int readVarint() {
        byte b = ((byte[])this.memory)[this.readIndex++];
        if (b >= -120 && b <= 127) {
            return b;
        } else {
            switch(b) {
                case -128:
                    return ~((((byte[])this.memory)[this.readIndex++] & 255) << 24 | (((byte[])this.memory)[this.readIndex++] & 255) << 16 | (((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255);
                case -127:
                    return ~((((byte[])this.memory)[this.readIndex++] & 255) << 16 | (((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255);
                case -126:
                    return ~((((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255);
                case -125:
                    return ~(((byte[])this.memory)[this.readIndex++] & 255);
                case -124:
                    return (((byte[])this.memory)[this.readIndex++] & 255) << 24 | (((byte[])this.memory)[this.readIndex++] & 255) << 16 | (((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255;
                case -123:
                    return (((byte[])this.memory)[this.readIndex++] & 255) << 16 | (((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255;
                case -122:
                    return (((byte[])this.memory)[this.readIndex++] & 255) << 8 | ((byte[])this.memory)[this.readIndex++] & 255;
                case -121:
                    return ((byte[])this.memory)[this.readIndex++] & 255;
                default:
                    throw new UnSupportException("not here");
            }
        }
    }

    public HeapByteBuf writeVarLong(long i) {
        if (i >= -112L && i <= 127L) {
            this.ensureCapacity(this.writeIndex + 1);
            ((byte[])this.memory)[this.writeIndex] = (byte)((int)i);
            ++this.writeIndex;
            return this;
        } else {
            int head = -112;
            if (i < 0L) {
                i = ~i;
                head = -120;
            }

            if (i <= 255L) {
                this.ensureCapacity(this.writeIndex + 2);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 1);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)i);
                this.writeIndex += 2;
            } else if (i <= 65535L) {
                this.ensureCapacity(this.writeIndex + 3);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 2);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)i);
                this.writeIndex += 3;
            } else if (i <= 16777215L) {
                this.ensureCapacity(this.writeIndex + 4);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 3);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)i);
                this.writeIndex += 4;
            } else if (i <= -1L) {
                this.ensureCapacity(this.writeIndex + 5);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 4);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 24));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)((int)i);
                this.writeIndex += 5;
            } else if (i <= 1099511627775L) {
                this.ensureCapacity(this.writeIndex + 6);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 5);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 32));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 24));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 5] = (byte)((int)i);
                this.writeIndex += 6;
            } else if (i <= 281474976710655L) {
                this.ensureCapacity(this.writeIndex + 7);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 6);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 40));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 32));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)(i >>> 24));
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 5] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 6] = (byte)((int)i);
                this.writeIndex += 7;
            } else if (i <= 72057594037927935L) {
                this.ensureCapacity(this.writeIndex + 8);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 7);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 48));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 40));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)(i >>> 32));
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)((int)(i >>> 24));
                ((byte[])this.memory)[this.writeIndex + 5] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 6] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 7] = (byte)((int)i);
                this.writeIndex += 8;
            } else {
                this.ensureCapacity(this.writeIndex + 9);
                ((byte[])this.memory)[this.writeIndex] = (byte)(head - 8);
                ((byte[])this.memory)[this.writeIndex + 1] = (byte)((int)(i >>> 56));
                ((byte[])this.memory)[this.writeIndex + 2] = (byte)((int)(i >>> 48));
                ((byte[])this.memory)[this.writeIndex + 3] = (byte)((int)(i >>> 40));
                ((byte[])this.memory)[this.writeIndex + 4] = (byte)((int)(i >>> 32));
                ((byte[])this.memory)[this.writeIndex + 5] = (byte)((int)(i >>> 24));
                ((byte[])this.memory)[this.writeIndex + 6] = (byte)((int)(i >>> 16));
                ((byte[])this.memory)[this.writeIndex + 7] = (byte)((int)(i >>> 8));
                ((byte[])this.memory)[this.writeIndex + 8] = (byte)((int)i);
                this.writeIndex += 9;
            }

            return this;
        }
    }

    public long readVarLong() {
        byte b = ((byte[])this.memory)[this.readIndex++];
        if (b >= -112 && b <= 127) {
            return (long)b;
        } else {
            switch(b) {
                case -128:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 56 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -127:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -126:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -125:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -124:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -123:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -122:
                    return ~(((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -121:
                    return ~((long)((byte[])this.memory)[this.readIndex++] & 255L);
                case -120:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 56 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -119:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 48 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -118:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 40 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -117:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 32 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -116:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 24 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -115:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 16 | ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -114:
                    return ((long)((byte[])this.memory)[this.readIndex++] & 255L) << 8 | (long)((byte[])this.memory)[this.readIndex++] & 255L;
                case -113:
                    return (long)((byte[])this.memory)[this.readIndex++] & 255L;
                default:
                    throw new UnSupportException("not here");
            }
        }
    }

    public HeapByteBuf writeVarChar(char c) {
        this.ensureCapacity(this.writeIndex + 3);
        return this._writeVarChar(c);
    }

    private HeapByteBuf _writeVarChar(char c) {
        if (c <= 251) {
            ((byte[])this.memory)[this.writeIndex] = (byte)c;
            ++this.writeIndex;
        } else if (c <= 255) {
            ((byte[])this.memory)[this.writeIndex] = -4;
            ((byte[])this.memory)[this.writeIndex + 1] = (byte)c;
            this.writeIndex += 2;
        } else if (c <= '\uffff') {
            ((byte[])this.memory)[this.writeIndex] = -3;
            ((byte[])this.memory)[this.writeIndex + 1] = (byte)(c >>> 8);
            ((byte[])this.memory)[this.writeIndex + 2] = (byte)c;
            this.writeIndex += 3;
        }

        return this;
    }

    public char readVarChar() {
        int length = ((byte[])this.memory)[this.readIndex++] & 255;
        if (length <= 251) {
            return (char)length;
        } else if (length == 252) {
            length = ((byte[])this.memory)[this.readIndex++] & 255;
            return (char)length;
        } else if (length == 253) {
            length = (((byte[])this.memory)[this.readIndex++] & 255) << 8;
            length |= ((byte[])this.memory)[this.readIndex++] & 255;
            return (char)length;
        } else {
            throw new UnSupportException("not here");
        }
    }

    public HeapByteBuf writeString(String value) {
        if (value == null) {
            throw new NullPointerException();
        } else {
            int length = value.length();
            this.writePositive(length);
            this.ensureCapacity(this.writeIndex + length * 3);

            for(int i = 0; i < length; ++i) {
                this._writeVarChar(value.charAt(i));
            }

            return this;
        }
    }

    public String readString() {
        int length = this.readPositive();
        if (length == 0) {
            return "";
        } else {
            char[] src = new char[length];

            for(int i = 0; i < length; ++i) {
                src[i] = this.readVarChar();
            }

            return new String(src);
        }
    }

    public byte[] directArray() {
        return (byte[])this.memory;
    }

    public static ByteBuf<byte[]> wrap(byte[] t) {
        HeapByteBuf buf = new HeapByteBuf(t);
        buf.writeIndex(t.length);
        return buf;
    }
}
