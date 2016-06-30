package com.yuanluesoft.jeaf.sms.client.huawei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 短信客户端:华为信息机,使用JDBC方式
 * @author linchuan
 *
 */
public class SmsClientJdbcImpl extends SmsClient {
	private String jdbcUrl = "jdbc:db2://218.86.32.68:50110/MASDB"; //JDBC URL
	private String jdbcUserName = "db2inst1"; //JDBC用户名
	private String jdbcPassword = "Flzx3qC*"; //JDBC密码
	private String masSign = "【纪委】"; //签名
	
	//私有属性
	private Connection jdbcConnection; //JDBC连接

	/**
	 * 启动
	 * @throws Exception
	 */
	public void stratup() {
		if(jdbcConnection!=null) {
			showdown(); //注销
		}
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			jdbcConnection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 退出
	 * @throws Exception
	 */
	public void showdown() {
		try {
			jdbcConnection.close();
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		jdbcConnection = null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		Statement statement = null;
		try {
			if(jdbcConnection==null) {
				stratup(); //未连接时,重新启动
			}
			long messageId = UUIDLongGenerator.generateId();
			String sql = "insert into TBL_SMSENDTASK " +
						 "(CREATORID, TASKNAME, SMSENDEDNUM, OPERATIONTYPE, SUBOPERATIONTYPE, SENDTYPE, ORGADDR, DESTADDR, SM_CONTENT, SENDTIME, NEEDSTATEREPORT, SERVICEID, FEETYPE, FEECODE, MSGID, SMTYPE, MESSAGEID, DESTADDRTYPE, SUBTIME, TASKSTATUS, SENDLEVEL, SENDSTATE, TRYTIMES, COUNT, SUCCESSID, RESERVE1, RESERVE2, SISMSID, MSGFMT, SENDMETHODTYPE, APPLICATIONID, SOURCEFLAG)" +
			  			 "values (" +
			  			 "'0000'," + //CREATORID VARCHAR
			  			 "'" + messageId + "'," + //TASKNAME VARCHAR
			  			 "0," + //SMSENDEDNUM INTEGER
			  			 "'WAS'," + //OPERATIONTYPE VARCHAR
			  			 "'66'," + //SUBOPERATIONTYPE VARCHAR
			  			 "1," + //SENDTYPE SMALLINT
			  			 "'" + senderNumber + "'," + //ORGADDR VARCHAR
			  			 "'" + (String)recevierNumbers.get(0) + "'," + //DESTADDR VARCHAR
			  			 "'" + message.replaceAll("'", "") + "'," + //SM_CONTENT VARCHAR 67个汉字, 140个纯英文
			  			 "'" + (receiveTime==null ? "2005-07-18 13:41:29" : DateTimeUtils.formatTimestamp(receiveTime, null)) + "'," + //SENDTIME TIMESTAMP
			  			 "1," + //NEEDSTATEREPORT SMALLINT
			  			 "null," + //SERVICEID VARCHAR
			  			 "'02'," + //FEETYPE CHAR
			  			 "'0'," + //FEECODE VARCHAR
			  			 "null," + //MSGID VARCHAR
			  			 "0," + //SMTYPE SMALLINT
			  			 "'0'," + //MESSAGEID VARCHAR
			  			 "0," + //DESTADDRTYPE SMALLINT
			  			 "'2005-07-18 13:41:29'," + //SUBTIME TIMESTAMP 
			  			 "0," + //TASKSTATUS SMALLINT
			  			 "2," + //SENDLEVEL SMALLINT
			  			 "0," + //SENDSTATE SMALLINT
			  			 "0," + //TRYTIMES SMALLINT
			  			 "1," + //COUNT BIGINT
			  			 "0," + //SUCCESSID INTEGER
			  			 "null," + //RESERVE1 VARCHAR
			  			 "null," + //RESERVE2 VARCHAR
			  			 "null," + //SISMSID VARCHAR
			  			 "null," + //MSGFMT INTEGER
			  			 "0," + //SENDMETHODTYPE INTEGER
			  			 "null," + //APPLICATIONID VARCHAR
			  			 "0)"; //SOURCEFLAG INTEGER
			statement = jdbcConnection.createStatement();
			statement.executeUpdate(sql);
			return messageId + "";
		}
		catch(SQLException se) {
			showdown();
			throw new ServiceException(se);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if(jdbcConnection==null) {
				stratup(); //未连接时,重新启动
			}
			String sql = "select ID from TBL_SMSENDTASK where TASKNAME='" + JdbcUtils.resetQuot(messageId) + "'";
			statement = jdbcConnection.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()) { //任务记录还在
				return null;
			}
			return DateTimeUtils.now(); //smRecvTime:短信被手机终端收到的时间，有状态报告的时候生效，无状态报告的时候同短信入库时间
		}
		catch(SQLException se) {
			showdown();
			throw new ServiceException(se);
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception e) {
				
			}
			try {
				resultSet.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected synchronized ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if(jdbcConnection==null) {
				stratup(); //未连接时,重新启动
			}
			//TBL_SMRECEIVED 字段列表: SM_ID ORGADDR DESTADDR SM_CONTENT RECVTIME RESERVE1 RESERVE2 SMTYPE MESSAGEID ORGADDRTYPE ACTIONID ACTIONREASONID SERVICEID PROTOCOLTYPE READED DROPED
			//1  13600891862  1065730600124830001  TeST  2012-09-18 18:04:32.468  null  null  0    0  0  0  MFJ0010111  CMPP2.0  1  0
			String sql = "select SM_ID, ORGADDR, DESTADDR, SM_CONTENT, RECVTIME from TBL_SMRECEIVED where READED=0 order by RECVTIME";
			statement = jdbcConnection.createStatement();
			resultSet = statement.executeQuery(sql);
			if(!resultSet.next()) { //没有短信
				return null;
			}
			ReceivedShortMessage receivedShortMessage = new ReceivedShortMessage();
			receivedShortMessage.setSenderNumber(resultSet.getString("ORGADDR")); //发送人号码, 接收消息的源地址，对应的就是发送消息的移动台
			receivedShortMessage.setReceiverNumber(resultSet.getString("DESTADDR")); //接收人号码
			receivedShortMessage.setMessage(resultSet.getString("SM_CONTENT")); //短信内容
			receivedShortMessage.setReceiveTime(resultSet.getTimestamp("RECVTIME")); //接收时间
			//将短信设置为已读
			int messageId = resultSet.getInt("SM_ID");
			statement.close();
			statement = jdbcConnection.createStatement();
			sql = "update TBL_SMRECEIVED set READED=1 where SM_ID=" + messageId;
			statement.executeUpdate(sql);
			return receivedShortMessage;
		}
		catch(SQLException se) {
			showdown();
			throw new ServiceException(se);
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception e) {
				
			}
			try {
				resultSet.close();
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 200;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMessageMaxLengthForCharge(boolean)
	 */
	public int getMessageMaxLengthForCharge(boolean singleByteCharacters) {
		return 70 - masSign.length(); //66, 70-"【纪委】'.length, 内容字数计算规则：根据签名长度、长短信统计，如：内容字数<=64字按1条计算，字数<=128字按2条计算
		/*if(singleByteCharacters) { //单字节字符串
			return isPHS ? 116 : 140;
		}
		else {
			return isPHS ? 58 : 67; //MAS,67个汉字
		}*/
	}

	/**
	 * @return the jdbcPassword
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}

	/**
	 * @param jdbcPassword the jdbcPassword to set
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the jdbcUserName
	 */
	public String getJdbcUserName() {
		return jdbcUserName;
	}

	/**
	 * @param jdbcUserName the jdbcUserName to set
	 */
	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
	}

	/**
	 * @return the masSign
	 */
	public String getMasSign() {
		return masSign;
	}

	/**
	 * @param masSign the masSign to set
	 */
	public void setMasSign(String masSign) {
		this.masSign = masSign;
	}
}