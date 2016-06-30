package com.yuanluesoft.exchange.client.sender;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据交换连接
 * @author linchuan
 *
 */
public class ExchangeConnection {
	private InputStream inputStream; //输入流
	private OutputStream outputStream; //输出流
	
	public ExchangeConnection(InputStream inputStream, OutputStream outputStream) {
		super();
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	/**
	 * 关闭
	 */
	public void close() {
		try {
			inputStream.close();
		} 
		catch (Exception e) {
		
		}
		try {
			outputStream.close();
		} 
		catch (Exception e) {
		
		}
	}
	
	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	/**
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
}