package com.yuanluesoft.microblog.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.microblog.model.MicroblogPlatformParameterDefine;

/**
 * 微博:帐号参数配置(microblog_account_parameter)
 * @author linchuan
 *
 */
public class MicroblogAccountParameter extends Record {
	private long accountId; //帐号配置ID
	private String parameterName; //参数名称
	private String parameterValue; //参数值
	
	//扩展属性
	private MicroblogPlatformParameterDefine parameterDefine; //参数定义
	
	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	/**
	 * @return the parameterValue
	 */
	public String getParameterValue() {
		return parameterValue;
	}
	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	/**
	 * @return the parameterDefine
	 */
	public MicroblogPlatformParameterDefine getParameterDefine() {
		return parameterDefine;
	}
	/**
	 * @param parameterDefine the parameterDefine to set
	 */
	public void setParameterDefine(MicroblogPlatformParameterDefine parameterDefine) {
		this.parameterDefine = parameterDefine;
	}
}