/*
 * Created on 2006-3-9
 *
 */
package com.yuanluesoft.jeaf.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import net.sf.hibernate.id.UUIDHexGenerator;

/**
 * <b>uuid.string</b><br>
 * <br>
 * A <tt>UUIDGenerator</tt> that returns a string of length 16,
 * This string will NOT consist of only alphanumeric
 * characters. Use this only if you don't mind unreadable
 * identifiers.<br>
 * <br>
 * This implementation is known to be incompatible with
 * Postgres.
 *
 * @see UUIDHexGenerator
 * @author Gavin King
 */

public class UUIDStringGenerator {
	private static final String DEFAULT_ALGORITHM = "MD5";
	private String algorithm = DEFAULT_ALGORITHM;
	private Random random;
	private String randomClass;
	private String entropy;
	private MessageDigest digest;
	private static String previousUUID = null;
	private static UUIDStringGenerator generator = new UUIDStringGenerator();
		
	private synchronized Random getRandom() {
		if(random == null) {
			try {
				Class clazz = Class.forName(randomClass);
				random = (Random)clazz.newInstance();
				long seed = System.currentTimeMillis();
				char entropy[] = getEntropy().toCharArray();
				for(int i = 0; i < entropy.length; i++) {
					long update = (byte)entropy[i] << (i % 8) * 8;
					seed ^= update;
				}
				
				random.setSeed(seed);
			}
			catch(Exception e) {
				random = new Random();
			}
		}
		return random;
	}
	
	private synchronized MessageDigest getDigest() {
		if(digest == null) {
			try {
				digest = MessageDigest.getInstance(algorithm);
			}
			catch(NoSuchAlgorithmException e) {
				try {
					digest = MessageDigest.getInstance("MD5");
				}
				catch(NoSuchAlgorithmException f) {
					digest = null;
				}
			}
		}
		return digest;
	}
	
	private String getEntropy() {
		if(entropy == null) {
			setEntropy(toString());
		}
		return entropy;
	}
	
	private void setEntropy(String entropy) {
		this.entropy = entropy;
	}
	
	private synchronized String generate() {
		String uuid = null;
		for(;;) {
			byte bytes[] = new byte[16];
			getRandom().nextBytes(bytes);
			bytes = getDigest().digest(bytes);
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < bytes.length; i++)        {
				byte b1 = (byte)((bytes[i] & 0xf0) >> 4);
				byte b2 = (byte)(bytes[i] & 0xf);
				if(b1 < 10) {
					result.append((char)(48 + b1));
				}
				else {
					result.append((char)(65 + (b1 - 10)));
				}
				if(b2 < 10) {
					result.append((char)(48 + b2));
				}
				else {
					result.append((char)(65 + (b2 - 10)));
				}
			}
			uuid = result.toString();
			if(!uuid.equals(previousUUID)) {
				previousUUID = uuid;
				break;
			}
		}
		return uuid;
	}
	
	/**
	 * 生成64字节的ID
	 * @return
	 */
	public static String generateId() {
		return generator.generate() + generator.generate();
	}
}