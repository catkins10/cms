package com.yuanluesoft.cms.publicservice.pojo;

import com.yuanluesoft.jeaf.database.LazyBody;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceBody extends LazyBody {
	
	/**
	 * 获取文本格式的内容
	 * @return
	 */
	public String getTextContent() {
		String body = getBody();
		if(body==null) {
			return null;
		}
		return StringUtils.filterHtmlElement(body, false).replaceAll("[ \\r\\n\\t]", "");
	}
}