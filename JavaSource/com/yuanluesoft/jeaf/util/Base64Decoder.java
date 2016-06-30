package com.yuanluesoft.jeaf.util;


/**
 * base64解码
 * @author linchuan
 *
 */
public class Base64Decoder {

	/**
	 * base64解码
	 * @param base64Text
	 * @return
	 */
	public byte[] decode(String base64Text) {
		byte[] bytes = base64Text.getBytes();
		int len = decode(bytes);
		byte[] newBytes = new byte[len];
		System.arraycopy(bytes, 0, newBytes, 0, len);
		return newBytes;
	}
	
	/**
	 * base64解码
	 * @param base64Bytes
	 * @return 解码后长度
	 */
	public int decode(byte[] base64Bytes) {
		return decode(base64Bytes, 0, base64Bytes.length);
	}
	
	/**
	 * base64解码
	 * @param base64Bytes
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public int decode(byte[] base64Bytes, int beginIndex, int endIndex) {
		byte aByte;
		int index = beginIndex;
		int blankCount = 0;
		for(int i=beginIndex; i<endIndex; i+=4) {
			for(int j=0; j<4; j++) {
				aByte = base64Bytes[i+j];
				if(aByte>=65 && aByte<=90) { //A-Z
					aByte -= 65;
				}
				else if(aByte>=97 && aByte<=122) { //a-z
					aByte -= 71;
				}
				else if(aByte>=48 && aByte<=57) { //0-9
					aByte += 4;
				}
				else if(aByte=='+') {
					aByte = 62;
				}
				else if(aByte=='/') {
					aByte = 63;
				}
				else if(aByte=='=') {
					aByte = 0;
					blankCount++;
				}
				base64Bytes[i+j] = aByte;
			}
			base64Bytes[index++] = (byte)(base64Bytes[i] * 4 + base64Bytes[i + 1] / 16);
			base64Bytes[index++] = (byte)(base64Bytes[i + 1] * 16 + base64Bytes[i + 2] / 4);
			base64Bytes[index++] = (byte)(base64Bytes[i + 2] * 64 + base64Bytes[i + 3]);
		}
		return index - beginIndex - blankCount;
	}
	
	/**
	 * base64解码
	 * @param base64Bytes
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public byte[] decode(char[] base64Chars, int beginIndex, int endIndex) {
		byte aByte;
		int index = 0;
		int blankCount = 0;
		byte[] bytes = new byte[(endIndex-beginIndex)/4*3 + 4];
		for(int i=beginIndex; i<endIndex; i+=4) {
			for(int j=0; j<4; j++) {
				aByte = (byte)base64Chars[i+j];
				if(aByte>=65 && aByte<=90) { //A-Z
					aByte -= 65;
				}
				else if(aByte>=97 && aByte<=122) { //a-z
					aByte -= 71;
				}
				else if(aByte>=48 && aByte<=57) { //0-9
					aByte += 4;
				}
				else if(aByte=='+') {
					aByte = 62;
				}
				else if(aByte=='/') {
					aByte = 63;
				}
				else if(aByte=='=') {
					aByte = 0;
					blankCount++;
				}
				bytes[index++] = aByte;
			}
			index -= 4;
			bytes[index] = (byte)(bytes[index] *  4 + bytes[++index] / 16);
			bytes[index] = (byte)(bytes[index] * 16 + bytes[++index] / 4);
			bytes[index] = (byte)(bytes[index] * 64 + bytes[++index]);
		}
		int len = index - blankCount;
		byte[] newBytes = new byte[len];
		System.arraycopy(bytes, 0, newBytes, 0, len);
		return newBytes;
	}
}