/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.base.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.model.user.Department;
import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.base.model.user.Role;
import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public class UserParser extends XmlParser {
	
	/**
	 * 解析人员列表
	 * @param xmlManager
	 * @return
	 * @throws ApplicationConfigureParseException
	 */
	public List parseUserList(Element xmlPersonList) throws ParseException {
		if(xmlPersonList==null) {
			return null;
		}
		ArrayList list = new ArrayList();
		for(Iterator iterator=xmlPersonList.elementIterator(); iterator.hasNext();) {
			Element xmlElement = (Element)iterator.next();
			String type = xmlElement.getName();
			if(type.equals("Person")) {
				list.add(parseElement(new Person(), xmlElement));
			}
			else if(type.equals("Department")) {
				list.add(parseElement(new Department(), xmlElement));
			}
			else if(type.equals("Role")) {
				list.add(parseElement(new Role(), xmlElement));
			}
		}
		return list;
	}
	

	/**
	 * 生成用户XML
	 * @param xmlParent
	 * @param userList
	 * @param type
	 * @throws ParseException
	 */
	public Element generateUserListXML(final Element xmlParent, final List userList, final String type) throws ParseException {
		if(userList==null || userList.isEmpty()) {
			return null;
		}
		Element xmlUserList = xmlParent.addElement(type);
		for(Iterator iterator=userList.iterator(); iterator.hasNext();) {
			com.yuanluesoft.jeaf.base.model.Element user = (com.yuanluesoft.jeaf.base.model.Element)iterator.next();
			String className = user.getClass().getName();
			generateElementXML(xmlUserList.addElement(className.substring(className.lastIndexOf(".")+1)), user);
		}
		return xmlUserList;
	}
}
