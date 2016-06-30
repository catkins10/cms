package com.yuanluesoft.jeaf.util;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesSecurity {
	Cipher enCipher;
	Cipher deCipher;
	
	/**
	 * @param key 加密密钥
	 * @param iv 初始化向量
	 * @throws Exception
	 */
	public DesSecurity(String key, String iv) throws Exception {
		if (key == null) {
			throw new NullPointerException("Parameter is null!");
		}
		initCipher(key.getBytes(), iv.getBytes());
	}

	private void initCipher(byte[] secKey, byte[] secIv) throws Exception {
		//创建MD5散列对象
		MessageDigest md = MessageDigest.getInstance("MD5");
		//散列密钥
		md.update(secKey);
		//获得DES密钥
		DESKeySpec dks = new DESKeySpec(md.digest());
		//获得DES加密密钥工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
		//生成加密密钥
		SecretKey key = keyFactory.generateSecret(dks);
		//创建初始化向量对象
		IvParameterSpec iv = new IvParameterSpec(secIv);
		AlgorithmParameterSpec paramSpec = iv;
		//为加密算法指定填充方式，创建加密会话对象
		enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		//为加密算法指定填充方式，创建解密会话对象
		deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		//初始化加解密会话对象
		enCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec); 
		deCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}


	public String encrypt(String data) throws Exception {
		byte[] bytes = enCipher.doFinal(data.getBytes());
		String ret = "";
		for(int i=0; i<bytes.length; i++) {
			ret += toHexString(bytes[i]);
		}
		return ret;
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
	
	
	public static void main(String[] args) {
		try {
			DesSecurity des = new DesSecurity("fjfzztbw", "fjfzztbw");
			System.out.println(des.encrypt("123abc中华人民共和国"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
