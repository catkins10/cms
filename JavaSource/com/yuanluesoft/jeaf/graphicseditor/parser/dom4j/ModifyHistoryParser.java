/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.parser.dom4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.graphicseditor.model.extend.ModifyHistory;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyHistoryParser extends XmlParser {
	/**
	 * 解析修改历史
	 * @param xmlHisfory
	 * @return
	 * @throws ApplicationConfigureParseException
	 */
	public List parseModifyHistory(Element xmlHisfory) throws com.yuanluesoft.jeaf.exception.ParseException {
		if(xmlHisfory==null) {
			return null;
		}
		ArrayList list = new ArrayList();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(Iterator iterator=xmlHisfory.elementIterator(); iterator.hasNext();) {
			ModifyHistory history = new ModifyHistory();
			Element xmlElement = (Element)iterator.next();
			history.setModifierId(new Long(xmlElement.attributeValue("Id")).longValue());
			history.setUserName(xmlElement.getText());
			try {
				history.setModifyDate(formatter.parse(xmlElement.attributeValue("Date")));
			} catch (ParseException e) {
			}
			list.add(history);
		}
		return list;
	}
	/**
	 * 生成修改历史XML
	 * @throws ParseException
	 */
	public void generateModifyHistory(final Element xmlParent, final List modifyHistory) throws com.yuanluesoft.jeaf.exception.ParseException {
		if(modifyHistory==null || modifyHistory.isEmpty()) {
			return;
		}
		Element xmlElement = xmlParent.addElement("ModifyHistory");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(Iterator iterator=modifyHistory.iterator(); iterator.hasNext();) {
			ModifyHistory modify = (ModifyHistory)iterator.next();
			Element xmlModify = xmlElement.addElement("Modify");
			xmlModify.addAttribute("Id", "" + modify.getModifierId());
			xmlModify.addAttribute("Date", formatter.format(modify.getModifyDate()));
			xmlModify.setText(modify.getUserName());
		}
	}
}
