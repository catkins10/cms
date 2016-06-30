package com.yuanluesoft.jeaf.sms.client.jasson;

import java.sql.Timestamp;
import java.util.List;

import com.jasson.im.api.APIClient;
import com.jasson.im.api.RPTItem;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 嘉讯信息机
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	private String host = "172.18.46.134"; //信息机IP
	private String userName = "fzjtwww"; //信息机接口数据库用户名
	private String password = "fzjt123456"; //信息机接口数据库密码
	private String masSign = "【纪委】"; //签名
	private APIClient client = new APIClient();
	
	/**
	 * 启动
	 * @throws ServiceException
	 */
	public void startup() throws ServiceException {
		int connectResult = client.init(host, userName, password, "fzjtwww", "mas");
        if(connectResult == APIClient.IMAPI_SUCC) {
        	System.out.println("*******succ");
        	return; //初始化成功
        }
        if(connectResult == APIClient.IMAPI_CONN_ERR) {
        	throw new ServiceException("连接失败");
        }
        if(connectResult == APIClient.IMAPI_API_ERR) {
        	throw new ServiceException("apiID不存在");
        }
        if(connectResult != APIClient.IMAPI_SUCC) {
        	throw new ServiceException("出错");
        }
	}

	/**
	 * 卸载
	 * @throws ServiceException
	 */
	public void shutdown() throws ServiceException {
		try {
			client.release();
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 1000; //支持长短信
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

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
	    //支持Wap Push的链接地址
        //result = client.sendSM(mobiles, tmpContent, smId, Long.parseLong(tmpSrcID), url);
		long smId = 10086; //短信ID:0~99999999
		int result = client.sendSM(new String[]{(String)recevierNumbers.get(0)}, message, (receiveTime==null ? null : DateTimeUtils.formatTimestamp(receiveTime, null)), smId, 0l);
        if(result == APIClient.IMAPI_SUCC) { //发送成功            
            return smId + "";
        }
        if(result == APIClient.IMAPI_INIT_ERR || //未初始化
           result == APIClient.IMAPI_CONN_ERR) { //数据库连接失败
        	//重新初始化客户端
        	shutdown();
        	startup();
        	//重新发送
        	result = client.sendSM((String)recevierNumbers.get(0), message, smId);
        	 if(result == APIClient.IMAPI_SUCC) { //发送成功            
                 return smId + "";
             }
        }
        if(result == APIClient.IMAPI_DATA_ERR) { //参数错误
            throw new ServiceException("参数错误");
        }
        else if(result == APIClient.IMAPI_DATA_TOOLONG) { //消息内容太长
        	 throw new ServiceException("消息内容太长");
        }
        else if(result == APIClient.IMAPI_INS_ERR) { //数据库插入错误
        	 throw new ServiceException("数据库插入错误");
        }
        if(result == APIClient.IMAPI_INIT_ERR) { //未初始化
        	 throw new ServiceException("未初始化");
        }
        else if(result == APIClient.IMAPI_CONN_ERR) { //数据库连接失败
        	throw new ServiceException("数据库连接失败");
        }
        else { //出现其他错误
        	 throw new ServiceException("其他错误");
        }
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		return DateTimeUtils.now(); //暂时不实现
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		return null; //暂时不实现
	}

	/**
	 * 接收回执,定时调用
	 *
	 */
	public List receiveReturnReceipts() throws ServiceException {
		RPTItem[] rpts = client.receiveRPT();
		if(rpts == null) {
            throw new ServiceException("未初始化或接收失败");
        }
		for(int i=0; i<rpts.length; i++) {
            System.out.println("手机: " + rpts[i].getMobile() + ", " +
            				   "短信ID: " + rpts[i].getSmID() + ", " +
            				   "回执编码: " + rpts[i].getCode() + ", " +
            				   "回执描述: " + rpts[i].getDesc() + ", " +
            				   "回执时间: " + rpts[i].getRptTime());
		}
		return null;
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
		this.host = host;
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
