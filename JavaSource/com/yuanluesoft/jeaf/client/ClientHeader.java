package com.yuanluesoft.jeaf.client;

/**
 * 客户端HTTP头
 * @author chuan
 *
 */
public class ClientHeader {
	public final static String CLIENT_HEADER_REMOTE_ADDRESS = "Client-Address"; //客户端IP地址
	public final static String CLIENT_HEADER_REMOTE_PORT = "Client-Port"; //客户端端口
	public final static String CLIENT_HEADER_CACHE_EXPIRE = "Client-Cache-Expire"; //缓存有效期
	public final static String CLIENT_HEADER_TEMPLATE_PATH = "Client-Template-Path"; //模版路径
	public final static String CLIENT_HEADER_TEMPLATE_MODIFIED = "Client-Template-Modified"; //模版修改时间
	public final static String CLIENT_HEADER_STYLE_NAME = "Client-Style-Name"; //样式名称
	public final static String CLIENT_HEADER_STYLE_URL = "Client-Style-URL"; //样式URL
}