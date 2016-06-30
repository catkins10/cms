package com.yuanluesoft.jeaf.databackup.processor.impl;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.ProcessUtils;

/**
 * oracle数据备份,exp.exe方式导出
 * @author linchuan
 *
 */
public class OracleBackupProcessor implements DataBackupProcessor {
	private String exportCommandPath; //exp.exe所在路径,如果系统path里面有,允许为空
	private String userName; //oracle用户名
	private String password; //oracle用户密码
	private String tnsName; //oracle TNS名称, 必须事先建, 10G可以不建, 直接用IP
	private String owners; //需要备份的数据库隶属的用户,用逗号分隔,默认=userName
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor#backup(java.lang.String, java.sql.Date)
	 */
	public void backup(String savePath, Date incrementDate, String excludeFileTypes) throws ServiceException {
		List commands = new ArrayList();
		commands.add(exportCommandPath==null ? "exp" : exportCommandPath.replace("/", File.separator));
		//commands.add("kd9191edu/kd9191edu@192.168.0.10:1521/KDSOFT"); //10G支持
		commands.add(userName + "/" + password + "@" + tnsName);
		commands.add("file=" + savePath + "oracleExport.dmp");
		commands.add("owner=(" + (owners==null ? userName : owners) + ")");
		ProcessUtils.executeCommand(commands, null, null);
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
		try {
			this.password = Encoder.getInstance().desBase64Decode(password, "yu050718", null);
		} 
		catch (Exception e) {
			this.password = password;
		}
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
		try {
			this.userName = Encoder.getInstance().desBase64Decode(userName, "yu050718", null);
		} 
		catch (Exception e) {
			this.userName = userName;
		}
	}

	/**
	 * @return the exportCommandPath
	 */
	public String getExportCommandPath() {
		return exportCommandPath;
	}

	/**
	 * @param exportCommandPath the exportCommandPath to set
	 */
	public void setExportCommandPath(String exportCommandPath) {
		try {
			this.exportCommandPath = Encoder.getInstance().desBase64Decode(exportCommandPath, "yu050718", null);
		} 
		catch (Exception e) {
			this.exportCommandPath = exportCommandPath;
		}
	}

	/**
	 * @return the owners
	 */
	public String getOwners() {
		return owners;
	}

	/**
	 * @param owners the owners to set
	 */
	public void setOwners(String owners) {
		try {
			this.owners = Encoder.getInstance().desBase64Decode(owners, "yu050718", null);
		} 
		catch (Exception e) {
			this.owners = owners;
		}
	}

	/**
	 * @return the tnsName
	 */
	public String getTnsName() {
		return tnsName;
	}

	/**
	 * @param tnsName the tnsName to set
	 */
	public void setTnsName(String tnsName) {
		try {
			this.tnsName = Encoder.getInstance().desBase64Decode(tnsName, "yu050718", null);
		} 
		catch (Exception e) {
			this.tnsName = tnsName;
		}
	}
	

	public static void main(String[] args) throws Exception {
		OracleBackupProcessor processor = new OracleBackupProcessor();
		processor.setUserName("kd9191edu");
		processor.setPassword("kd9191edu");
		processor.setTnsName("kd9191edu");
		processor.setOwners("kd9191edu");
		processor.backup("d:/", null, null);
	}
}