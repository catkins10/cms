package com.yuanluesoft.jeaf.sms.client.zzbm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 漳州行政服务中心短信客户端
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	private String hostName = "59.59.58.247";
	private int port  = 1433;
	private String dbName = "t_sms";
	private String userName = "wsysp";
	private String password = "wsysp2026995";

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + hostName + ":" + port + ";DatabaseName=" + dbName, userName, password);
			String sql = "insert into t_sms" +
						  " (Sms_MtNumber,Sms_Case_No,Sms_Case_Applicant,Sms_Case_ServiceNO,Sms_SendTime,Sms_Text,Sms_SendFlag,operater_Id)" +
						  " values (?, ?, ?, ?, ?, ?, ?, ?)";
		    statement = connection.prepareStatement(sql);
		    statement.setString(1, (String)recevierNumbers.get(0)); //Sms_MtNumber
		    statement.setString(2, null); //Sms_Case_No
		    statement.setString(3, null); //Sms_Case_Applicant
		    statement.setInt(4, 0); //Sms_Case_ServiceNO
		    statement.setString(5, DateTimeUtils.formatTimestamp(receiveTime==null ? DateTimeUtils.now() : receiveTime, "yyyy-M-d HH:mm:ss")); //Sms_SendTime;
		    statement.setString(6, message); //Sms_Text
		    statement.setInt(7, 0); //Sms_SendFlag
		    statement.setInt(8, 0); //operater_Id
		    //statement.setInt(9, 0); //reply_flag
		    statement.executeUpdate() ;
		    return null;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
		    try {
				statement.close();
			} 
		    catch(Exception e) {
			
			}
		    try {
		    	connection.close();
			} 
		    catch(Exception e) {
			
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		return DateTimeUtils.now();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 200;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		return null;
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
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
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
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
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
}