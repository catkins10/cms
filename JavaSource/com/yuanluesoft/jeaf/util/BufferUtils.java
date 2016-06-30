/*
 * Created on 2006-10-20
 *
 */
package com.yuanluesoft.jeaf.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 
 * @author linchuan
 *
 */
public class BufferUtils {
	
    /**
     * 获取字符
     * @param data
     * @param beginIndex
     * @return
     */
    public static char getChar(byte[] data, int[] beginIndex) {
        char value = (char)(getByte(data, beginIndex[0]) + (getByte(data, beginIndex[0]+1)<<8));
        beginIndex[0] += 2;
        return value;
    }
    
    /**
     * 获取字节
     * @param data
     * @param beginIndex
     * @return
     */
    public static byte getByte(byte[] data, int[] beginIndex) {
    	beginIndex[0]++;
        return data[beginIndex[0]-1];
    }
    
    /**
     * 获取整数
     * @param data
     * @param beginIndex
     * @return
     */
    public static int getInt(byte[] data, int[] beginIndex) {
        int intValue = getByte(data,  beginIndex[0] + 4 - 1);
        for(int i = beginIndex[0] + 4 - 2; i>=beginIndex[0]; i--) {
            intValue = (intValue<<8) + getByte(data, i);
        }
        beginIndex[0] += 4;
        return intValue;
    }
    
    /**
     * 获取长整数
     * @param data
     * @param beginIndex
     * @return
     */
    public static long getLong(byte[] data, int[] beginIndex) {
        long longValue = getByte(data, beginIndex[0] + 8 - 1);
        for(int i= beginIndex[0] + 8 - 2; i>=beginIndex[0]; i--) {
            longValue = (longValue<<8) + getByte(data, i);
        }
        beginIndex[0] += 8;
        return longValue;
    }
    
    /**
     * 获取字节值
     * @param data
     * @param index
     * @return
     */
    private static int getByte(byte[] data, int index) {
        return data[index]>=0 ? data[index] : 256 + data[index];
    }
    
    /**
     * 获取布尔值
     * @param data
     * @param index
     * @return
     */
    public static boolean getBoolean(byte[] data, int[] beginIndex) {
        boolean value = (data[beginIndex[0]]==1);
        beginIndex[0]++;
        return value;
    }
    
    /**
     * 获取字符串,以0x00结尾
     * @param data
     * @param beginIndex
     * @param dataLen
     * @return
     */
    public static String getString(byte[] data, int[] beginIndex, int dataLen) {
    	int i = beginIndex[0];
        for(; i<dataLen && data[i]!=0; i++);
        String value = new String(data, beginIndex[0], i - beginIndex[0]);
        beginIndex[0] = i + 1;
        return value;
    }
    
    /**
     * 获取字节数组
     * @param data
     * @param beginIndex
     * @param dataLen
     * @return
     */
    public static byte[] getBytes(byte[] data, int[] beginIndex, int dataLen) {
    	byte[] bytes = new byte[dataLen];
    	System.arraycopy(data, beginIndex[0], bytes, 0, dataLen);
    	beginIndex[0] += dataLen;
    	return bytes;
    }
    
