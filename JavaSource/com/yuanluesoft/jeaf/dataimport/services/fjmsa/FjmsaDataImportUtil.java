package com.yuanluesoft.jeaf.dataimport.services.fjmsa;

import java.sql.ResultSet;

import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 福建海事局导入工具
 * @author lmiky
 *
 */
public class FjmsaDataImportUtil {

	/**
	 * 附件格式
	 * @param body
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static String attachmentFormat(String body, ResultSet rs) throws Exception {
		// 是否拥有附件
		boolean hasAttachment = false;
		String attachmentHtmlStr = "<div id=\"articleAttachmentBody\" style=\"margin-top:10px;\">"
									+ "<div align=\"left\">附件：</div>";
		while (rs.next()) {
			hasAttachment = true;
			String name = rs.getString("NAME");
			String description = rs.getString("DESCRIPTION");
			attachmentHtmlStr += "<div><a href=\"http://198.20.1.6/message/templet/download.jsp?id="
									+ rs.getString("ID")
									+ "&fileName="
									+ name.replaceAll("[ \r\n\t&\\?\\\\/]", "")
									+ "\">"
									+ (description == null || description.trim().isEmpty() ? name
									: description) + 
								 "</a></div>";
		}
		attachmentHtmlStr += 	"</div>";
		return (hasAttachment ? attachmentHtmlStr : null);
	}
	
	/**
	 * 图片格式
	 * @param imgURI
	 * @return
	 * @throws Exception
	 */
	public static String imgFormat(String imgURI) throws Exception {
		return	"<div id=\"articleImgBody\" align=\"center\" style=\"margin-top:5px; margin-bottom:5px;\" >" +
					"<img width='640' height='480' border='0' src=\"http://198.20.1.6" + imgURI + "\" />" +
				"</div>";

	}
	
	/**
	 * 内容格式
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static String resourceFormat(String body) throws Exception {
		return "<div id=\"articleResourceBody\" >" + body + "</div>";
	}
	
	/**
	 * 是否需要提取word内容
	 * @param body
	 * @return
	 */
	public static boolean isWordOnly(String body) {
		if(body==null || body.trim().isEmpty()) {
			return true;
		}
		body = StringUtils.filterHtmlElement(body, false).replaceAll("[\\r\\n 。]", "");
		return body.isEmpty() || "见附件".equals(body) || "请点击附件".equals(body) ||  "详见附件".equals(body) || "内容见附件".equals(body);
	}
}