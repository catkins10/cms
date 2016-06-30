package com.yuanluesoft.jeaf.databackup.processor.impl;

import java.sql.Date;

import com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * mysql数据备份,可以实现增量备份,不推荐,主要试用文件拷贝方式
 * @author linchuan
 *
 */
public class MySQLBackupProcessor implements DataBackupProcessor {
	private String serverIp; //服务器ID
	private long serverPort; //服务器端口
	private String userName; //数据库用户名
	private String password; //数据库密码
	private String characterSet; //字符集
	private String databaseNames; //需要备份的数据库列表,用逗号分隔
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor#backup(java.lang.String, java.sql.Date)
	 */
	public void backup(String savePath, Date incrementDate, String excludeFileTypes) throws ServiceException {
		/*mysqldump --default-character-set=gb2312 -h255.255.000.00 -uroot -pxxxxxx mydatabase>d:\data.sql

		其中-h后面是ip地址,-u后面是用户名,-p后面是密码,mydatabase是数据库名称,d:\data.sql是导出的数据文件,执行完毕后数据库就乖乖的备份到本地来了,利用计划任务甚至可以用来定时备份数据库.

		下面的命令是如何恢复数据,和上面类似:
		mysql --default-character-set=gb2312 -h255.255.000.00 -uroot -pxxxxxx mydatabase<d:\data.sql
		*/
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
	public long getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(long serverPort) {
		this.serverPort = serverPort;
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
	 * @return the databaseNames
	 */
	public String getDatabaseNames() {
		return databaseNames;
	}

	/**
	 * @param databaseNames the databaseNames to set
	 */
	public void setDatabaseNames(String databaseNames) {
		this.databaseNames = databaseNames;
	}

	/**
	 * @return the characterSet
	 */
	public String getCharacterSet() {
		return characterSet;
	}

	/**
	 * @param characterSet the characterSet to set
	 */
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
}
