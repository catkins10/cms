package com.yuanluesoft.cms.siteresource.exchange;

import com.yuanluesoft.cms.siteresource.model.Article;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.exchange.client.api.ExchangeAPI;

/**
 * 文章交换客户端
 * @author linchuan
 *
 */
public class ArticleExchangeAPI {

	/**
	 * 创建一篇文章
	 * @param remoteHost 服务器IP
	 * @param remotePort 服务器端口
	 * @param receiverName 接收者名称,如:海事局网站内网服务器
	 * @param article 文章
	 * @throws Exception
	 */
	public static void createArticle(String remoteHost, int remotePort, String receiverName, Article article) throws Exception {
		//创建一个TCP数据交换客户端
		ExchangeClient exchangeClient = ExchangeAPI.createExchangeTcpClient(remoteHost, remotePort, receiverName);
		//发送文件
		article.setAttachmentFilePaths(exchangeClient.sendFiles(article.getAttachmentFilePaths(), null, true, false));
		article.setImageFilePaths(exchangeClient.sendFiles(article.getImageFilePaths(), null, true, false));
		article.setVideoFilePaths(exchangeClient.sendFiles(article.getVideoFilePaths(), null, true, false));
		//发送主记录
		exchangeClient.synchUpdate(article, null, 0);
	}
	
	/**
	 * 删除一篇文章
	 * @param remoteHost 服务器IP
	 * @param remotePort 服务器端口
	 * @param receiverName 接收者名称,如:海事局网站内网服务器
	 * @param articleId 文章ID
	 * @throws Exception
	 */
	public static void deleteArticle(String remoteHost, int remotePort, String receiverName, String articleId) throws Exception {
		//创建一个TCP数据交换客户端
		ExchangeClient exchangeClient = ExchangeAPI.createExchangeTcpClient(remoteHost, remotePort, receiverName);
		//删除文章
		Article article = new Article();
		article.setArticleId(articleId);
		exchangeClient.synchDelete(article, null, 0);
	}
}