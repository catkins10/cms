package com.yuanluesoft.jeaf.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 对象序列化
 * @author linchuan
 *
 */
public class ObjectSerializer {
	
    /**
     * 把对象序列化成字节
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Serializable obj) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        try {
            oos.writeObject(obj);
        }
        finally {
            oos.close();
        }
        return baos.toByteArray();
    }

    /**
     * 把字节缓存转换成对象
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BufferedInputStream bis = new BufferedInputStream(bais);
        ObjectInputStream ois = new ObjectInputStream(bis);
        try {
        	return (Serializable)ois.readObject();
        }
        finally {
        	ois.close();
        }
    }
    
    /**
     * 把对象写入文件
     * @param obj
     * @param filePath
     * @throws IOException
     */
    public static void serializeToFile(Serializable obj, String filePath) throws IOException {
    	FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		}
		finally {
			try {
				oos.close();
			}
			catch(Exception e) {
				
			}
			try {
				fos.close();
			}
			catch(Exception e) {
				
			}
		}
    }
    
    /**
     * 从文件中读取对象
     * @param filePath
     * @return
     * @throws Exception
     */
    public static Serializable deserializeFromFile(String filePath) throws Exception {
    	FileInputStream fis = null;
		ObjectInputStream ois =null;
		try{
			fis = new FileInputStream(filePath);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}
		finally {
			try {
				ois.close();
			}
			catch(Exception e) {
				
			}
			try {
				fis.close();
			}
			catch(Exception e) {
				
			}
		}
    }
}