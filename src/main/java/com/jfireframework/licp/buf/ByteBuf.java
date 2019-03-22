//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jfireframework.licp.buf;

import com.jfireframework.baseutil.Verify;
import com.jfireframework.baseutil.collection.StringCache;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public abstract class ByteBuf<T> {
    protected static final int[] offsets = new int[]{0, 8, 16, 24, 32, 40, 48, 56};
    protected int capacity;
    protected int writeIndex = 0;
    protected int maskWrite = 0;
    protected int maskRead = 0;
    protected int readIndex = 0;
    protected T memory;
    protected static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected ByteBuffer cachedNioBuffer;

    public ByteBuf() {
    }

    public ByteBuf<T> maskRead() {
        this.maskRead = this.readIndex;
        return this;
    }

    public ByteBuf<T> resetRead() {
        this.readIndex = this.maskRead;
        return this;
    }

    public int readIndex() {
        return this.readIndex;
    }

    public ByteBuf<T> readIndex(int readIndex) {
        this.readIndex = readIndex;
        return this;
    }

    public ByteBuf<T> addReadIndex(int length) {
        this.readIndex += length;
        return this;
    }

    public ByteBuf<T> addWriteIndex(int length) {
        this.writeIndex += length;
        this.ensureCapacity(this.writeIndex);
        return this;
    }

    public int writeIndex() {
        return this.writeIndex;
    }

    public ByteBuf<T> maskWrite() {
        this.maskWrite = this.writeIndex;
        return this;
    }

    public ByteBuf<T> resetWrite() {
        this.writeIndex = this.maskWrite;
        return this;
    }

    public ByteBuf<T> writeIndex(int writeIndex) {
        this.writeIndex = writeIndex;
        return this;
    }

    public abstract ByteBuffer nioBuffer();

    public ByteBuffer cachedNioBuffer() {
        if (this.cachedNioBuffer == null) {
            this.cachedNioBuffer = this.nioBuffer();
        }

        return this.cachedNioBuffer;
    }

    public void put(ByteBuffer buffer, int length) {
        this.ensureCapacity(this.writeIndex + length);
        this._put(buffer, length);
        this.writeIndex += length;
    }

    protected abstract void _put(ByteBuffer var1, int var2);

    public ByteBuf<T> put(byte b) {
        this.ensureCapacity(this.writeIndex + 1);
        this._put(this.writeIndex, b);
        ++this.writeIndex;
        return this;
    }

    protected abstract void _put(int var1, byte var2);

    public void put(int index, byte b) {
        this._put(index, b);
    }

    public ByteBuf<T> put(byte[] content) {
        return this.put(content, 0, content.length);
    }

    public ByteBuf<T> put(byte[] content, int off, int len) {
        this.ensureCapacity(this.writeIndex + len);
        this._put(content, off, len);
        this.writeIndex += len;
        return this;
    }

    protected abstract void _put(byte[] var1, int var2, int var3);

    public ByteBuf<T> ensureCapacity(int newSize) {
        if (this.capacity < newSize) {
            this._expend(newSize);
        }

        return this;
    }

    protected abstract void _expend(int var1);

    public ByteBuf<T> clear() {
        this.writeIndex = 0;
        this.readIndex = 0;
        return this;
    }

    public abstract byte get(int var1);

    public abstract byte get();

    public int size() {
        return this.capacity;
    }

    public int remainRead() {
        return this.writeIndex - this.readIndex;
    }

    public int remainWrite() {
        return this.capacity - this.writeIndex;
    }

    public abstract byte[] toArray();

    public abstract ByteBuf<T> compact();

    public void get(byte[] content, int length) {
        Verify.True(length <= this.remainRead(), "需要读取的长度太长，没有足够的数据可以读取", new Object[0]);
        this._get(content, 0, length);
    }

    public void get(byte[] content, int off, int len) {
        Verify.True(len <= this.remainRead(), "需要读取的长度太长，没有足够的数据可以读取", new Object[0]);
        this._get(content, off, len);
    }

    protected abstract void _get(byte[] var1, int var2, int var3);

    public abstract String toString(Charset var1, int var2);

    public String toString(Charset charset) {
        return this.toString(charset, this.remainRead());
    }

    public String toString() {
        return (new StringCache("readIndex:")).append(this.readIndex).appendComma().append("writeIndex:").append(this.writeIndex).appendComma().append("capacity:").append(this.capacity).toString();
    }

    public ByteBuf<T> put(ByteBuf<?> byteBuf) {
        return this.put(byteBuf, byteBuf.remainRead());
    }

    public ByteBuf<T> put(ByteBuf<?> byteBuf, int length) {
        this.ensureCapacity(this.writeIndex + length);
        this._put(byteBuf, length);
        this.writeIndex += length;
        return this;
    }

    protected abstract void _put(ByteBuf<?> var1, int var2);

    public ByteBuf<T> writeInt(int i) {
        this.ensureCapacity(this.writeIndex + 4);
        this._writeInt(this.writeIndex, i);
        this.writeIndex += 4;
        return this;
    }

    public ByteBuf<T> writeInt(int index, int i) {
        this._writeInt(index, i);
        return this;
    }

    protected abstract void _writeInt(int var1, int var2);

    public ByteBuf<T> writeShort(short s) {
        this.ensureCapacity(this.writeIndex + 2);
        this._writeShort(this.writeIndex, s);
        this.writeIndex += 2;
        return this;
    }

    public ByteBuf<T> writeShort(int index, short s) {
        this._writeShort(index, s);
        return this;
    }

    protected abstract void _writeShort(int var1, short var2);

    public ByteBuf<T> writeLong(long l) {
        this.ensureCapacity(this.writeIndex + 8);
        this._writeLong(this.writeIndex, l);
        this.writeIndex += 8;
        return this;
    }

    public ByteBuf<T> writeLong(int index, long l) {
        this._writeLong(index, l);
        return this;
    }

    protected abstract void _writeLong(int var1, long var2);

    public ByteBuf<T> writeFloat(float f) {
        return this.writeInt(Float.floatToRawIntBits(f));
    }

    public ByteBuf<T> writeFloat(int index, float f) {
        return this.writeInt(index, Float.floatToRawIntBits(f));
    }

    public ByteBuf<T> writeDouble(double d) {
        return this.writeLong(Double.doubleToRawLongBits(d));
    }

    public ByteBuf<T> writeDouble(int index, double d) {
        return this.writeLong(index, Double.doubleToRawLongBits(d));
    }

    public ByteBuf<T> writeChar(char c) {
        this.ensureCapacity(this.writeIndex + 2);
        this._writeChar(this.writeIndex, c);
        this.writeIndex += 2;
        return this;
    }

    public ByteBuf<T> writeChar(int index, char c) {
        this._writeChar(index, c);
        return this;
    }

    protected abstract void _writeChar(int var1, char var2);

    public ByteBuf<T> writeBoolean(boolean b) {
        this.ensureCapacity(this.writeIndex + 1);
        this._writeBoolean(this.writeIndex, b);
        ++this.writeIndex;
        return this;
    }

    protected abstract void _writeBoolean(int var1, boolean var2);

    public abstract int readInt();

    public abstract int readInt(int var1);

    public abstract short readShort();

    public abstract short readShort(int var1);

    public abstract long readLong();

    public abstract long readLong(int var1);

    public abstract char readChar();

    public abstract char readChar(int var1);

    public abstract float readFloat();

    public abstract float readFloat(int var1);

    public abstract double readDouble();

    public abstract double readDouble(int var1);

    public abstract boolean readBoolean();

    public int indexOf(byte[] src) {
        int length = src.length;

        label29:
        for(int i = this.readIndex; i < this.writeIndex; ++i) {
            if (this.get(i) == src[0] && i + length < this.writeIndex) {
                for(int j = 0; j < length; ++j) {
                    if (this.get(i + j) != src[j]) {
                        continue label29;
                    }
                }

                return i;
            }
        }

        return -1;
    }

    public abstract String hexString();

    public ByteBuf<T> writeIntArray(int[] array) {
        this.writeInt(array.length);
        int[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            this.writeInt(i);
        }

        return this;
    }

    public int[] readIntArray() {
        int length = this.readInt();
        int[] tmp = new int[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readInt();
        }

        return tmp;
    }

    public int[] readIntArray(int length) {
        int[] tmp = new int[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readInt();
        }

        return tmp;
    }

    public ByteBuf<T> writeLongArray(long[] array) {
        this.writeInt(array.length);
        long[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            long i = var2[var4];
            this.writeLong(i);
        }

        return this;
    }

    public long[] readLongArray() {
        int length = this.readInt();
        long[] tmp = new long[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readLong();
        }

        return tmp;
    }

    public long[] readLongArray(int length) {
        long[] tmp = new long[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readLong();
        }

        return tmp;
    }

    public ByteBuf<T> writeFloatArray(float[] array) {
        this.writeInt(array.length);
        float[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            float each = var2[var4];
            this.writeFloat(each);
        }

        return this;
    }

    public float[] readFloatArray() {
        int length = this.readInt();
        return this.readFloatArray(length);
    }

    public float[] readFloatArray(int length) {
        float[] tmp = new float[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readFloat();
        }

        return tmp;
    }

    public ByteBuf<T> writeDoubleArray(double[] array) {
        this.writeInt(array.length);
        double[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            double each = var2[var4];
            this.writeDouble(each);
        }

        return this;
    }

    public double[] readDoubleArray() {
        int length = this.readInt();
        return this.readDoubleArray(length);
    }

    public double[] readDoubleArray(int length) {
        double[] tmp = new double[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readDouble();
        }

        return tmp;
    }

    public ByteBuf<T> writeShortArray(short[] array) {
        this.writeInt(array.length);
        short[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            short each = var2[var4];
            this.writeShort(each);
        }

        return this;
    }

    public short[] readShortArray() {
        int length = this.readInt();
        return this.readShortArray(length);
    }

    public short[] readShortArray(int length) {
        short[] tmp = new short[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readShort();
        }

        return tmp;
    }

    public ByteBuf<T> writeCharArray(char[] array) {
        this.writeInt(array.length);
        this.ensureCapacity(array.length * 2 + this.writeIndex);
        char[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char each = var2[var4];
            this._writeChar(this.writeIndex, each);
            this.writeIndex += 2;
        }

        return this;
    }

    public char[] readCharArray() {
        int length = this.readInt();
        return this.readCharArray(length);
    }

    public char[] readCharArray(int length) {
        char[] tmp = new char[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readChar();
        }

        return tmp;
    }

    public ByteBuf<T> writeByteArray(byte[] array) {
        this.writeInt(array.length);
        this.put(array);
        return this;
    }

    public byte[] readByteArray() {
        int length = this.readInt();
        return this.readByteArray(length);
    }

    public byte[] readByteArray(int length) {
        byte[] tmp = new byte[length];
        this.get(tmp, length);
        return tmp;
    }

    public ByteBuf<T> writeBooleanArray(boolean[] array) {
        this.writeInt(array.length);
        boolean[] var2 = array;
        int var3 = array.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            boolean each = var2[var4];
            this.writeBoolean(each);
        }

        return this;
    }

    public boolean[] readBooleanArray() {
        int length = this.readInt();
        return this.readBooleanArray(length);
    }

    public boolean[] readBooleanArray(int length) {
        boolean[] tmp = new boolean[length];

        for(int i = 0; i < length; ++i) {
            tmp[i] = this.readBoolean();
        }

        return tmp;
    }

    public abstract void release();

    public abstract void writePositive(int var1);

    public abstract int readPositive();

    public abstract ByteBuf<T> writeVarint(int var1);

    public abstract int readVarint();

    public abstract ByteBuf<T> writeVarLong(long var1);

    public abstract long readVarLong();

    public abstract ByteBuf<T> writeVarChar(char var1);

    public abstract char readVarChar();

    public abstract ByteBuf<T> writeString(String var1);

    public abstract String readString();
}
