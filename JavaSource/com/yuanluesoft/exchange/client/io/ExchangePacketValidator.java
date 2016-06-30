package com.yuanluesoft.exchange.client.io;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 数据交换验证
 * @author linchuan
 *
 */
public class ExchangePacketValidator {
	
	/**
	 * 验证数据有效性
	 * @param data
	 * @param validateCode
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean validate(byte[] data, byte[] validateCode, String key) throws Exception {
		if(validateCode==null) {
			Logger.info("ExchangePacketValidator: validate code failed.");
			return false;
		}
		byte[] code = generateValidateCode(data, key);
		if(validateCode.length!=code.length) {
			Logger.info("ExchangePacketValidator: validate error, length validate failed.");
			return false;
		}
		for(int i=0; i<validateCode.length; i++) {
			if(validateCode[i]!=code[i]) {
				Logger.info("ExchangePacketValidator: validate error, code validate failed.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 生成验证码
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] generateValidateCode(byte[] data, String key) throws Exception {
		//MD5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.reset();
		md5.update(data);
		byte[] result = md5.digest();
		//DES
		Cipher cipher = Cipher.getInstance("DES");
		//用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, generateDESKey(key));
		//加密
		result = cipher.doFinal(result);
		return result;
	}
	
	/**
	 * 生成DES密钥
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static SecretKey generateDESKey(String key) throws Exception {
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
}