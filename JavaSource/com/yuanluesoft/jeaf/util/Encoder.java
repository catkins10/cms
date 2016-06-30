/*
 * Created on 2004-11-5
 *
 */
package com.yuanluesoft.jeaf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 
 * @author linchuan
 * 
 */
public class Encoder {
	private static Encoder encode = new Encoder();
	
	public static Encoder getInstance() {
		return encode;
	}
    
    /**
	 * utf-8编码成js可识别的格式
	 * @param str
	 * @return
	 * @throws Exception
	 */
    public String utf8JsEncode(String str) throws Exception {
    	StringBuffer sb = new StringBuffer();
    	int k, j, len = str.length();
    	byte[] bytes;
    	char c;
    	for(int i=0; i<len; i++) {
    		c = str.charAt(i);
    		if (c >= 0 && c <= 127) {
    			sb.append(c);
    		}
    		else {
    			bytes = ("" + c).getBytes("UnicodeBig");
    			sb.append("\\u");
    			for(j=2; j<4 ; j++) {
    				k = bytes[j];
    				if(k<0) {
    					k += 256;
    				}
    				if(k<16) {
    					sb.append('0');
    				}
    				sb.append(Integer.toHexString(k).toUpperCase());
    			}
    		}
    	}
    	return sb.toString();
    }
    
    /**
     * 转换字节为16进制字符
     * @param b
     * @return
     */
    private String toHexString(byte b) {
        int intValue = (b>=0 ? b : 256+b);
        String hex = Integer.toHexString(intValue).toUpperCase();
        return hex.length()<2 ? "0" + hex : hex;
    }
    
    /**
     * 转换为16进制字符
     * @param bytes
     * @return
     */
    private String toHexString(byte[] bytes) {
    	String ret = "";
		for(int i=0; i<bytes.length; i++) {
			ret += toHexString(bytes[i]);
		}
		return ret;
    }
	
	/**
	 * MD5+BASE64编码
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String md5Base64Encode(String source) throws Exception {
		Base64Encoder base64 = new Base64Encoder();
		byte[] pwhash = md5Final(source);
		return new String(base64.encode(pwhash));
	}
	
	/**
	 * MD5编码
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String md5Encode(String source) throws Exception {
		return toHexString(md5Final(source));
	}
	
	/**
	 * MD5编码
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public byte[] md5Final(String source) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.reset();
		md5.update(source.getBytes());
		return md5.digest();
	}
	
	/**
	 * DES加密,输出为16进制数
	 * @param source
	 * @param key
	 * @param transformation AES/CBC/NoPadding (128)
	 *						 AES/CBC/PKCS5Padding (128)
	 *						 AES/ECB/NoPadding (128)
	 *						 AES/ECB/PKCS5Padding (128)
	 *						 DES/CBC/NoPadding (56)
	 *						 DES/CBC/PKCS5Padding (56)
	 *						 DES/ECB/NoPadding (56)
	 *						 DES/ECB/PKCS5Padding (56)
	 *						 DESede/CBC/NoPadding (168)
	 *						 DESede/CBC/PKCS5Padding (168)
	 *						 DESede/ECB/NoPadding (168)
	 *						 DESede/ECB/PKCS5Padding (168)
	 *						 RSA/ECB/PKCS1Padding (1024, 2048)
	 *						 RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
	 *						 RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
	 * @return
	 * @throws Exception
	 */
	public String desEncode(String source, String key, String charset, String transformation) throws Exception {
		if(charset.compareToIgnoreCase("unicode")==0) {
			charset = "UnicodeBig";
		}
		//Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(transformation==null ? "DES" : transformation);
		//用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, generateDESKey(key));
		//加密
		return toHexString(cipher.doFinal(resetDESSource(source.getBytes(charset), transformation)));
	}
	
	/**
	 * 重置DES源数据
	 * @param sourceBytes
	 * @param transformation
	 * @return
	 */
	private byte[] resetDESSource(byte[] sourceBytes, String transformation) {
		if(transformation==null || sourceBytes.length % 8==0 || !transformation.endsWith("NoPadding")) {
			return sourceBytes;
		}
		byte[] newSource = new byte[sourceBytes.length/8 * 8 + 8];
		System.arraycopy(sourceBytes, 0, newSource, 0, sourceBytes.length);
		return newSource;
	}
	
