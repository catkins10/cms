package com.yuanluesoft.jeaf.rss.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * rss频道
 * @author linchuan
 *
 */
public class RSSChannel {
	private String title; //必需的。定义频道的标题。 
	private String description; //必需的。描述频道。 
	private String link; //必需的。定义指向频道的超链接。 
	private Timestamp pubDate; //可选的。为 feed 的内容定义最后发布日期。如: Sat, 07 Sep 2002 00:00:01 GMT
	private int ttl; //可选的。指定从 feed 源更新此 feed 之前，feed 可被缓存的分钟数。 
	private List items; //条目列表
	
	private String generator; //可选的。规定用于生成 feed 的程序。 如: yuanluecms
	private String language; //可选的。规定编写 feed 所用的语言。如: zh-cn 
	private RSSImage image; //可选的。在聚合器呈现某个 feed 时，显示一个图像。 
	private String category; //可选的。为 feed 定义所属的一个或多个种类。 
	private RSSClound cloud; //可选的。注册进程，以获得 feed 更新的立即通知。 如: <cloud domain="www.w3school.com.cn" port="80" path="/RPC" registerProcedure="NotifyMe" protocol="xml-rpc" /> 
	private String copyright; //可选。告知版权资料。 
	private String docs; //可选的。规定指向当前 RSS 文件所用格式说明的 URL。 
	private Timestamp lastBuildDate; //可选的。定义 feed 内容的最后修改日期。 如: Sat, 07 Sep 2002 00:00:01 GMT
	private String managingEditor; //可选的。定义 feed 内容编辑的电子邮件地址。 
	private String rating; //可选的。频道分级（主要指成人、限制、儿童等）。 
	private int skipDays; //可选的。规定忽略 feed 更新的天。 
	private int skipHours; //可选的。规定忽略 feed 更新的小时。 
	private RSSTextInput textInput; //可选的。规定应当与 feed 一同显示的文本输入域。 
	private String webMaster; //可选的。定义此 feed 的 web 管理员的电子邮件地址。 
	
	/**
	 * 构造rss频道
	 * @param title 频道的标题
	 * @param description 描述频道
	 * @param link 指向频道的超链接
	 * @param pubDate 最后发布日期
	 * @param ttl 可被缓存的分钟数
	 * @param items 条目列表
	 */
	public RSSChannel(String title, String description, String link, Timestamp pubDate, int ttl, List items) {
		super();
		this.title = title;
		this.description = description;
		this.link = link==null ? "" : link;
		this.pubDate = pubDate;
		this.ttl = ttl;
		this.items = items;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}
	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the docs
	 */
	public String getDocs() {
		return docs;
	}
	/**
	 * @param docs the docs to set
	 */
	public void setDocs(String docs) {
		this.docs = docs;
	}
	/**
	 * @return the generator
	 */
	public String getGenerator() {
		return generator;
	}
	/**
	 * @param generator the generator to set
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	/**
	 * @return the image
	 */
	public RSSImage getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(RSSImage image) {
		this.image = image;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the lastBuildDate
	 */
	public Timestamp getLastBuildDate() {
		return lastBuildDate;
	}
	/**
	 * @param lastBuildDate the lastBuildDate to set
	 */
	public void setLastBuildDate(Timestamp lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the managingEditor
	 */
	public String getManagingEditor() {
		return managingEditor;
	}
	/**
	 * @param managingEditor the managingEditor to set
	 */
	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}
	/**
	 * @return the pubDate
	 */
	public Timestamp getPubDate() {
		return pubDate;
	}
	/**
	 * @param pubDate the pubDate to set
	 */
	public void setPubDate(Timestamp pubDate) {
		this.pubDate = pubDate;
	}
	/**
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	/**
	 * @return the skipDays
	 */
	public int getSkipDays() {
		return skipDays;
	}
	/**
	 * @param skipDays the skipDays to set
	 */
	public void setSkipDays(int skipDays) {
		this.skipDays = skipDays;
	}
	/**
	 * @return the skipHours
	 */
	public int getSkipHours() {
		return skipHours;
	}
	/**
	 * @param skipHours the skipHours to set
	 */
	public void setSkipHours(int skipHours) {
		this.skipHours = skipHours;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the ttl
	 */
	public int getTtl() {
		return ttl;
	}
	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	/**
	 * @return the webMaster
	 */
	public String getWebMaster() {
		return webMaster;
	}
	/**
	 * @param webMaster the webMaster to set
	 */
	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	/**
	 * @return the cloud
	 */
	public RSSClound getCloud() {
		return cloud;
	}

	/**
	 * @param cloud the cloud to set
	 */
	public void setCloud(RSSClound cloud) {
		this.cloud = cloud;
	}

	/**
	 * @return the items
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List items) {
		this.items = items;
	}

	/**
	 * @return the textInput
	 */
	public RSSTextInput getTextInput() {
		return textInput;
	}

	/**
	 * @param textInput the textInput to set
	 */
	public void setTextInput(RSSTextInput textInput) {
		this.textInput = textInput;
	}
}
