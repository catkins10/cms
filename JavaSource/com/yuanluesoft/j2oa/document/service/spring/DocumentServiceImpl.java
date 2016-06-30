/*
 * Created on 2005-11-9
 *
 */
package com.yuanluesoft.j2oa.document.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.j2oa.document.pojo.DocumentOption;
import com.yuanluesoft.j2oa.document.service.DocumentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class DocumentServiceImpl extends BusinessServiceImpl implements DocumentService {
	private HTMLParser htmlParser; //HTML解析器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		DocumentOption option = (DocumentOption)record;
		option.setDocType(option.getDocType().replaceAll("，", ",").replaceAll(" ", "").replaceAll("　", ""));
		option.setPriority(option.getPriority().replaceAll("，", ",").replaceAll(" ", "").replaceAll("　", ""));
		option.setSecureLevel(option.getSecureLevel().replaceAll("，", ",").replaceAll(" ", "").replaceAll("　", ""));
		option.setSecureTerm(option.getSecureTerm().replaceAll("，", ",").replaceAll(" ", "").replaceAll("　", ""));
		if(option.getId()==0) { //新纪录
			option.setId(UUIDLongGenerator.generateId());
			return super.save(option);
		}
		else {
			return super.update(record);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.document.service.DocumentOptionService#getDocumentOption()
	 */
	public DocumentOption getDocumentOption() throws ServiceException {
		DocumentOption option = (DocumentOption)getDatabaseService().findRecordByHql("from DocumentOption DocumentOption");
		if(option==null) {
			option = new DocumentOption();
			option.setId(0);
			option.setSecureLevel("普通,秘密,机密,绝密"); //秘密等级
			option.setSecureTerm("无,三个月,半年,一年,二年,三年,五年"); //保密期限
			option.setPriority("普通,急,特急"); //紧急程度
			option.setDocType("命令,决定,公告,通告,通知,通报,议案,报告,请示,批复,意见,函,会议纪要"); //公文种类
		}
		return option;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		DocumentOption documentOption = getDocumentOption();
		if("secureLevel".equals(itemsName)) {
			return ListUtils.generateList(documentOption.getSecureLevel(), ",");
		}
		else if("secureTerm".equals(itemsName)) {
			return ListUtils.generateList(documentOption.getSecureTerm(), ",");
		}
		else if("priority".equals(itemsName)) {
			return ListUtils.generateList(documentOption.getPriority(), ",");
		}
		else if("docType".equals(itemsName) || "dataType".equals(itemsName)) {
			return ListUtils.generateList(documentOption.getDocType(), ",");
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.document.service.DocumentService#generateHandling(java.lang.String, com.yuanluesoft.jeaf.database.Record)
	 */
	public String generateHandling(String handlingTemplate, Record record) throws ServiceException {
		if(handlingTemplate==null || handlingTemplate.isEmpty()) {
			return null;
		}
		//解析办理单模板
		HTMLDocument htmlDocument = htmlParser.parseHTMLString(handlingTemplate, "utf-8");
		//获取字段列表
		List fields = FieldUtils.listRecordFields(record.getClass().getName(), null, "attachment", null, "hidden", false, true, false, false, 0);
		//处理字段列表
		NodeList nodes = htmlDocument.getElementsByTagName("a");
		for(int i=(nodes==null ? -1 : nodes.getLength()-1); i>=0; i--) {
			HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(i);
			if(!"field".equals(a.getId())) {
				continue;
			}
			try {
				String title = htmlParser.getTextContent(a);
				Field field = (Field)ListUtils.findObjectByProperty(fields, "title", title.substring(1, title.length()-1));
				//获取字段值,并格式化
				Object value = FieldUtils.getFieldValue(record, field.getName(), null);
				if(value!=null) {
					if(field.getTitle().endsWith("意见")) { //意见字段
						((HTMLElement)a.getParentNode()).setId("opinionArea");
					}
					value = FieldUtils.formatFieldValue(value, field, record, true, null, false, false, false, 0, "、", "", null);
					HTMLDocument valueDocument = htmlParser.parseHTMLString((String)value, "utf-8");
					htmlParser.importNodes(valueDocument.getBody().getChildNodes(), a, true);
				}
				a.getParentNode().removeChild(a);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		return htmlParser.getDocumentHTML(htmlDocument, "utf-8");
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}