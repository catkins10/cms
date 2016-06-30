package com.yuanluesoft.jeaf.dialog.forms;


/**
 * 消息对话框
 * @author linchuan
 *
 */
public class MessageDialog extends SelectDialog {
	private String message; //消息
	private String type; //类型,info(默认)/warn/error
	private String buttonNames; //按钮列表
	private String script; //URL参数:选择后执行的脚本
	
	/**
	 * @return the buttonNames
	 */
	public String getButtonNames() {
		return buttonNames;
	}
	/**
	 * @param buttonNames the buttonNames to set
	 */
	public void setButtonNames(String buttonNames) {
		this.buttonNames = buttonNames;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
}