    /**
     * 获取时间
     * @param data
     * @param beginIndex
     * @return
     */
    public static Timestamp getTime(byte[] data, int[] beginIndex) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set((int)getChar(data, beginIndex), (int)data[beginIndex[0]], (int)data[beginIndex[0] + 1], (int)data[beginIndex[0] + 2], (int)data[beginIndex[0] + 3], (int)data[beginIndex[0] + 4]);
    	beginIndex[0] += 5;
    	return new Timestamp(calendar.getTimeInMillis());
    }
    
    /**
     * 获取日期
     * @param data
     * @param beginIndex
     * @return
     */
    public static Date getDate(byte[] data, int[] beginIndex) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set((int)getChar(data, beginIndex), (int)data[beginIndex[0]] - 1, (int)data[beginIndex[0] + 1], 0, 0, 0);
    	beginIndex[0] += 2;
    	return new Date(calendar.getTimeInMillis());
    }
    
    /**
     * 填充字符串
     * @param charValue
     * @param buffer
     * @param beginIndex
     */
    public static int putChar(char charValue, byte[] buffer, int beginIndex) {
        buffer[beginIndex] = (byte)(charValue%256);
        buffer[beginIndex+1] = (byte)(charValue/256);
        return beginIndex + 2;
    }
    
    /**
     * 填充布尔值
     * @param boolValue
     * @param buffer
     * @param beginIndex
     */
    public static int putBoolean(boolean boolValue, byte[] buffer, int beginIndex) {
        buffer[beginIndex] = (byte)(boolValue ? 1 : 0);
        return beginIndex + 1;
    }
    
    /**
     * 填充整数
     * @param intValue
     * @param buffer
     * @param beginIndex
     */
    public static int putInt(int intValue, byte[] buffer, int beginIndex) {
        for(int i=0; i<4; i++) {
            buffer[beginIndex+i] = (byte)(intValue%256);
            intValue/=256;
        }
        return beginIndex + 4;
    }
    
    /**
     * 填充长整数
     * @param longValue
     * @param buffer
     * @param beginIndex
     */
    public static int putLong(long longValue, byte[] buffer, int beginIndex) {
        for(int i=0; i<8; i++) {
            buffer[beginIndex+i] = (byte)(longValue%256);
            longValue/=256;
        }
        return beginIndex + 8;
    }
    
    /**
     * 填充字符串
     * @param strValue
     * @param buffer
     * @param beginIndex
     */
    public static int putString(String strValue, byte[] buffer, int beginIndex) {
    	if(strValue==null) {
    		strValue = "";
    	}
        byte[] strBytes = strValue.getBytes();
        int len = strBytes.length;
        System.arraycopy(strBytes, 0, buffer, beginIndex, len);
        buffer[beginIndex + len] = 0;
        return beginIndex + len + 1;
    }
    
    /**
     * 填充字节
     * @param byteValue
     * @param buffer
     * @param beginIndex
     * @return
     */
    public static int putByte(byte byteValue, byte[] buffer, int beginIndex) {
    	buffer[beginIndex] = byteValue;
    	return beginIndex + 1;
    }
    
    /**
     * 填充字节数组
     * @param bytes
     * @param buffer
     * @param beginIndex
     * @return
     */
    public static int putBytes(byte[] bytes, byte[] buffer, int beginIndex) {
    	if(bytes==null) {
    		return beginIndex;
    	}
    	int len = bytes.length;
    	System.arraycopy(bytes, 0, buffer, beginIndex, len);
    	return beginIndex + len;
    }
    
    /**
     * 填充时间
     * @param time
     * @param buffer
     * @param beginIndex
     */
    public static int putTime(Timestamp time, byte[] buffer, int beginIndex) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(time.getTime());
    	putChar((char)calendar.get(Calendar.YEAR), buffer, beginIndex); //年
    	buffer[beginIndex + 2] = (byte)calendar.get(Calendar.MONTH); //月
    	buffer[beginIndex + 3] = (byte)calendar.get(Calendar.DAY_OF_MONTH); //日
    	buffer[beginIndex + 4] = (byte)calendar.get(Calendar.HOUR_OF_DAY); //时
    	buffer[beginIndex + 5] = (byte)calendar.get(Calendar.MINUTE); //分
    	buffer[beginIndex + 6] = (byte)calendar.get(Calendar.SECOND); //秒
    	return beginIndex + 7;
    }
    
    /**
     * 填充日期
     * @param time
     * @param buffer
     * @param beginIndex
     */
    public static int putDate(Date date, byte[] buffer, int beginIndex) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(date.getTime());
    	putChar((char)calendar.get(Calendar.YEAR), buffer, beginIndex); //年
    	buffer[beginIndex + 2] = (byte)(calendar.get(Calendar.MONTH) + 1); //月
    	buffer[beginIndex + 3] = (byte)calendar.get(Calendar.DAY_OF_MONTH); //日
    	return beginIndex + 4;
    }
}