package com.jfireframework.fse.data;

import java.util.Date;

/**
 * Device entity. @author MyEclipse Persistence Tools
 */

public class Device implements java.io.Serializable
{
    // Fields

    /**
     *
     */
    private static final long    serialVersionUID = -7833130819750178757L;
    private              long    id;
    private              String  sn;
    private              String  udid;
    private              String  openUdid;
    private              String  uuid;
    private              String  idfa;
    private              String  imei;
    private              String  mac;
    private              int     majorVersion;
    private              int     minorVersion;
    private              int     buildVersion;
    private              int     os;
    private              String  osVersion;
    private              int     promoPlatformCode;
    private              Date    activationTime;
    private              long    userId;
    private              boolean bound;

    private int[]       j  = new int[]{1, 2, 4, 5};
    private boolean[]   k  = new boolean[]{true, false, true, false, false, false, true};
    private char[]      l  = new char[]{'a', 'v', 'q', 'j', 'h', 'e', 'f'};
    private byte[]      m  = new byte[]{0x32, 0x12, 0x34, (byte) 0x96};
    private short[]     n  = new short[]{3, 8, 213, 451, 312, 45};
    private long[]      o  = new long[]{12313131313l, 524141431313l, 3131231231425l, 1313123121l};
    private double[]    p  = new double[]{6468613646.48646d, 4646.456d, 546864648867.466d};
    private float[]     q  = new float[]{46486.2f, 49849.2f, 646854.6f};
    private String[]    r  = new String[]{"abcdf12345", "abdfcgf12323"};
    private int[][]     j2 = new int[][]{{1, 2, 4, 5}, {1, 2, 3, 4, 5, 6}};
    private boolean[][] k2 = new boolean[][]{{true, false, true, false, false, false, true}, {true, false, true, false, false, false, false}};
    private char[][]    l2 = new char[][]{{'a', 'v', 'q', 'j', 'h', 'e', 'f'}, {'a', 'v', 'q', 'j', 'h', 'e', 'f'}};
    private byte[][]    m2 = new byte[][]{{0x32, 0x12, 0x34, (byte) 0x96}, {0x32, 0x12, 0x34, (byte) 0x96}};
    private short[][]   n2 = new short[][]{{3, 8, 213, 451, 312, 45}, {3, 8, 213, 451, 312, 45}};
    private long[][]    o2 = new long[][]{{12313131313l, 524141431313l, 3131231231425l, 1313123121l}, {12313131313l, 524141431313l, 3131231231425l, 1313123121l}};
    private double[][]  p2 = new double[][]{{6468613646.48646d, 4646.456d, 546864648867.466d}, {6468613646.48646d, 4646.456d, 546864648867.466d}};
    private float[][]   q2 = new float[][]{{46486.2f, 49849.2f, 646854.6f}, {46486.2f, 49849.2f, 646854.6f}};
    private String[][]  r2 = new String[][]{{"12qw", "sad123"}, {"xdr234", "5986sad"}};

    /**
     * default constructor
     */
    public Device()
    {
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getUdid()
    {
        return udid;
    }

    public void setUdid(String udid)
    {
        this.udid = udid;
    }

    public String getOpenUdid()
    {
        return openUdid;
    }

    public void setOpenUdid(String openUdid)
    {
        this.openUdid = openUdid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getIdfa()
    {
        return idfa;
    }

    public void setIdfa(String idfa)
    {
        this.idfa = idfa;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getMac()
    {
        return mac;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }

    public int getMajorVersion()
    {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion)
    {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion()
    {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion)
    {
        this.minorVersion = minorVersion;
    }

    public int getBuildVersion()
    {
        return buildVersion;
    }

    public void setBuildVersion(int buildVersion)
    {
        this.buildVersion = buildVersion;
    }

    public int getOs()
    {
        return os;
    }

    public void setOs(int os)
    {
        this.os = os;
    }

    public String getOsVersion()
    {
        return osVersion;
    }

    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }

    public int getPromoPlatformCode()
    {
        return promoPlatformCode;
    }

    public void setPromoPlatformCode(int promoPlatformCode)
    {
        this.promoPlatformCode = promoPlatformCode;
    }

    public Date getActivationTime()
    {
        return activationTime;
    }

    public void setActivationTime(Date activationTime)
    {
        this.activationTime = activationTime;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public boolean isBound()
    {
        return bound;
    }

    public void setBound(boolean bound)
    {
        this.bound = bound;
    }

    public int[] getJ()
    {
        return j;
    }

    public void setJ(int[] j)
    {
        this.j = j;
    }

    public boolean[] getK()
    {
        return k;
    }

    public void setK(boolean[] k)
    {
        this.k = k;
    }

    public char[] getL()
    {
        return l;
    }

    public void setL(char[] l)
    {
        this.l = l;
    }

    public byte[] getM()
    {
        return m;
    }

    public void setM(byte[] m)
    {
        this.m = m;
    }

    public short[] getN()
    {
        return n;
    }

    public void setN(short[] n)
    {
        this.n = n;
    }

    public long[] getO()
    {
        return o;
    }

    public void setO(long[] o)
    {
        this.o = o;
    }

    public double[] getP()
    {
        return p;
    }

    public void setP(double[] p)
    {
        this.p = p;
    }

    public float[] getQ()
    {
        return q;
    }

    public void setQ(float[] q)
    {
        this.q = q;
    }

    public String[] getR()
    {
        return r;
    }

    public void setR(String[] r)
    {
        this.r = r;
    }

    public int[][] getJ2()
    {
        return j2;
    }

    public void setJ2(int[][] j2)
    {
        this.j2 = j2;
    }

    public boolean[][] getK2()
    {
        return k2;
    }

    public void setK2(boolean[][] k2)
    {
        this.k2 = k2;
    }

    public char[][] getL2()
    {
        return l2;
    }

    public void setL2(char[][] l2)
    {
        this.l2 = l2;
    }

    public byte[][] getM2()
    {
        return m2;
    }

    public void setM2(byte[][] m2)
    {
        this.m2 = m2;
    }

    public short[][] getN2()
    {
        return n2;
    }

    public void setN2(short[][] n2)
    {
        this.n2 = n2;
    }

    public long[][] getO2()
    {
        return o2;
    }

    public void setO2(long[][] o2)
    {
        this.o2 = o2;
    }

    public double[][] getP2()
    {
        return p2;
    }

    public void setP2(double[][] p2)
    {
        this.p2 = p2;
    }

    public float[][] getQ2()
    {
        return q2;
    }

    public void setQ2(float[][] q2)
    {
        this.q2 = q2;
    }

    public String[][] getR2()
    {
        return r2;
    }

    public void setR2(String[][] r2)
    {
        this.r2 = r2;
    }
}
