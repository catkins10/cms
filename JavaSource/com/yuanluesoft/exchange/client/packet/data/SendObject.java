package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 发送对象命令
 * @author linchuan
 *
 */
public class SendObject extends ExchangePacket {
	private Object object;

	public SendObject(Object object) {
		super();
		this.object = object;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
}