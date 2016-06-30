package com.yuanluesoft.im.model;

import java.util.Set;

import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.service.IMService;

/**
 * 
 * @author linchuan
 *
 */
public class IMChat extends ChatDetail {
	private Set talks; //发言列表
	private String mailAddress; //邮件地址
	private String tel; //电话
	private String mobile; //手机
	private String department; //部门
	private byte status; //用户状态 
	private String webIM; //是否WEBIM在线, 是/否
	
	/**
	 * 获取用户状态说明
	 * @return
	 */
	public String getStatusText() {
		try {
			return IMService.IM_PERSON_STATUS_TEXTS[status - IMService.IM_PERSON_STATUS_OFFLINE];
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * @return the talks
	 */
	public Set getTalks() {
		return talks;
	}

	/**
	 * @param talks the talks to set
	 */
	public void setTalks(Set talks) {
		this.talks = talks;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the webIM
	 */
	public String getWebIM() {
		return webIM;
	}

	/**
	 * @param webIM the webIM to set
	 */
	public void setWebIM(String webIM) {
		this.webIM = webIM;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
}