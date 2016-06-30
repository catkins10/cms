package com.yuanluesoft.jeaf.payment.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;

/**
 * 商户:参数(payment_merchant_parameter)
 * @author linchuan
 *
 */
public class PaymentMerchantParameter extends Record {
	private long merchantId; //商户ID
	private String parameterName; //参数名称
	private String parameterValue; //参数值
	
	//扩展属性,配置时使用
	private PaymentMethodParameterDefine parameterDefine; //参数定义
	
	public PaymentMerchantParameter() {
		super();
	}
	
	public PaymentMerchantParameter(String parameterName, String parameterValue) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}

	/**
	 * @return the merchantId
	 */
	public long getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
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
	public PaymentMethodParameterDefine getParameterDefine() {
		return parameterDefine;
	}
	/**
	 * @param parameterDefine the parameterDefine to set
	 */
	public void setParameterDefine(PaymentMethodParameterDefine parameterDefine) {
		this.parameterDefine = parameterDefine;
	}
}