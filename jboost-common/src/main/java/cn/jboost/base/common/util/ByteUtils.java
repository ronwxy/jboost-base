/*
 * Copyright (c) 2016-2019 Hunan CNBOT Co., Ltd. All Rights Reserved.
 * FileName: ByteUtils.java
 * @author: yuxk
 * @date: 19-3-29 下午2:16
 * @version: 1.0
 */

package cn.jboost.base.common.util;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yuxk
 * @version V1.0
 * @Title: byte处理工具类
 * @Description:
 * @date：2019/1/22 17:04
 */
@Slf4j
public class ByteUtils {

    /**
     * 校验和
     * @param data 字节数组
     * @return true 校验成功；false 校验失败
     */
    public static boolean checkSum(byte[] data) {
        int maxLen = 12;
        boolean b = false;

        if (data == null || data.length < maxLen) {
            return b;
        }

        int sum = 0;
        for (int i = 10; i < data.length - 1; i++) {
            sum = data[i] + sum;
        }

        BigInteger i = new BigInteger(Integer.valueOf(sum)
                .toString());
        byte[] temp = i.toByteArray();

        return (byte) (256 - (temp[temp.length - 1] & 0xFF)) == data[data.length - 1];
    }

    /**
     * 获取校验和
     * @param b 字节数组
     * @return 校验和字节
     */
    public static byte getCheckSum(byte[] b) {
        if (b == null || b.length <= 0) {
            return 0;
        }

        int c = 0;
        for (int i = 10; i < b.length; i++) {
            c = b[i] + c;
        }

        BigInteger i = new BigInteger(Integer.valueOf(c)
                .toString());
        byte[] temp = i.toByteArray();

        return (byte) (256 - (temp[temp.length - 1] & 0xFF));
    }

    /**
     * 求异或
     * @param datas
     * @return
     */
    public static byte getXor(byte[] datas) {

        byte temp = datas[0];

        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }

