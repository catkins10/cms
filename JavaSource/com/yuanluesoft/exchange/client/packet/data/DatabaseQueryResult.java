package com.yuanluesoft.exchange.client.packet.data;

import java.util.List;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseQueryResult extends ExchangePacket {
	private List records; //记录列表,按SQL查询时返回的是SqlResult列表

	public DatabaseQueryResult(List records) {
		super();
		this.records = records;
	}

	/**
	 * @return the records
	 */
	public List getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List records) {
		this.records = records;
	}
}