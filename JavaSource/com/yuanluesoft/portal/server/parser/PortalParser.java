package com.yuanluesoft.portal.server.parser;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.portal.server.model.Portal;

/**
 * 
 * @author linchuan
 *
 */
public interface PortalParser {

	/**
	 * 解析PORTAL配置
	 * @param portalXml
	 * @return
	 */
	public Portal parsePortal(String portalXml) throws ParseException;
	
	/**
	 * 生成PORTAL XML
	 * @param portal
	 * @return
	 */
	public String generatePortalXml(Portal portal) throws ParseException;
}