        return temp;
    }

    /**
     *求异或和
     */
    public static byte getXorSum(byte[] datas) {
        int bccCal = 0;
        for (int i = 0; i < datas.length - 1; i++) {
            bccCal ^= (datas[i] & 0x0FF);
        }
        return ByteUtils.intToByte(bccCal);
    }

    /**
     * 移除字节数组尾部连续为0x00的所有字节
     * @param b 字节数组
     * @return 字节数组
     */
    public static byte[] trimRightBytes(byte[] b) {
        int c = 0;
        for (int i = b.length - 1; i > 0; i--) {
            if (b[i] != 0x00) {
                break;
            }
            c++;
        }

        int l = b.length - c;

        byte[] bytes = new byte[l];

        System.arraycopy(b, 0, bytes, 0, l);

        return bytes;
    }

    /**
     * 移除字节数组头部连续为0x00的所有字节
     * @param b 字节数组
     * @return 字节数组
     */
    public static byte[] trimLeftBytes(byte[] b) {
        int c = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 0x00) {
                break;
            }
            c++;
        }

        int l = b.length - c;

        byte[] bytes = new byte[l];

        System.arraycopy(b, c, bytes, 0, l);

        return bytes;
    }

    /**
     * long类型转成byte数组
     * @param l long型数据
     * @return byte数组
     */
    public static byte[] longToByte(long l) {
        long temp = l;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();
            // 将最低位保存在最低位  向右移8位
            temp = temp >> 8;
        }
        return b;
    }

    /**
     * byte数组转成long
     * @param b byte数组
     * @return long型数据
     */
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b.length > 0
                ? b[0] & 0xff
                : 0;// 最低位
        long s1 = b.length > 1
                ? b[1] & 0xff
                : 0;
        long s2 = b.length > 2
                ? b[2] & 0xff
                : 0;
        long s3 = b.length > 3
                ? b[3] & 0xff
                : 0;
        long s4 = b.length > 4
                ? b[4] & 0xff
                : 0;// 最低位
        long s5 = b.length > 5
                ? b[5] & 0xff
                : 0;
        long s6 = b.length > 6
                ? b[6] & 0xff
                : 0;
        long s7 = b.length > 7
                ? b[7] & 0xff
                : 0;

        // s0不变
        s1 <<= 8;
        s2 <<= 8 * 2;
        s3 <<= 8 * 3;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * int转换成byte数组（4位）
     * @param number int型数据
     * @return byte数组
     */
    public static byte[] intToByteArr(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();
            // 将最低位保存在最低位  向右移8位
            temp = temp >> 8;
        }
        return b;
    }

    /**
     * int结果码转换数组（2位）
     * smallInt转换成byte数组
     * @param number int型数据
     * @return byte数组
     */
    public static byte[] smallIntToByteArr(int number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();
            // 将最低位保存在最低位  向右移8位
            temp = temp >> 8;
        }
        return b;
    }


    /**
     * byte[]与int转换
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }


    /**
     * byte[]与int转换
     * @param a
     * @return
     */
    public static Byte[] intToByteArray(int a) {
        return new Byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * int转换成byte
     * @param number int型数据
     * @return byte数组
     */
    public static byte intToByte(int number) {
        int temp = number;
        byte b = new Integer(temp & 0xff).byteValue();
        return b;
    }

    /**
     * byte转换成int
     * @return byte数组
     */
    public static int byteToInt(byte data) {
        return data & 0XFF;
    }


    /**
     * byte数组转换成int
     * @param b byte数组
     * @return int型数据
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b.length > 0 ? b[0] & 0xff : 0;// 最低位
        int s1 = b.length > 1 ? b[1] & 0xff : 0;
        int s2 = b.length > 2 ? b[2] & 0xff : 0;
        int s3 = b.length > 3 ? b[3] & 0xff : 0;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }


    /**
     * short转换成byte数组
     * @param s short型数据
     * @return byte数组
     */
    public static byte[] shortToByte(short s) {
        int temp = s;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();
            // 将最低位保存在最低位  向右移8位
            temp = temp >> 8;
        }
        return b;
    }

    /**
     * byte数组转换成short
     * @param b byte数组
     * @return short型数据
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = b.length > 0
                ? (short) (b[0] & 0xff)
                : 0;// 最低位
        short s1 = b.length > 1
                ? (short) (b[1] & 0xff)
                : 0;
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 字符串转字节数组
     * @param str 要转换的字符串
     * @param charEncode 字符编码，如：UTF-8、GB2312，UTF-8编码一个汉字占三个字节，GB2312一个汉字占两个字节
     * @return 字节数组
     */
    public static byte[] stringToByte(String str, String charEncode) {
        byte[] destObj = null;
        try {
            if (null == str || "".equals(str.trim())) {
                destObj = new byte[0];
                return destObj;
            } else {
                destObj = str.getBytes(charEncode);
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        return destObj;
    }


    /**
     * 分割字节数组
     * @param b 字节数组
     * @param split 分隔字节
     * @return 二维字节数组
     */
    public static byte[][] splitByte(byte[] b, byte split) {
        List<Integer> indexs = new ArrayList<Integer>();
        for (int i = 0; i < b.length; i++) {
            if (b[i] == split) {
                indexs.add(i);
            }
        }
        byte[][] bb = new byte[indexs.size() + 1][];
        for (int i = 0; i < indexs.size() + 1; i++) {
            int start = i == 0
                    ? 0
                    : indexs.get(i - 1) + 1;
            int end = i == indexs.size()
                    ? b.length
                    : indexs.get(i);
            bb[i] = new byte[end - start];
            System.arraycopy(b, start, bb[i], 0, bb[i].length);
        }
        return bb;
    }

    /**
     * Object对象转换成字节数组
     * @param obj Object对象
     * @return 字节数组
     */
    public static byte[] objectToByte(Object obj) {
        byte[] bytes = new byte[1024];
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return (bytes);
    }

    /**
     * 字节数组转Object对象
     * @param bytes 字节数组
     * @return Object对象
     */
    public static Object byteToObject(byte[] bytes) {
        Object obj = new Object();
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();

            bi.close();
            oi.close();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return obj;
    }

    /**
     * 字节数组转换成十六进制字符串
     * @param b 字节数组
     * @param split 分隔字符串，主要用于输出调试
     * @return
     */
    public static String byteArrToHexStr(byte[] b, String split) {
        String zero = "0";
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + zero + stmp;
            } else {
                hs = hs + stmp;
            }
            if(n != b.length-1){
                hs = hs + split;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 字节数组转换成十六进制字符串数组
     * @param b 字节数组
     * @return
     */
    public static String[] byteArrToHexStrArr(byte[] b) {
        String zero = "0";
        String hs = "";
        String stmp = "";
        String[] str = new String[b.length];
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = zero + stmp;
            } else {
                hs = stmp;
            }
            str[n] = hs.toUpperCase();
        }
        return str;
    }

    /**
     * 字节转换成十六进制字符串
     * @param b 字节
     * @param split 分隔字符串，主要用于输出调试
     * @return
     */
    public static String byteToHexStr(byte b, String split) {
        String zero = "0";
        String hs = "";
        String stmp = "";
        stmp = (Integer.toHexString(b & 0XFF));
        if (stmp.length() == 1) {
            hs = hs + zero + stmp;
        } else {
            hs = hs + stmp;
        }
        hs = hs + split;
        return hs.toUpperCase();
    }

    /**
     * 字节数组转换成十六进制字符串
     * @param b 字节数组
     * @param split 分隔字符串，主要用于输出调试
     * @return
     */
    public static String byteToHexStr(byte[] b, int len, String split) {
        String zero = "0";
        String hs = "";
        String stmp = "";
        for (int n = 0; n < len; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + zero + stmp;
            } else {
                hs = hs + stmp;
            }
            if(n != b.length-1){
                hs = hs + split;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] clearZear(byte[] b, int len) {
        List<Byte> list = new ArrayList<Byte>();
        boolean isStart = false;
        for (int i = 0; i < len; i++) {
            if (b[i] == 0x00) {
                if (!isStart) {
                    list.add(b[i]);
                }
                isStart = true;
            } else {
                isStart = false;
                list.add(b[i]);
            }
        }
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }

    /**
     * 十六进制字符串转换成字节数组
     * @param hexstr 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStrToByte(String hexstr) {
        int len = (hexstr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexstr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (((byte) "0123456789ABCDEF".indexOf(achar[pos])) << 4 | ((byte) "0123456789ABCDEF".indexOf(
                    achar[pos + 1])));
        }
        return result;
    }

    public static byte[] irConvert(byte[] data,
                                   int lead1Units,
                                   int lead2Units,
                                   int zore1Units,
                                   int zore2Units,
                                   int one1Units,
                                   int one2Units) {
        int len = 2;
        int jLen = 8;
        byte[] result = new byte[data.length * 8 * 2 * 2 + 4];

        result[0] = (byte) 0x80;
        result[1] = ByteUtils.intToByteArr(lead1Units)[0];
        result[2] = (byte) 0x00;
        result[3] = ByteUtils.intToByteArr(lead2Units)[0];

        for (int i = 4; i < result.length; i = i + len) {
            if (i % 4 == 0) {
                result[i] = (byte) 0x80;
            } else {
                result[i] = (byte) 0x00;
            }
        }

        for (int i = 0; i < data.length; i++) {
            int point = i * 32 + 4;
            byte b = data[i];
            for (int j = 0; j < jLen; j++) {
                if (b >> j == 0x00) {
                    result[point + j * 4 + 1] = (byte) zore1Units;
                    result[point + j * 4 + 3] = (byte) zore2Units;
                } else {
                    result[point + j * 4 + 1] = (byte) one1Units;
                    result[point + j * 4 + 3] = (byte) one2Units;
                }
            }
        }

        return result;
    }

    /**
     * 将int转换为16进制字符串
     * @param token
     * @param split
     * @return
     */
    public static String intToHexStr(int token, String split) {
        String zero = "0";
        String hs = "";
        String stmp = "";
        stmp = (Integer.toHexString(token));
        if (stmp.length() == 1) {
            hs += zero + stmp;
        } else {
            hs = hs + stmp;
        }
        hs += split;
        return hs.toUpperCase();

    }


    /**
     * 字节转10进制
     */
    public static int byte2Int(byte b) {
        int r = (int) b;
        return r;
    }

    /**
     * 字节数组转字符串
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b, "UTF-8");
        return r;
    }

    /**
     * 字节数组转字符串
     * @param b 要转换的字节数组
     * @param charEncode 字符编码，如：UTF-8、GB2312，UTF-8编码一个汉字占三个字节，GB2312一个汉字占两个字节
     * @return 字符串
     */
    public static String byteToString(byte[] b, String charEncode) {
        String destObj = null;
        try {
            destObj = new String(b, charEncode);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        return destObj.replaceAll("\0", " ");
    }
    public static String byteToString(byte[] b) {
        return byteToString(b,"UTF-8");
    }
}