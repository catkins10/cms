package com.yuanluesoft.cms.advert.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Advert extends ActionForm {
	private long siteId; //站点ID
	private long spaceId; //广告位ID
	private String spaceName; //广告位名称
	private long customerId; //广告客户ID
	private String customerName; //广告客户名称
	private String name; //名称
	private String content; //广告内容HTML
	private String minimizeContent; //最小化时HTML
	private String width; //宽度
	private String height; //高度
	private String minimizeWidth; //最小化时宽度
	private String minimizeHeight; //最小化时高度
	private String href; //链接
	private Timestamp created; //创建时间
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Set puts; //投放列表
	
	//扩展属性
	private String opener; //customer/space
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}
	/**
	 * @return the minimizeContent
	 */
	public String getMinimizeContent() {
		return minimizeContent;
	}
	/**
	 * @param minimizeContent the minimizeContent to set
	 */
	public void setMinimizeContent(String minimizeContent) {
		this.minimizeContent = minimizeContent;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the spaceId
	 */
	public long getSpaceId() {
		return spaceId;
	}
	/**
	 * @param spaceId the spaceId to set
	 */
	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return the puts
	 */
	public Set getPuts() {
		return puts;
	}
	/**
	 * @param puts the puts to set
	 */
	public void setPuts(Set puts) {
		this.puts = puts;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the spaceName
	 */
	public String getSpaceName() {
		return spaceName;
	}
	/**
	 * @param spaceName the spaceName to set
	 */
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	/**
	 * @return the opener
	 */
	public String getOpener() {
		return opener;
	}
	/**
	 * @param opener the opener to set
	 */
	public void setOpener(String opener) {
		this.opener = opener;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the minimizeHeight
	 */
	public String getMinimizeHeight() {
		return minimizeHeight;
	}
	/**
	 * @param minimizeHeight the minimizeHeight to set
	 */
	public void setMinimizeHeight(String minimizeHeight) {
		this.minimizeHeight = minimizeHeight;
	}
	/**
	 * @return the minimizeWidth
	 */
	public String getMinimizeWidth() {
		return minimizeWidth;
	}
	/**
	 * @param minimizeWidth the minimizeWidth to set
	 */
	public void setMinimizeWidth(String minimizeWidth) {
		this.minimizeWidth = minimizeWidth;
	}
}