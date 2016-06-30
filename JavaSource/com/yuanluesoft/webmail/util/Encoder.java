/*
 * Created on 2005-6-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.util;

import com.yuanluesoft.jeaf.util.Base64Encoder;

/**
 * 
 * @author root
 * 
 */
public class Encoder {
	private static Encoder encoder = new Encoder();
	public static Encoder getInstance() {
		return encoder;
	}
	/**
	 * 属性值编码
	 * @param propertyValue
	 * @param charset
	 * @return
	 */
	public String encodePropertyValue(String propertyValue, String charset) {
		String base64Value = "";
		int len = propertyValue.length();
		if(len==0) {
			return "";
		}
		int prevPosition = 0;
		boolean checkLongChar = true;
		int valueChar;
		Base64Encoder base64Encoder = new Base64Encoder();
		for(int i=0; i<len; i++) {
			valueChar = (int)propertyValue.charAt(i);
			if(checkLongChar) {
				if(valueChar>255) {
					checkLongChar = false;
					base64Value += propertyValue.substring(prevPosition, i);
					prevPosition = i;
				}
			}
			else {
				if(valueChar<=255) {
					checkLongChar = true;
					base64Value += "=?" + charset + "?B?" + base64Encoder.encode(propertyValue.substring(prevPosition, i), charset) + "?=";
					prevPosition = i;
				}
			}
		}
		return base64Value + (checkLongChar ? propertyValue.substring(prevPosition) : "=?" + charset + "?B?" + base64Encoder.encode(propertyValue.substring(prevPosition), charset) + "?=");
	}
}