	/**
	 * DES编码
	 * @param source
	 * @param key
	 * @param charset
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public String desEncode(String source, byte[] key, String charset, String transformation) throws Exception {
		if(charset.compareToIgnoreCase("unicode")==0) {
			charset = "UnicodeBig";
		}
		//用密钥初始化Cipher对象
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		//Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(transformation==null ? "DES" : transformation);
		cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(dks));
		//加密
		return toHexString(cipher.doFinal(resetDESSource(source.getBytes(charset), transformation)));
	}
	
	/**
	 * DES解密，输入为16进制数
	 * @param encrypted
	 * @param key
	 * @param charset
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public String desDecode(String encrypted, String key, String charset, String transformation) throws Exception {
		if(charset.compareToIgnoreCase("unicode")==0) {
			charset = "UnicodeBig";
		}
		byte[] bytes = new byte[encrypted.length()/2];
		for(int i=0; i<bytes.length; i++) {
			bytes[i] = (byte)Integer.parseInt(encrypted.substring(i*2, i*2 + 2), 16);
		}
		Cipher cipher = Cipher.getInstance(transformation==null ? "DES" : transformation);
        cipher.init(Cipher.DECRYPT_MODE, generateDESKey(key));
        byte[] clearByte = cipher.doFinal(bytes);
        return (new String(clearByte, charset));
	}
	
	/**
	 * base64编码
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String base64Encode(String source) throws Exception {
		Base64Encoder base64 = new Base64Encoder();
	    return base64.encode(source.getBytes("utf-8")).replaceAll("[\\r\\n]", "");
	}
	
	/**
	 * base64解码
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String base64Decode(String source) throws Exception {
		Base64Decoder base64 = new Base64Decoder();
	    return new String(base64.decode(source), "utf-8");
	}
	
	/**
	 * DES+base64编码
	 * @param source
	 * @param key
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public String desBase64Encode(String source, String key, String transformation) throws Exception {
		//Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(transformation==null ? "DES" : transformation);
		//用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, generateDESKey(key));
		//加密
		byte[] cipherByte = cipher.doFinal(source.getBytes()); 
		Base64Encoder base64 = new Base64Encoder();
	    return base64.encode(cipherByte);
	}
	
	/**
	 * DES+base64解码
	 * @param encrypted
	 * @param key
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public String desBase64Decode(String encrypted, String key, String transformation) throws Exception {
        Base64Decoder base64 = new Base64Decoder();
        Cipher cipher = Cipher.getInstance(transformation==null ? "DES" : transformation);
        cipher.init(Cipher.DECRYPT_MODE, generateDESKey(key));
        byte[] clearByte = cipher.doFinal(base64.decode(encrypted));
        return (new String(clearByte));
    }
	
	/**
	 * 生成DES密钥
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private SecretKey generateDESKey(String key) throws Exception {
		int len = key.length();
		if(len<8) {
			key = "        ".substring(0, 8-len) + key;
		}
		else {
			key = key.substring(0, 8);
		}
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return keyFactory.generateSecret(dks);
    }
	
	/**
	 * 对象序列化,并转换为base64编码
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String objectBase64Encode(Serializable object) throws Exception {
		ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
    	ObjectOutputStream outputStream = new ObjectOutputStream(bytesStream);
    	outputStream.writeObject(object);
    	Base64Encoder base64 = new Base64Encoder();
    	return base64.encode(bytesStream.toByteArray());
	}
	
	/**
	 * 从base64字符串反序列化对象
	 * @param objectBase64Text
	 * @return
	 * @throws Exception
	 */
	public Object objectBase64Decode(String objectBase64Text) throws Exception {
		if(objectBase64Text==null || objectBase64Text.equals("")) {
			return null;
		}
		Base64Decoder base64 = new Base64Decoder();
		ByteArrayInputStream bytesStream = new ByteArrayInputStream(base64.decode(objectBase64Text));
    	ObjectInputStream inputStream = new ObjectInputStream(bytesStream);
    	return inputStream.readObject();
	}
	
	/**
	 * SHA加密
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String sha1(String source) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return toHexString(md.digest(source.getBytes()));
    }
}