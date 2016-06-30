package com.yuanluesoft.exchange.client.sender;

import com.yuanluesoft.exchange.client.exception.ExchangeException;


/**
 * 数据交换客户端:数据发送器
 * @author linchuan
 *
 */
public abstract class ExchangeDataSender {
	private String receiverNames; //接收方名称列表,用逗号分隔
	private boolean realTimeExchange = true; //是否实时交换
	//文件交换相关
	private boolean fileExchangeByCopy; //是否通过文件拷贝的方式来文件交换,如果部署了防篡改软件,设置为true
	private String pathsCopyDisabled; //禁止使用拷贝方式的目录列表(如:防篡改软件中被排除的目录),通过文件拷贝的方式来文件交换时有效
	private String fileExchangePath; //文件交换路径,通过文件拷贝的方式来文件交换时有效
	private int fileExchangeSpeed = 10000000; //交换速度,以字节/秒为单位,通过文件拷贝的方式来文件交换时有效,默认每秒10M字节
	private boolean reliableCopy = true; //是否可靠的拷贝,通过防篡改软件做数据交换时,设置为false

	/**
	 * 连接
	 *
	 */
	public abstract ExchangeConnection connect() throws ExchangeException;

	/**
	 * @return the receiverNames
	 */
	public String getReceiverNames() {
		return receiverNames;
	}

	/**
	 * @param receiverNames the receiverNames to set
	 */
	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}
	
	/**
	 * @return the fileExchangeByCopy
	 */
	public boolean isFileExchangeByCopy() {
		return fileExchangeByCopy;
	}

	/**
	 * @param fileExchangeByCopy the fileExchangeByCopy to set
	 */
	public void setFileExchangeByCopy(boolean fileExchangeByCopy) {
		this.fileExchangeByCopy = fileExchangeByCopy;
	}

	/**
	 * @return the fileExchangePath
	 */
	public String getFileExchangePath() {
		return fileExchangePath;
	}

	/**
	 * @param fileExchangePath the fileExchangePath to set
	 */
	public void setFileExchangePath(String fileExchangePath) {
		fileExchangePath = fileExchangePath.replaceAll("\\x5c", "/");
		if(!fileExchangePath.endsWith("/")) {
			fileExchangePath += "/";
		}
		this.fileExchangePath = fileExchangePath;
	}

	/**
	 * @return the fileExchangeSpeed
	 */
	public int getFileExchangeSpeed() {
		return fileExchangeSpeed;
	}

	/**
	 * @param fileExchangeSpeed the fileExchangeSpeed to set
	 */
	public void setFileExchangeSpeed(int fileExchangeSpeed) {
		this.fileExchangeSpeed = fileExchangeSpeed;
	}

	/**
	 * @return the reliableCopy
	 */
	public boolean isReliableCopy() {
		return reliableCopy;
	}

	/**
	 * @param reliableCopy the reliableCopy to set
	 */
	public void setReliableCopy(boolean reliableCopy) {
		this.reliableCopy = reliableCopy;
	}

	/**
	 * @return the realTimeExchange
	 */
	public boolean isRealTimeExchange() {
		return realTimeExchange;
	}

	/**
	 * @param realTimeExchange the realTimeExchange to set
	 */
	public void setRealTimeExchange(boolean realTimeExchange) {
		this.realTimeExchange = realTimeExchange;
	}

	/**
	 * @return the pathsCopyDisabled
	 */
	public String getPathsCopyDisabled() {
		return pathsCopyDisabled;
	}

	/**
	 * @param pathsCopyDisabled the pathsCopyDisabled to set
	 */
	public void setPathsCopyDisabled(String pathsCopyDisabled) {
		this.pathsCopyDisabled = pathsCopyDisabled;
	}
}