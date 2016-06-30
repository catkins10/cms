package com.yuanluesoft.exchange.client.io;

import java.io.InputStream;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.ObjectSerializer;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangePacketReader {

	/**
	 * 读取数据
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static ExchangePacket readPacket(InputStream inputStream, String validateKey) throws Exception {
		byte[] data = readData(inputStream);
		//校验数据
		if(!ExchangePacketValidator.validate(data, readData(inputStream), validateKey)) {
			throw new Exception("ExchangePacketReader: validate error.");
		}
		//处理接收到的数据
		ExchangePacket packet = (ExchangePacket)ObjectSerializer.deserialize(data); //反序列化,TODO 判断是否XML格式data的前7个字节是"XMLDATA",如果是则解析XML得到对象
		if(Logger.isInfoEnabled()) {
			//Logger.info("ExchangePacketReader: receive a packet, class name is " + packet.getClass().getName() + ".");
		}
		return packet;
	}
	
	/**
	 * 读取数据
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readData(InputStream inputStream) throws Exception {
		int dataLength = 0; //数据长度
		for(int i=0; i<4; i++) {
			int byteValue = inputStream.read();
			if(byteValue==-1) {
				return null;
			}
			dataLength = dataLength*256 + byteValue;
		}
		if(dataLength<=0 || dataLength>5000000) { //小于0或者超过5M
			throw new Exception("received data length overflow, lenght is " + dataLength + ".");
		}
		byte[] data = new byte[dataLength];
		int total = 0;
		for(int readLen=inputStream.read(data, 0, dataLength); readLen!=-1 && total<dataLength; readLen=inputStream.read(data, total, dataLength-total)) {
			total += readLen;
		}
		if(total==dataLength) {
			return data;
		}
		throw new Exception("read failed");
	}
}