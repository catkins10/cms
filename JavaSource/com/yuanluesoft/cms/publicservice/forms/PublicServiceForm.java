package com.yuanluesoft.cms.publicservice.forms;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceForm extends PublicServiceAdminForm {
	//附加属性
	private String validateCode;

	/**
	 * 获取HTML格式的正文
	 * @return
	 */
	public String getHtmlContent() {
		String content = getContent();
		return content==null ? null : content.replaceAll(" ", "&nbsp;").replaceAll("\\r", "").replaceAll("\\n", "<br/>");
	}

	/**
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}

	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}
