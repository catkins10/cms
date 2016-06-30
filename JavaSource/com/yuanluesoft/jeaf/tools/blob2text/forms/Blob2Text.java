package com.yuanluesoft.jeaf.tools.blob2text.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Blob2Text extends ActionForm {
	private String dbmsType; //服务器类型,mysql/oracle
	private String serverIp;
	private String serverPort;
	private String dbName;
	private String userName;
	private String password;
	private String tableName;
	private String fromBlobField;
	private String toTextField;
	/**
	 * @return the dbmsType
	 */
	public String getDbmsType() {
		return dbmsType;
	}
	/**
	 * @param dbmsType the dbmsType to set
	 */
	public void setDbmsType(String dbmsType) {
		this.dbmsType = dbmsType;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}
	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	/**
	 * @return the serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the toTextField
	 */
	public String getToTextField() {
		return toTextField;
	}
	/**
	 * @param toTextField the toTextField to set
	 */
	public void setToTextField(String toTextField) {
		this.toTextField = toTextField;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the fromBlobField
	 */
	public String getFromBlobField() {
		return fromBlobField;
	}
	/**
	 * @param fromBlobField the fromBlobField to set
	 */
	public void setFromBlobField(String fromBlobField) {
		this.fromBlobField = fromBlobField;
	}
}
