package com.yuanluesoft.cms.onlineservice.model;

/**
 * 公共服务类别
 * @author linchuan
 *
 */
public class PublicServiceType {
	private String type; //类别

	public PublicServiceType(String type) {
		super();
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}