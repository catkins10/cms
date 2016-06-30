package com.yuanluesoft.exchange.client.io;

import java.io.OutputStream;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.ObjectSerializer;

/**
 * 包输出
 * @author linchuan
 *
 */
public class ExchangePacketWriter {
	
	/**
	 * 发送对象
	 * @param exchangeSender
	 * @param command
	 * @throws Exception
	 */
	public static void writePacket(OutputStream outputStream, ExchangePacket packet, String validateKey) throws Exception {
		if(Logger.isDebugEnabled()) {
			//Logger.debug("ExchangePacketWriter: write a packet, class name is " + packet.getClass().getName() + ".");
		}
		//输出序列化后的对象
		byte[] bytes = ObjectSerializer.serialize(packet); //序列化
		int length = bytes.length;
		byte[] intValue = new byte[4];
		for(int i=3; i>=0; i--) {
			intValue[i] = (byte)(length%256);
			length = length/256;
		}
		outputStream.write(intValue, 0, 4);
		outputStream.write(bytes, 0, bytes.length);
		
		//发送校验码(DES+MD5)
		byte[] validateCode = ExchangePacketValidator.generateValidateCode(bytes, validateKey); //生成验证码
		length = validateCode.length;
		for(int i=3; i>=0; i--) {
			intValue[i] = (byte)(length%256);
			length = length/256;
		}
		outputStream.write(intValue, 0, 4);
		outputStream.write(validateCode, 0, validateCode.length);
	}
}