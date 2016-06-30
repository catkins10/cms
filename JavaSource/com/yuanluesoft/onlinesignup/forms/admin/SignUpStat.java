/*
 * Created on 2006-5-31
 *
 */
package com.yuanluesoft.onlinesignup.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 报名信息
 * @author zyh
 *
 */
public class SignUpStat extends ViewForm {
	private String province;//省
	private String city;//市
	private String country;//县
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	
	
	
}
