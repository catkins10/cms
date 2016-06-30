package com.yuanluesoft.exchange.client.packet.data;

import java.io.Serializable;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 发送对象命令的应答
 * @author linchuan
 *
 */
public class SendObjectAck extends ExchangePacket {
	private Serializable result;

	public SendObjectAck(Serializable result) {
		super();
		this.result = result;
	}

	/**
	 * @return the result
	 */
	public Serializable getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Serializable result) {
		this.result = result;
	}
}