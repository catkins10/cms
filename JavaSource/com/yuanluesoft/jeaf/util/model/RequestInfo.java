package com.yuanluesoft.jeaf.util.model;

/**
 * 客户端信息
 * @author linchuan
 *
 */
public class RequestInfo {
	//页面类型
	public final static int PAGE_TYPE_NORMAL = 0; //普通页面
	public final static int PAGE_TYPE_EDIT = 1; //页面编辑
	public final static int PAGE_TYPE_BUILD_STATIC_PAGE = 2; //静态页面生成请求
	public final static int PAGE_TYPE_CLIENT_DATA = 3; //客户端数据获取
	public final static int PAGE_TYPE_CLIENT_PAGE = 4; //客户端页面更新
	public final static int PAGE_TYPE_CLIENT_POST = 5; //客户端提交

	private boolean isWapRequest; //是否WAP请求
	private boolean isClientRequest; //是否客户端请求
	private boolean isWechatRequest; //是否微信请求
	private String terminalType; //终端类型,computer/android/iphone/ipad/symbian/client
	private int pageWidth; //终端类型为android/iphone/symbian时,需要显示的页面宽度,如果为-1,则表示用户尚未作出选择
	private int pageType; //页面类型,普通页面/静态页面生成请求/客户端数据获取/客户端提交/客户端页面更新
	private boolean flashSupport; //是否支持flash
	private boolean traditionalChinese; //是否繁体中文
	
	/**
	 * @return the flashSupport
	 */
	public boolean isFlashSupport() {
		return flashSupport;
	}
	/**
	 * @param flashSupport the flashSupport to set
	 */
	public void setFlashSupport(boolean flashSupport) {
		this.flashSupport = flashSupport;
	}
	/**
	 * @return the pageWidth
	 */
	public int getPageWidth() {
		return pageWidth;
	}
	/**
	 * @param pageWidth the pageWidth to set
	 */
	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}
	/**
	 * @return the terminalType
	 */
	public String getTerminalType() {
		return terminalType;
	}
	/**
	 * @param terminalType the terminalType to set
	 */
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	/**
	 * @return the isWapRequest
	 */
	public boolean isWapRequest() {
		return isWapRequest;
	}
	/**
	 * @param isWapRequest the isWapRequest to set
	 */
	public void setWapRequest(boolean isWapRequest) {
		this.isWapRequest = isWapRequest;
	}
	/**
	 * @return the traditionalChinese
	 */
	public boolean isTraditionalChinese() {
		return traditionalChinese;
	}
	/**
	 * @param traditionalChinese the traditionalChinese to set
	 */
	public void setTraditionalChinese(boolean traditionalChinese) {
		this.traditionalChinese = traditionalChinese;
	}
	/**
	 * @return the isClientRequest
	 */
	public boolean isClientRequest() {
		return isClientRequest;
	}
	/**
	 * @param isClientRequest the isClientRequest to set
	 */
	public void setClientRequest(boolean isClientRequest) {
		this.isClientRequest = isClientRequest;
	}
	/**
	 * @return the isWechatRequest
	 */
	public boolean isWechatRequest() {
		return isWechatRequest;
	}
	/**
	 * @param isWechatRequest the isWechatRequest to set
	 */
	public void setWechatRequest(boolean isWechatRequest) {
		this.isWechatRequest = isWechatRequest;
	}
	/**
	 * @return the pageType
	 */
	public int getPageType() {
		return pageType;
	}
	/**
	 * @param pageType the pageType to set
	 */
	public void setPageType(int pageType) {
		this.pageType = pageType;
	}
}