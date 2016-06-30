package com.yuanluesoft.cms.infopublic.exchange;

import com.yuanluesoft.cms.infopublic.model.Info;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.exchange.client.api.ExchangeAPI;

/**
 * 政府信息交换客户端
 * @author linchuan
 *
 */
public class InfoExchangeAPI {

	/**
	 * 创建一个信息
	 * @param remoteHost 服务器IP
	 * @param remotePort 服务器端口
	 * @param receiverName 接收者名称,如:海事局网站内网服务器
	 * @param info 信息
	 * @throws Exception
	 */
	public static void createInfo(String remoteHost, int remotePort, String receiverName, Info info) throws Exception {
		//创建一个TCP数据交换客户端
		ExchangeClient exchangeClient = ExchangeAPI.createExchangeTcpClient(remoteHost, remotePort, receiverName);
		//发送文件
		info.setAttachmentFilePaths(exchangeClient.sendFiles(info.getAttachmentFilePaths(), null, true, false));
		info.setImageFilePaths(exchangeClient.sendFiles(info.getImageFilePaths(), null, true, false));
		info.setVideoFilePaths(exchangeClient.sendFiles(info.getVideoFilePaths(), null, true, false));
		//发送主记录
		exchangeClient.synchUpdate(info, null, 0);
	}
	
	/**
	 * 删除一个信息
	 * @param remoteHost
	 * @param remotePort
	 * @param receiverName
	 * @param infoId
	 * @throws Exception
	 */
	public static void deleteArticle(String remoteHost, int remotePort, String receiverName, String infoId) throws Exception {
		//创建一个TCP数据交换客户端
		ExchangeClient exchangeClient = ExchangeAPI.createExchangeTcpClient(remoteHost, remotePort, receiverName);
		//删除信息
		Info info = new Info();
		info.setInfoId(infoId);
		exchangeClient.synchDelete(info, null, 0);
	}
}