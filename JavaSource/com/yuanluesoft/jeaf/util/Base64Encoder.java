package com.yuanluesoft.jeaf.util;

import java.io.UnsupportedEncodingException;

/**
 * base64编码器
 * @author linchuan
 *
 */
public class Base64Encoder {

	/**
	 * base64编码
	 * @param base64Text
	 * @return
	 */
	public String encode(String text, String charset) {
		byte[] bytes;
		try {
			bytes = text.getBytes(charset);
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
		byte[] base64Bytes = new byte[(bytes.length + 2) / 3 * 4];
		int len = encode(bytes, 0, bytes.length, base64Bytes);
		return new String(base64Bytes, 0, len);
	}
	
	/**
	 * 对字节数组编码
	 * @param plainBytes
	 * @return
	 */
	public String encode(byte[] plainBytes) {
		byte[] base64Bytes = new byte[(plainBytes.length + 2) / 3 * 4];
		int len = encode(plainBytes, 0, plainBytes.length, base64Bytes);
		return new String(base64Bytes, 0, len);
	}
	
	/**
	 * base64编码,输出成字节数组
	 * @param plainBytes
	 * @param beginIndex
	 * @param endIndex
	 * @param base64Bytes, 长度必须大于明文的4/3倍
	 * @return
	 */
	public int encode(byte[] plainBytes, int beginIndex, int endIndex, byte[] base64Bytes) {
		int index = 0;
		int left = (endIndex - beginIndex) % 3;
		int end = endIndex - left;
		int[] ints = new int[3];
		for(int i=beginIndex; i<end; i+=3) {
			for(int j=0; j<3; j++) {
				ints[j] = plainBytes[i + j] >= 0 ? plainBytes[i + j] : 256 + plainBytes[i + j];
			}
			base64Bytes[index++] = (byte)convertBase64Int(ints[0] / 4); //字节1的高6位
			base64Bytes[index++] = (byte)convertBase64Int((ints[0] % 4 * 16) + ints[1] / 16); //字节1的低2位 + 字节2的高4位 
			base64Bytes[index++] = (byte)convertBase64Int((ints[1] % 16 * 4) + ints[2] / 64);  //字节2的低4位 + 字节3的高2位
			base64Bytes[index++] = (byte)convertBase64Int(ints[2] % 64);  //字节3的低6位
		}
		if(left > 0) {
			for(int i=end; i<endIndex; i++) {
				ints[i-end] = plainBytes[i] >= 0 ? plainBytes[i] : 256 + plainBytes[i];
			}
			base64Bytes[index++] = (byte)convertBase64Int(ints[0] / 4); //字节1的高6位
			if(end+1>=endIndex) {
				base64Bytes[index++] = (byte)convertBase64Int(ints[0] % 4 * 16);
			}
			else {
				base64Bytes[index++] = (byte)convertBase64Int(ints[0] % 4 * 16 + ints[1] / 16); //字节1的低2位 + 字节2的高4位
				base64Bytes[index++] = (byte)convertBase64Int(ints[1] % 16 * 4); //字节2的低4位 + 字节3的高2位
			}
			for(int i=3; i>left; i--) {
				base64Bytes[index++] = '=';
			}
		}
		return index;
	}
	
	/**
	 * 转换整数为base64字节
	 * @param base64Int
	 * @return
	 */
	private int convertBase64Int(int base64Int) {
		if(base64Int>=0 && base64Int<=25) {
			base64Int += 65;
		}
		else if(base64Int>=26 && base64Int<=51) {
			base64Int += 71;
		}
		else if(base64Int>=52 && base64Int<=61) {
			base64Int -= 4;
		}
		else if(base64Int==62) {
			base64Int = 43;
		}
		else if(base64Int==63) {
			base64Int = 47;
		}
		return base64Int;
	}
	
	/**
	 * base64编码,输出成字符数组
	 * @param plainBytes
	 * @param beginIndex
	 * @param endIndex
	 * @param base64Chars
	 * @return
	 */
	public int encode(byte[] plainBytes, int beginIndex, int endIndex, char[] base64Chars) {
		int index = 0;
		int left = (endIndex - beginIndex) % 3;
		int end = endIndex - left;
		int[] ints = new int[3];
		for(int i=beginIndex; i<end; i+=3) {
			for(int j=0; j<3; j++) {
				ints[j] = plainBytes[i + j] >= 0 ? plainBytes[i + j] : 256 + plainBytes[i + j];
			}
			base64Chars[index++] = (char)convertBase64Int(ints[0] / 4); //字节1的高6位
			base64Chars[index++] = (char)convertBase64Int((ints[0] % 4 * 16) + ints[1] / 16); //字节1的低2位 + 字节2的高4位 
			base64Chars[index++] = (char)convertBase64Int((ints[1] % 16 * 4) + ints[2] / 64);  //字节2的低4位 + 字节3的高2位
			base64Chars[index++] = (char)convertBase64Int(ints[2] % 64);  //字节3的低6位
		}
		if(left > 0) {
			for(int i=end; i<endIndex; i++) {
				ints[i-end] = plainBytes[i] >= 0 ? plainBytes[i] : 256 + plainBytes[i];
			}
			base64Chars[index++] = (char)convertBase64Int(ints[0] / 4); //字节1的高6位
			if(end+1>=endIndex) {
			    base64Chars[index++] = (char)convertBase64Int(ints[0] % 4 * 16);
			}
			else {
			    base64Chars[index++] = (char)convertBase64Int(ints[0] % 4 * 16 + ints[1] / 16); //字节1的低2位 + 字节2的高4位
			    base64Chars[index++] = (char)convertBase64Int(ints[1] % 16 * 4); //字节2的低4位 + 字节3的高2位
			}
			for(int i=3; i>left; i--) {
			    base64Chars[index++] = '=';
			}
		}
		return index;
	}
}