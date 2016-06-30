package com.yuanluesoft.jeaf.rss.writer.dom4j;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.rss.model.RSSChannel;
import com.yuanluesoft.jeaf.rss.model.RSSItem;
import com.yuanluesoft.jeaf.rss.writer.RSSWriter;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class RSSWriterImpl implements RSSWriter {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.rss.writer.RSSWriter#writeRSSChannel(com.yuanluesoft.jeaf.rss.model.RSSChannel, javax.servlet.http.HttpServletResponse)
	 */
	public void writeRSSChannel(RSSChannel channel, HttpServletResponse response) throws ServiceException {
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		try {
			writeRSSChannel(channel, response.getOutputStream());
		} 
		catch(IOException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 输出rss频道
	 * @param channel
	 * @param outputStream
	 * @throws ServiceException
	 */
	private void writeRSSChannel(RSSChannel channel, OutputStream out) throws ServiceException {
		Document rssDocument = createRssDocument(channel);
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(out, new OutputFormat("", true, "utf-8"));
			writer.write(rssDocument);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				writer.close();
			} 
			catch(Exception e) {
			
			}
		}
	}
	
	/**
	 * 创建rss文档
	 * @param channel
	 * @return
	 * @throws ServiceException
	 */
	private Document createRssDocument(RSSChannel channel) throws ServiceException {
		Document rssDocument = DocumentHelper.createDocument();
		rssDocument.addProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"" + Environment.getContextPath() + "/jeaf/rss/rss.xsl\"");
		
		//添加rss元素
		Element rssElement = rssDocument.addElement("rss");
		rssElement.addAttribute("version", "2.0");
		//添加channel元素
		Element channelElement = rssElement.addElement("channel");
		//输出channel属性
		channelElement.addElement("title").add(new DefaultCDATA(channel.getTitle())); //必需的。定义频道的标题。 
		channelElement.addElement("description").add(new DefaultCDATA(channel.getDescription())); //必需的。描述频道。 
		channelElement.addElement("link").setText(channel.getLink()); //必需的。定义指向频道的超链接。 
		if(channel.getPubDate()!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			channelElement.addElement("pubDate").setText(formatter.format(channel.getPubDate())); //可选的。为 feed 的内容定义最后发布日期。如: Sat, 07 Sep 2002 00:00:01 GMT" +
		}
		if(channel.getTtl()>0) {
			channelElement.addElement("ttl").setText(channel.getTtl() + ""); //可选的。指定从 feed 源更新此 feed 之前，feed 可被缓存的分钟数。 
		}
		if(channel.getGenerator()!=null) {
			channelElement.addElement("generator").setText(channel.getGenerator()); //可选的。规定用于生成 feed 的程序。 如: yuanluecms
		}
		if(channel.getImage()!=null) { //可选的。在聚合器呈现某个 feed 时，显示一个图像。
			Element imageElement = channelElement.addElement("image");
			imageElement.addElement("link").setText(channel.getImage().getLink()); //必需。定义提供该频道的网站的超连接。 
			imageElement.addElement("title").add(new DefaultCDATA(channel.getImage().getTitle())); //必需。定义当图片不能显示时所显示的替代文本。 
			imageElement.addElement("url").setText(channel.getImage().getUrl()); //必需。定义图像的 URL。 
			if(channel.getImage().getDescription()!=null) {
				imageElement.addElement("description").add(new DefaultCDATA(channel.getImage().getDescription())); //可选。规定图片链接的 HTML 标题属性中的文本。 
			}
			if(channel.getImage().getHeight()>0) {
				imageElement.addElement("height").setText(channel.getImage().getHeight() + ""); //可选。定义图像的高度。默认是 31。最大值是 400。 
			}
			if(channel.getImage().getWidth()>0) {
				imageElement.addElement("width").setText(channel.getImage().getWidth() + ""); //可选。定义图像的宽度。默认是 88。最大值是 144。 
			}
		}
		if(channel.getCloud()!=null) { //可选的。注册进程，以获得 feed 更新的立即通知。 如: <cloud domain="www.w3school.com.cn" port="80" path="/RPC" registerProcedure="NotifyMe" protocol="xml-rpc" />
			Element cloudElement = channelElement.addElement("cloud");
			cloudElement.addAttribute("domain", channel.getCloud().getDomain()); //loud程序所在机器的域名或IP地址   radio.xmlstoragesystem.com
			cloudElement.addAttribute("port", channel.getCloud().getPort() + ""); ; //访问clound程序所通过的端口   80 
			cloudElement.addAttribute("path", channel.getCloud().getPath()); ; //程序所在路径（不一定是真实路径）   /RPC2 
			cloudElement.addAttribute("egisterProcedure", channel.getCloud().getRegisterProcedure()); ; //注册的可提供的服务或过程   xmlStorageSystem.rssPleaseNotify  
			cloudElement.addAttribute("protocol", channel.getCloud().getProtocol()); ; //协议 xml-rpc, soap , http-post 之一   xml-rpc
		}  

		channelElement.addElement("language").setText(channel.getLanguage()==null ? "zh-cn" : channel.getLanguage()); //可选的。规定编写 feed 所用的语言。如: zh-cn 
		if(channel.getCategory()!=null) {
			channelElement.addElement("category").add(new DefaultCDATA(channel.getCategory())); //可选的。为 feed 定义所属的一个或多个种类
		}
		if(channel.getCopyright()!=null) {
			channelElement.addElement("copyright").add(new DefaultCDATA(channel.getCopyright())); //可选。告知版权资料
		}
		if(channel.getDocs()!=null) {
			channelElement.addElement("docs").add(new DefaultCDATA(channel.getDocs())); //可选的。规定指向当前 RSS 文件所用格式说明的 URL。 
		}
		if(channel.getLastBuildDate()!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			channelElement.addElement("lastBuildDate").setText(formatter.format(channel.getLastBuildDate())); //可选的。定义 feed 内容的最后修改日期。 如: Sat, 07 Sep 2002 00:00:01 GMT
		}
		if(channel.getManagingEditor()!=null) {
			channelElement.addElement("managingEditor").setText(channel.getManagingEditor()); //可选的。定义 feed 内容编辑的电子邮件地址。 
		}
		if(channel.getRating()!=null) {
			channelElement.addElement("rating").add(new DefaultCDATA(channel.getRating())); //可选的。频道分级（主要指成人、限制、儿童等）。 
		}
		if(channel.getSkipDays()>0) {
			channelElement.addElement("skipDays").setText(channel.getSkipDays() + ""); //可选的。规定忽略 feed 更新的天。 
		}
		if(channel.getSkipHours()>0) {
			channelElement.addElement("skipHours").setText(channel.getSkipHours() + ""); //可选的。规定忽略 feed 更新的小时。 
		}
		if(channel.getTextInput()!=null) { //可选的。规定应当与 feed 一同显示的文本输入域。
			Element textInputElement = channelElement.addElement("textInput");
			textInputElement.addElement("description").add(new DefaultCDATA(channel.getTextInput().getTitle())); //必需。定义对文本输入域的描述。 
			textInputElement.addElement("name").setText(channel.getTextInput().getLink()); //必需。定义在文本输入域中的文本对象的名称。 
			textInputElement.addElement("link").setText(channel.getTextInput().getLink()); //必需。定义处理文本输入的 CGI 脚本的 URL。 
			textInputElement.addElement("title").add(new DefaultCDATA(channel.getTextInput().getTitle())); //必需。定义文本输入域中的提交按钮的标注 (label)。 
		}
		if(channel.getWebMaster()!=null) {
			channelElement.addElement("webMaster").setText(channel.getWebMaster()); //可选的。定义此 feed 的 web 管理员的电子邮件地址。 
		}
		//输出条目列表
		if(channel.getItems()!=null) {
			for(Iterator iterator = channel.getItems().iterator(); iterator.hasNext();) {
				RSSItem item = (RSSItem)iterator.next();
				Element itemElement = channelElement.addElement("item");
				itemElement.addElement("title").add(new DefaultCDATA(item.getTitle())); //必需的。定义此项目的标题。 
				itemElement.addElement("link").setText(item.getLink()); //必需的。定义指向此项目的超链接。 
				itemElement.addElement("description").add(new DefaultCDATA(item.getDescription())); //必需的。描述此项目。  
				if(item.getPubDate()!=null) {
					SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
					itemElement.addElement("pubDate").setText(formatter.format(item.getPubDate())); //可选的。定义此项目的最后发布日期。 
				}
				if(item.getGuid()!=null) {
					itemElement.addElement("guid").setText(item.getGuid()); //可选的。为项目定义一个唯一的标识符。
				}
				if(item.getAuthor()!=null) {
					itemElement.addElement("author").setText(item.getAuthor()); //可选的。规定项目作者的电子邮件地址。 
				}
				if(item.getCategory()!=null) {
					itemElement.addElement("category").add(new DefaultCDATA(item.getCategory())); //可选的。定义项目所属的一个或多个类别。 
				}
				if(item.getComments()!=null) {
					itemElement.addElement("comments").add(new DefaultCDATA(item.getComments())); //可选的。允许项目连接到有关此项目的注释（文件）。 
				}
				if(item.getEnclosure()!=null) {
					itemElement.addElement("enclosure").add(new DefaultCDATA(item.getEnclosure())); //可选的。允许将一个媒体文件导入一个项中。 
				}
				if(item.getSource()!=null) {
					itemElement.addElement("source").add(new DefaultCDATA(item.getSource())); //可选的。为此项目指定一个第三方来源。
				}
			}
		}
		return rssDocument;
	}
}