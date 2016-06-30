/*
 * Created on 2005-5-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.util;

import java.io.UnsupportedEncodingException;

import com.yuanluesoft.jeaf.util.Base64Decoder;

/**
 * @author root
 *
 */
public class Decoder {
	private static Decoder decoder = new Decoder();
	public static Decoder getInstance() {
		return decoder;
	}
	/**
	 * 解析属性值
	 * @param propertyValue
	 * @return
	 */
	public String decodePropertyValue(String propertyValue) {
		String out = "";
		int prevIndex = 0, nextIndex;
		Base64Decoder base64Encoder = new Base64Decoder();
		while((nextIndex = propertyValue.indexOf("=?", prevIndex))!=-1) {
			if(prevIndex!=nextIndex && (prevIndex+1>nextIndex || propertyValue.charAt(prevIndex)!=' ')) {
				out += propertyValue.substring(prevIndex, nextIndex);
			}
			prevIndex = nextIndex + 2;
			nextIndex = propertyValue.indexOf('?', prevIndex);
			String charset = propertyValue.substring(prevIndex, nextIndex);
			char encodeMode = propertyValue.charAt(nextIndex + 1);
			prevIndex = nextIndex + 3;
			nextIndex = propertyValue.indexOf("?=", prevIndex);
			if(encodeMode=='Q' || encodeMode=='q') { //QP
				out += qpDecode(propertyValue.substring(prevIndex, nextIndex), charset);
			}
			else if(encodeMode=='B' || encodeMode=='b') { //BASE64
				try {
					out += new String(base64Encoder.decode(propertyValue.substring(prevIndex, nextIndex)), charset);
				}
				catch (UnsupportedEncodingException e) {
					throw new Error(e.getMessage());
				}
			}
			prevIndex = nextIndex + 2;
		}
		out += propertyValue.substring(prevIndex);
		return out;
	}
	
	/**
	 * Quoted-Printable解码
	 * @param qpText
	 * @return
	 */
	public String qpDecode(String qpText, String charset) {
	    int len = qpText.length();
		byte[] buffer = new byte[len];
		int index = 0;
		for(int i=0; i<len; i++) {
			if(qpText.charAt(i)=='=') {
				char first = qpText.charAt(i + 1);
				if(first=='\n') {
				    i++;
				}
				else {
					char second = qpText.charAt(i + 2);
					if(first!='\r' || second!='\n') {
					    first -= (first>=65 ? 55 : 48);
					    second -= (second>=65 ? 55 : 48);
					    buffer[index++] = (byte)(first*16 + second);
					}
					i+=2;
				}
			}
			else {
				buffer[index++] = (byte)qpText.charAt(i);
			}
		}
		try {
			return new String(buffer, 0, index, charset);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}