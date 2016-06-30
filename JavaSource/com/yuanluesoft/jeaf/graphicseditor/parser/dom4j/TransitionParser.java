/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.graphicseditor.model.Transition;

/**
 * 
 * @author linchuan
 *
 */
public class TransitionParser extends XmlParser {
	/**
	 * 解析连接列表
	 * @param xmlSubForms
	 * @return
	 * @throws ResourceParseException
	 */
	public List parseTransitionList(Element xmlTransitions) throws ParseException {
		if(xmlTransitions==null) {
			return null;
		}
		ArrayList list = new ArrayList();
		for(Iterator iterator=xmlTransitions.elementIterator(); iterator.hasNext();) {
			list.add(parseTransition((Element)iterator.next()));
		}
		return list;
	}
	
	/**
	 * 解析连接
	 * @param xmlSubForm
	 * @return
	 * @throws ResourceParseException
	 */
	public Transition parseTransition(Element xmlTransition) throws ParseException {
		return parseTransition(new Transition(), xmlTransition);
	}
	/**
	 * 解析连接
	 * @param transition
	 * @param xmlTransition
	 * @return
	 * @throws ParseException
	 */
	public Transition parseTransition(Transition transition, Element xmlTransition) throws ParseException {
		parseElement(transition, xmlTransition);
		transition.setFromElementId(xmlTransition.attributeValue("From"));
		transition.setToElementId(xmlTransition.attributeValue("To"));
		return transition;
	}

	/**
	 * 生成连接列表XML
	 * @param xmlParent
	 * @param transitionList
	 * @throws ParseException
	 */
	public void generateTransitionListXML(final Element xmlParent, List transitionList) throws ParseException {
		if(transitionList==null || transitionList.isEmpty()) {
			return;
		}
		Element xmlTransitionList = xmlParent.addElement("Transitions");
		for(Iterator iterator=transitionList.iterator(); iterator.hasNext();) {
			generateTransitionXML(xmlTransitionList, (Transition)iterator.next());
		}
	}
	/**
	 * 生成连接XML
	 * @param xmlParent
	 * @param transition
	 * @throws ParseException
	 */
	public Element generateTransitionXML(final Element xmlParent, final Transition transition) throws ParseException {
		Element xmlTransition = xmlParent.addElement("Transition");
		generateElementXML(xmlTransition, transition);
		if(transition.getFromElement()!=null) {
			xmlTransition.addAttribute("From", transition.getFromElement().getId());
		}
		if(transition.getToElement()!=null) {
			xmlTransition.addAttribute("To", transition.getToElement().getId());
		}
		return xmlTransition;
	}
}
