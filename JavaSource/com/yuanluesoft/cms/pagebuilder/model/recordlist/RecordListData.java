package com.yuanluesoft.cms.pagebuilder.model.recordlist;

import java.util.List;

/**
 * 记录列表数据
 * @author linchuan
 *
 */
public class RecordListData {
	private List records; //记录
	private int recordCount; //记录数
	
	public RecordListData(List records, int recordCount) {
		super();
		this.records = records;
		this.recordCount = recordCount;
	}
	
	public RecordListData(List records) {
		super();
		this.records = records;
		this.recordCount = records==null ? 0 : records.size();
	}
	
	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
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