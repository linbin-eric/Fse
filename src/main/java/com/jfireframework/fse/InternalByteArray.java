package com.jfireframework.fse;

public class InternalByteArray extends ByteArray
{
    public InternalByteArray(int size)
    {
        super(size);
    }

    public InternalByteArray(byte[] array)
    {
        super(array);
    }

    public void writeVarInt(int i)
    {
        if (i >= -120 && i <= 127)
        {
            ensureCapacity(1);
            array[writePosi] = (byte) i;
            writePosi += 1;
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
                ensureCapacity(2);
                array[writePosi] = (byte) (head - 1);
                array[writePosi + 1] = (byte) i;
                writePosi += 2;
            }
            else if (i <= 65535)
            {
                ensureCapacity(3);
                array[writePosi] = (byte) (head - 2);
                array[writePosi + 1] = (byte) (i >>> 8);
                array[writePosi + 2] = (byte) i;
                writePosi += 3;
            }
            else if (i <= 16777215)
            {
                ensureCapacity(4);
                array[writePosi] = (byte) (head - 3);
                array[writePosi + 1] = (byte) (i >>> 16);
                array[writePosi + 2] = (byte) (i >>> 8);
                array[writePosi + 3] = (byte) i;
                writePosi += 4;
            }
            else
            {
                ensureCapacity(5);
                array[writePosi] = (byte) (head - 4);
                array[writePosi + 1] = (byte) (i >>> 24);
                array[writePosi + 2] = (byte) (i >>> 16);
                array[writePosi + 3] = (byte) (i >>> 8);
                array[writePosi + 4] = (byte) i;
                writePosi += 5;
            }
        }
    }

    public int readVarInt()
    {
        byte b = array[readIndex++];
        if (b >= -120 && b <= 127)
        {
            return b;
        }
        else
        {
            switch (b)
            {
                case -128:
                    return ~((array[readIndex++] & 255) << 24 | (array[readIndex++] & 255) << 16 | (array[readIndex++] & 255) << 8 | array[readIndex++] & 255);
                case -127:
                    return ~((array[readIndex++] & 255) << 16 | (array[readIndex++] & 255) << 8 | array[readIndex++] & 255);
                case -126:
                    return ~((array[readIndex++] & 255) << 8 | array[readIndex++] & 255);
                case -125:
                    return ~(array[readIndex++] & 255);
                case -124:
                    return (array[readIndex++] & 255) << 24 | (array[readIndex++] & 255) << 16 | (array[readIndex++] & 255) << 8 | array[readIndex++] & 255;
                case -123:
                    return (array[readIndex++] & 255) << 16 | (array[readIndex++] & 255) << 8 | array[readIndex++] & 255;
                case -122:
                    return (array[readIndex++] & 255) << 8 | array[readIndex++] & 255;
                case -121:
                    return array[readIndex++] & 255;
                default:
                    throw new IllegalArgumentException("not here");
            }
        }
    }

    public void writeVarLong(long i)
    {
        if (i >= -112L && i <= 127L)
        {
            ensureCapacity(1);
            array[writePosi] = (byte) ((int) i);
            writePosi += 1;
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
                ensureCapacity(2);
                array[writePosi] = (byte) (head - 1);
                array[writePosi + 1] = (byte) ((int) i);
                writePosi += 2;
            }
            else if (i <= 65535L)
            {
                ensureCapacity(3);
                array[writePosi] = (byte) (head - 2);
                array[writePosi + 1] = (byte) ((int) (i >>> 8));
                array[writePosi + 2] = (byte) ((int) i);
                writePosi += 3;
            }
            else if (i <= 16777215L)
            {
                ensureCapacity(4);
                array[writePosi] = (byte) (head - 3);
                array[writePosi + 1] = (byte) ((int) (i >>> 16));
                array[writePosi + 2] = (byte) ((int) (i >>> 8));
                array[writePosi + 3] = (byte) ((int) i);
                writePosi += 4;
            }
            else if (i <= -1L)
            {
                ensureCapacity(5);
                array[writePosi] = (byte) (head - 4);
                array[writePosi + 1] = (byte) ((int) (i >>> 24));
                array[writePosi + 2] = (byte) ((int) (i >>> 16));
                array[writePosi + 3] = (byte) ((int) (i >>> 8));
                array[writePosi + 4] = (byte) ((int) i);
                writePosi += 5;
            }
            else if (i <= 1099511627775L)
            {
                ensureCapacity(6);
                array[writePosi] = (byte) (head - 5);
                array[writePosi + 1] = (byte) ((int) (i >>> 32));
                array[writePosi + 2] = (byte) ((int) (i >>> 24));
                array[writePosi + 3] = (byte) ((int) (i >>> 16));
                array[writePosi + 4] = (byte) ((int) (i >>> 8));
                array[writePosi + 5] = (byte) ((int) i);
                writePosi += 6;
            }
            else if (i <= 281474976710655L)
            {
                ensureCapacity(7);
                array[writePosi] = (byte) (head - 6);
                array[writePosi + 1] = (byte) ((int) (i >>> 40));
                array[writePosi + 2] = (byte) ((int) (i >>> 32));
                array[writePosi + 3] = (byte) ((int) (i >>> 24));
                array[writePosi + 4] = (byte) ((int) (i >>> 16));
                array[writePosi + 5] = (byte) ((int) (i >>> 8));
                array[writePosi + 6] = (byte) ((int) i);
                writePosi += 7;
            }
            else if (i <= 72057594037927935L)
            {
                ensureCapacity(8);
                array[writePosi] = (byte) (head - 7);
                array[writePosi + 1] = (byte) ((int) (i >>> 48));
                array[writePosi + 2] = (byte) ((int) (i >>> 40));
                array[writePosi + 3] = (byte) ((int) (i >>> 32));
                array[writePosi + 4] = (byte) ((int) (i >>> 24));
                array[writePosi + 5] = (byte) ((int) (i >>> 16));
                array[writePosi + 6] = (byte) ((int) (i >>> 8));
                array[writePosi + 7] = (byte) ((int) i);
                writePosi += 8;
            }
            else
            {
                ensureCapacity(9);
                array[writePosi] = (byte) (head - 8);
                array[writePosi + 1] = (byte) ((int) (i >>> 56));
                array[writePosi + 2] = (byte) ((int) (i >>> 48));
                array[writePosi + 3] = (byte) ((int) (i >>> 40));
                array[writePosi + 4] = (byte) ((int) (i >>> 32));
                array[writePosi + 5] = (byte) ((int) (i >>> 24));
                array[writePosi + 6] = (byte) ((int) (i >>> 16));
                array[writePosi + 7] = (byte) ((int) (i >>> 8));
                array[writePosi + 8] = (byte) ((int) i);
                writePosi += 9;
            }
        }
    }

    public long readVarLong()
    {
        byte b = array[readIndex++];
        if (b >= -112 && b <= 127)
        {
            return (long) b;
        }
        else
        {
            switch (b)
            {
                case -128:
                    return ~(((long) array[readIndex++] & 255L) << 56 | ((long) array[readIndex++] & 255L) << 48 | ((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -127:
                    return ~(((long) array[readIndex++] & 255L) << 48 | ((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -126:
                    return ~(((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -125:
                    return ~(((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -124:
                    return ~(((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -123:
                    return ~(((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -122:
                    return ~(((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L);
                case -121:
                    return ~((long) array[readIndex++] & 255L);
                case -120:
                    return ((long) array[readIndex++] & 255L) << 56 | ((long) array[readIndex++] & 255L) << 48 | ((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -119:
                    return ((long) array[readIndex++] & 255L) << 48 | ((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -118:
                    return ((long) array[readIndex++] & 255L) << 40 | ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -117:
                    return ((long) array[readIndex++] & 255L) << 32 | ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -116:
                    return ((long) array[readIndex++] & 255L) << 24 | ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -115:
                    return ((long) array[readIndex++] & 255L) << 16 | ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -114:
                    return ((long) array[readIndex++] & 255L) << 8 | (long) array[readIndex++] & 255L;
                case -113:
                    return (long) array[readIndex++] & 255L;
                default:
                    throw new IllegalArgumentException("not here");
            }
        }
    }

    public void writeVarCharWithoutCheck(char c)
    {
        if (c <= 251)
        {
            array[writePosi] = (byte) c;
            writePosi += 1;
        }
        else if (c <= 255)
        {
            array[writePosi] = -4;
            array[writePosi + 1] = (byte) c;
            writePosi += 2;
        }
        else if (c <= '\uffff')
        {
            array[writePosi] = -3;
            array[writePosi + 1] = (byte) (c >>> 8);
            array[writePosi + 2] = (byte) c;
            writePosi += 3;
        }
    }

    public void writeVarChar(char c)
    {
        if (c <= 251)
        {
            ensureCapacity(1);
            array[writePosi] = (byte) c;
            ++writePosi;
        }
        else if (c <= 255)
        {
            ensureCapacity(2);
            array[writePosi] = -4;
            array[writePosi + 1] = (byte) c;
            writePosi += 2;
        }
        else if (c <= '\uffff')
        {
            ensureCapacity(3);
            array[writePosi] = -3;
            array[writePosi + 1] = (byte) (c >>> 8);
            array[writePosi + 2] = (byte) c;
            writePosi += 3;
        }
    }

    public char readVarChar()
    {
        int length = array[readIndex++] & 255;
        if (length <= 251)
        {
            return (char) length;
        }
        else if (length == 252)
        {
            length = array[readIndex++] & 255;
            return (char) length;
        }
        else if (length == 253)
        {
            length = (array[readIndex++] & 255) << 8;
            length |= array[readIndex++] & 255;
            return (char) length;
        }
        else
        {
            throw new IllegalArgumentException("not here");
        }
    }

    public void writePositive(int positive)
    {
        if (positive < 0)
        {
            throw new UnsupportedOperationException();
        }
        else
        {
            if (positive <= 251)
            {
                ensureCapacity(1);
                array[writePosi] = (byte) positive;
                ++writePosi;
            }
            else if (positive <= 255)
            {
                ensureCapacity(2);
                array[writePosi] = -4;
                array[writePosi + 1] = (byte) positive;
                writePosi += 2;
            }
            else if (positive <= 65535)
            {
                ensureCapacity(3);
                array[writePosi] = -3;
                array[writePosi + 1] = (byte) (positive >>> 8);
                array[writePosi + 2] = (byte) positive;
                writePosi += 3;
            }
            else if (positive <= 16777215)
            {
                ensureCapacity(4);
                array[writePosi] = -2;
                array[writePosi + 1] = (byte) (positive >>> 16);
                array[writePosi + 2] = (byte) (positive >>> 8);
                array[writePosi + 3] = (byte) positive;
                writePosi += 4;
            }
            else
            {
                ensureCapacity(5);
                array[writePosi] = -1;
                array[writePosi + 1] = (byte) (positive >>> 24);
                array[writePosi + 2] = (byte) (positive >>> 16);
                array[writePosi + 3] = (byte) (positive >>> 8);
                array[writePosi + 4] = (byte) positive;
                writePosi += 5;
            }
        }
    }

    public int readPositive()
    {
        int length = array[readIndex++] & 255;
        if (length <= 251)
        {
            return length;
        }
        else if (length == 252)
        {
            length = array[readIndex++] & 255;
            return length;
        }
        else if (length == 253)
        {
            length = (array[readIndex++] & 255) << 8;
            length |= array[readIndex++] & 255;
            return length;
        }
        else if (length == 254)
        {
            length = (array[readIndex++] & 255) << 16;
            length |= (array[readIndex++] & 255) << 8;
            length |= array[readIndex++] & 255;
            return length;
        }
        else if (length == 255)
        {
            length = (array[readIndex++] & 255) << 24;
            length |= (array[readIndex++] & 255) << 16;
            length |= (array[readIndex++] & 255) << 8;
            length |= array[readIndex++] & 255;
            return length;
        }
        else
        {
            throw new RuntimeException("wrong data");
        }
    }

    public void writeInt(int i)
    {
        ensureCapacity(4);
        array[writePosi] = (byte) (i >> 24);
        array[writePosi + 1] = (byte) (i >> 16);
        array[writePosi + 2] = (byte) (i >> 8);
        array[writePosi + 3] = (byte) i;
        writePosi += 4;
    }

    public void writeShort(short s)
    {
        ensureCapacity(2);
        array[writePosi] = (byte) (s >> 8);
        array[writePosi + 1] = (byte) s;
        writePosi += 2;
    }

    public void writeLong(long l)
    {
        ensureCapacity(8);
        array[writePosi] = (byte) ((int) (l >> 56));
        array[writePosi + 1] = (byte) ((int) (l >> 48));
        array[writePosi + 2] = (byte) ((int) (l >> 40));
        array[writePosi + 3] = (byte) ((int) (l >> 32));
        array[writePosi + 4] = (byte) ((int) (l >> 24));
        array[writePosi + 5] = (byte) ((int) (l >> 16));
        array[writePosi + 6] = (byte) ((int) (l >> 8));
        array[writePosi + 7] = (byte) ((int) l);
        writePosi += 8;
    }

    public int readInt()
    {
        int i = (array[readIndex] & 255) << 24;
        i |= (array[readIndex + 1] & 255) << 16;
        i |= (array[readIndex + 2] & 255) << 8;
        i |= array[readIndex + 3] & 255;
        this.readIndex += 4;
        return i;
    }

    public short readShort()
    {
        short s = (short) ((array[readIndex] & 255) << 8);
        s = (short) (s | array[readIndex + 1] & 255);
        this.readIndex += 2;
        return s;
    }

    public long readLong()
    {
        long l = (long) array[readIndex] << 56 | ((long) array[readIndex + 1] & 255L) << 48 | ((long) array[readIndex + 2] & 255L) << 40 | ((long) array[readIndex + 3] & 255L) << 32 | ((long) array[readIndex + 4] & 255L) << 24 | ((long) array[readIndex + 5] & 255L) << 16 | ((long) array[readIndex + 6] & 255L) << 8 | (long) array[readIndex + 7] & 255L;
        this.readIndex += 8;
        return l;
    }

    public void writeFloat(float f)
    {
        writeInt(Float.floatToRawIntBits(f));
    }

    public void writeDouble(double d)
    {
        writeLong(Double.doubleToRawLongBits(d));
    }

    public float readFloat()
    {
        int i = readInt();
        return Float.intBitsToFloat(i);
    }

    public double readDouble()
    {
        long l = readLong();
        return Double.longBitsToDouble(l);
    }

    public void skipWrite(int len)
    {
        ensureCapacity(len);
        writePosi += 4;
    }

    public void writeString(String value)
    {
        if (value == null)
        {
            writeVarInt(-1);
        }
        else
        {
            int length = value.length();
            writeVarInt(length);
            ensureCapacity(3 * length);
            for (int i = 0; i < length; ++i)
            {
                writeVarCharWithoutCheck(value.charAt(i));
            }
        }
    }

    public String readString()
    {
        int length = readVarInt();
        if (length == -1)
        {
            return null;
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

    public boolean remainRead()
    {
        return readIndex < writePosi;
    }
}
