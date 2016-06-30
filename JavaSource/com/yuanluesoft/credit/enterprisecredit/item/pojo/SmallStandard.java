package com.yuanluesoft.credit.enterprisecredit.item.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 龙海市小、微型企业标准化网上申报通过名单(credit_smallstandard)
 * @author zyh
 *
 */
public class SmallStandard extends Record {
	private String unit ; //单位名称
	private String industry;//行业
	private String level;//所属乡镇
	private String bookNum;//证书编号
	private String startDate;//达标日期
	private String usefulDate;//有效期
	private String mainUnit;//行业主管单位
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMainUnit() {
		return mainUnit;
	}
	public void setMainUnit(String mainUnit) {
		this.mainUnit = mainUnit;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUsefulDate() {
		return usefulDate;
	}
	public void setUsefulDate(String usefulDate) {
		this.usefulDate = usefulDate;
	}

	
	
	
	
}