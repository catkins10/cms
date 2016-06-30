package com.yuanluesoft.jeaf.databackup.processor.impl;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.ProcessUtils;

/**
 * postgresql数据备份,pg_dump.exe方式导出
 * @author linchuan
 *
 */
public class PostgresBackupProcessor implements DataBackupProcessor {
	private String dumpCommandPath; //pg_dump.exe所在路径,如果系统path里面有,允许为空
	private String userName; //用户名
	private String password; //密码
	private String host; //数据库服务器主机名
	private String port; //数据库服务器端口
	private String dbName; //数据库名称
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.databackup.processor.DataBackupProcessor#backup(java.lang.String, java.sql.Date)
	 */
	public void backup(String savePath, Date incrementDate, String excludeFileTypes) throws ServiceException {
		//备份 pg_dump -U postgres -h localhost -p 5432 fjmsacms > d:\pg_backup.dat
		//恢复 psql -U postgres -h localhost -p 5432 fjmsacms < d:\pg_backup.dat
		//pgadmin1.10(postgres自带)使用的备份: D:/PostgreSQL/8.4/bin\pg_dump.exe --host localhost --port 5432 --username postgres --format custom --blobs --verbose --file "D:\backup\pg_2011-11-05(1).backup" zzcms_bak
		//pgadmin1.8使用的备份: D:\PostgreSQL\8.4\bin\pg_dump.exe -h localhost -p 5432 -U postgres -F c -b -v -f "C:\Users\linchuan\Desktop\pg_jy.backup" test
		Map environmentParameters = new HashMap();
		environmentParameters.put("PGPASSWORD", password);
		List commands = new ArrayList();
		commands.add(dumpCommandPath==null ? "pg_dump" : dumpCommandPath.replace("/", File.separator));
		/*commands.add("-U" + userName);
		commands.add("-h" + host);
		commands.add("-p" + port);
		commands.add("-w");
		commands.add("-f" + savePath + "pg.backup");
		commands.add(dbName);*/
		commands.add("--host");
		commands.add(host);
		commands.add("--port");
		commands.add("" + port);
		commands.add("--username");
		commands.add(userName);
		commands.add("--format");
		commands.add("custom");
		commands.add("--blobs");
		commands.add("--verbose");
		commands.add("--file");
		commands.add("\"" + savePath + "pg.backup\"");
		commands.add(dbName);
		ProcessUtils.executeCommand(commands, environmentParameters, null);
	}
	
	public static void main(String[] args) throws Exception {
		PostgresBackupProcessor processor = new PostgresBackupProcessor();
		processor.setDumpCommandPath("D:/PostgreSQL/8.4/bin/pg_dump.exe");
		processor.setUserName("postgres");
		processor.setPassword("root");
		processor.setHost("localhost");
		processor.setPort("5432");
		processor.setDbName("test");
		processor.backup("d:/", null, null);
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
		try {
			this.dbName = Encoder.getInstance().desBase64Decode(dbName, "yu050718", null);
		} 
		catch (Exception e) {
			this.dbName = dbName;
		}
	}

	/**
	 * @return the dumpCommandPath
	 */
	public String getDumpCommandPath() {
		return dumpCommandPath;
	}

	/**
	 * @param dumpCommandPath the dumpCommandPath to set
	 */
	public void setDumpCommandPath(String dumpCommandPath) {
		try {
			this.dumpCommandPath = Encoder.getInstance().desBase64Decode(dumpCommandPath, "yu050718", null);
		} 
		catch (Exception e) {
			this.dumpCommandPath = dumpCommandPath;
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		try {
			this.host = Encoder.getInstance().desBase64Decode(host, "yu050718", null);
		} 
		catch (Exception e) {
			this.host = host;
		}
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
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		try {
			this.port = Encoder.getInstance().desBase64Decode(port, "yu050718", null);
		} 
		catch (Exception e) {
			this.port = port;
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
}