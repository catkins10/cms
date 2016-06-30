package com.yuanluesoft.credit.enterprisecredit.item.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 龙海市安监局行政许可情况统计(credit_permit)
 * @author zyh
 *
 */
public class Permit extends ActionForm {
	private String permitNum ; //许可证号
	private String unit;//单位名称
	private String range;//许可经营范围
	private String overTime;//办结时间
	private String usefulDate;//证书有效期
	private String remark;//备注
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
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
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String getPermitNum() {
		return permitNum;
	}
	public void setPermitNum(String permitNum) {
		this.permitNum = permitNum;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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