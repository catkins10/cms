package com.yuanluesoft.jeaf.wap.spring;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTextAreaElement;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.cyberneko.CyberNekoHTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.wap.WapPageService;

/**
 * 
 * @author linchuan
 *
 */
public class WapPageServiceImpl implements WapPageService {
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.wap.WapPageService#writeWapPage(java.lang.String, java.io.Writer)
	 */
	public void writeWapPage(String htmlPage, Writer writer) throws ServiceException {
		XMLWriter xmlWriter = null;
		try {
			//解析HTML
			int skip = htmlPage.indexOf('<');
			HTMLDocument htmlDocument = htmlParser.parseHTMLString(skip>0 ? htmlPage.substring(skip) : htmlPage, "utf-8"); //htmlParser.parseHTMLFile("c:/WML 参考手册.htm"); //
			//创建WAP文档
			Document wapDocument = createWapDocument(htmlDocument);
			//输出
			xmlWriter = new XMLWriter(writer, new OutputFormat("", true, "utf-8"));
			xmlWriter.write(wapDocument);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				xmlWriter.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 创建WAP文档
	 * @param htmlDocument
	 * @return
	 * @throws ServiceException
	 */
	private Document createWapDocument(HTMLDocument htmlDocument) throws ServiceException {
		Document wapDocument = DocumentHelper.createDocument();
		wapDocument.setDocType(new DefaultDocumentType("wml", "-//WAPFORUM//DTD WML 1.1//EN", "http://www.wapforum.org/DTD/wml_1.1.xml"));
		Element wmlElement = wapDocument.addElement("wml"); //创建wml元素
		//创建card
		Element cardElement = wmlElement.addElement("card");
		String title = htmlDocument.getTitle();
		cardElement.addAttribute("title", (title==null ? "" : title));
		//创建p
		Element paragraphElement = cardElement.addElement("p");
		//输出html body
		writeHTMLNodes(htmlDocument.getBody(), paragraphElement, new ArrayList());
		return wapDocument;
	}
	
	/**
	 * 输出HTML子元素列表
	 * @param parentHtmlNode
	 * @param parentWapElement
	 * @throws ServiceException
	 */
	private void writeHTMLNodes(Node parentHtmlNode, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		NodeList nodes = parentHtmlNode.getChildNodes();
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getUserData("processed")!=null) {
				continue;
			}
			try {
				writeHTMLNode(node, parentWapElement, parentWapElementNames);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 输出HTML元素
	 * @param node
	 * @param parentWapElement
	 * @param parentWapElementTagNames 所有的父元素名称列表
	 * @throws ServiceException
	 */
	private void writeHTMLNode(Node node, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		String parentWapElementName = parentWapElement.getName();
		if(parentWapElementNames.indexOf(parentWapElementName)==-1) {
			parentWapElementNames = new ArrayList(parentWapElementNames);
			parentWapElementNames.add(parentWapElementName);
		}
		if(node instanceof Text) { //文本
			String content = ((Text)node).getTextContent().trim();
			if(!content.equals("")) { //WAP不支持验证码和附件
				if(!content.startsWith("验证码") && !content.startsWith("输入验证码") && !content.startsWith("附件")) {
					parentWapElement.addText(content);
				}
				else { //删除最后一个换行
					removeBlankLine(parentWapElement);
				}
			}
			return;
		}
		String tagName = node.getNodeName().toLowerCase();
		if(tagName.equals("br")) { //换行
			parentWapElement.addElement("br");
			return;
		}
		if(tagName.equals("b") ||
		   tagName.equals("strong") ||
		   tagName.equals("i") ||
		   tagName.equals("em") ||
		   tagName.equals("small") ||
		   tagName.equals("big") ||
		   tagName.equals("u")) { //文本格式化标签
			writeHTMLNodes(node, parentWapElement.addElement(tagName), parentWapElementNames);
			return;
		}
		if(tagName.compareTo("h1")>=0 && tagName.compareTo("h9")<=0) { //h1,h2...
			writeHTMLNodes(node, parentWapElement.addElement("b"), parentWapElementNames);
			return;
		}
		if(tagName.equals("p") || tagName.equals("pre") || tagName.equals("div") || tagName.equals("ul")) { //P/PRE/DIV/UL
			parentWapElement.addElement("br"); //不支持UL,直接换行
			writeHTMLNodes(node, parentWapElement, parentWapElementNames); //输出下一级
			return;
		}
		if(tagName.equals("li")) { //LI
			writeHTMLNodes(node, parentWapElement, parentWapElementNames); //输出下一级
			parentWapElement.addElement("br"); //添加一个换行
			return;
		}
		if(tagName.equals("img")) { //图片
			processImageElement((HTMLImageElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("a")) { //链接
			processAnchorElement((HTMLAnchorElement)node, parentWapElement, parentWapElementNames); 
			return;
		}
		if(tagName.equals("table")) { //表格
			processTableElement((HTMLTableElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("tr")) { //表格行
			processTableRowElement((HTMLTableRowElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("td") || tagName.equals("th")) { //表格列
			processTableCellElement((HTMLTableCellElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("input")) { //输入框
			processInputElement((HTMLInputElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("textarea")) { //多行文本输入框
			processTextAreaElement((HTMLTextAreaElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("button")) { //按钮
			processButtonElement((HTMLButtonElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("span")) { //SPAN
			processSpanElement((HTMLElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		if(tagName.equals("script")) { //脚本
			processScriptElement((HTMLScriptElement)node, parentWapElement, parentWapElementNames);
			return;
		}
		//<span onclick="FormUtils.submitForm()"
		if(tagName.equals("iframe") ||
		   tagName.equals("embed") ||
		   tagName.equals("applet") ||
		   tagName.equals("style")) { //直接跳过
			return;
		}
		//不支持的HTML元素,直接输出下一级
		writeHTMLNodes(node, parentWapElement, parentWapElementNames);
	}
	
	/**
	 * 删除最后一个换行
	 * @param parentWapElement
	 */
	private void removeBlankLine(Element parentWapElement) {
		List elements = parentWapElement.elements();
		if(elements==null || elements.size()==0) {
			return;
		}
		Element lastElement = (Element)elements.get(elements.size() - 1);
		if("br".equalsIgnoreCase(lastElement.getName())) {
			parentWapElement.remove(lastElement);
		}
	}
	
	/**
	 * 处理SPAN元素
	 * @param span
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processSpanElement(HTMLElement span, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		String id = span.getId();
		if("imageButton".equals(id)) { //图片按钮 
			String onclick = span.getAttribute("onclick");
			processJavaScript(onclick, span, parentWapElement, span.getTitle(), parentWapElementNames);
		}
		else { //其他
			writeHTMLNodes(span, parentWapElement, parentWapElementNames);
		}
	}
	
	/**
	 * 处理INPUT元素
	 * @param input
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processInputElement(HTMLInputElement input, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		String type = input.getType();
		type = (type==null ? "text" : type.trim());
		if("text".equals(type) || "password".equals(type)) { //文本输入框/密码输入框
			appendInputElement(parentWapElement, input, type, input.getValue());
		}
		else if("radio".equals(type) || "checkbox".equals(type)) { //单选框/多选框
			List optionValues = new ArrayList();
			List optionTitles = new ArrayList();
			String name = input.getName();
			String value = input.getValue();
			NodeList boxes = ((HTMLDocument)input.getOwnerDocument()).getElementsByName(name);
			for(int i=0; i<boxes.getLength(); i++) {
				input = (HTMLInputElement)boxes.item(i);
				String display = StringUtils.getStyle(input.getAttribute("style"), "display");
				if(display!=null && display.equalsIgnoreCase("none")) {
					continue;
				}
				if(input.getChecked()) {
					value = input.getValue();
				}
				input.setUserData("processed", "true", null);
				optionValues.add(input.getValue());
				Node node = input;
				String title = "";
				while((node=node.getNextSibling())!=null) {
					if(node.getNodeType()!=Node.TEXT_NODE && !"label".equalsIgnoreCase(node.getNodeName())) { //不是文本,也不是LABEL,不处理
						break;
					}
					title += htmlParser.getTextContent(node).trim();
					node.setUserData("processed", "true", null);
				}
				optionTitles.add(title);
			}
			appendSelectElement(parentWapElement, name, value, optionValues, optionTitles);
		}
		else if("button".equals(type)) { //按钮
			String onclick = input.getAttribute("onclick");
			processJavaScript(onclick, input, parentWapElement, input.getAttribute("value"), parentWapElementNames);
		}
		else if("submit".equals(type)) { //提交
			processJavaScript("FormUtils.submitForm()", input, parentWapElement, input.getAttribute("value"), parentWapElementNames);
		}
	}
	
	/**
	 * 处理多行文本输入框
	 * @param textArea
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processTextAreaElement(HTMLTextAreaElement textArea, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		Element inputElement = parentWapElement.addElement("input");
		parentWapElement.addText("　");
		inputElement.addAttribute("name", textArea.getName());
		inputElement.addAttribute("type", "text");
		String value = textArea.getValue();
		if(value==null || value.equals("")) {
			value = htmlParser.getTextContent(textArea);
		}
		if(value!=null && !value.equals("")) {
			inputElement.addAttribute("value", value);
		}
		String styleClass = textArea.getAttribute("class");
		inputElement.addAttribute("emptyok", styleClass==null || styleClass.toLowerCase().indexOf("required")==-1 ? "true" : "false");
	}
	
	/**
	 * 处理A元素
	 * @param a
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processAnchorElement(HTMLAnchorElement a, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		if(parentWapElementNames.indexOf("a")!=-1) { //父元素已经是a,不允许嵌套
			writeHTMLNodes(a, parentWapElement, parentWapElementNames);
			return;
		}
		String href = a.getHref();
		String js = a.getAttribute("onclick");
		if((js==null || js.equals("")) && (href!=null && href.startsWith("javascript:"))) {
			js = href.substring("javascript:".length());
		}
		if(js!=null && !js.equals("")) { //有脚本
			processJavaScript(js, a, parentWapElement, null, parentWapElementNames);
			return;
		}
		if(href==null || href.equals("") || href.equals("#")) { //没有链接
			return;
		}
		Element anchorElement = parentWapElement.addElement("a");
		anchorElement.addAttribute("href", href);
		writeHTMLNodes(a, anchorElement, parentWapElementNames);
	}
	
	/**
	 * 处理表格
	 * @param table
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processTableElement(HTMLTableElement table, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		if(parentWapElementNames.indexOf("table")!=-1 || //父元素已经包括表格,不允许嵌套,或者表格
		   table.getElementsByTagName("input").getLength()>0) { //表格中包含"input"
			writeHTMLNodes(table, parentWapElement, parentWapElementNames);
			parentWapElement.addElement("br"); //不支持表格,换行
			return;
		}
		if(table.getRows()==null || table.getRows().getLength()==0) {
			return;
		}
		int columns = ((HTMLTableRowElement)table.getRows().item(0)).getCells().getLength();
		if(columns==0) {
			return;
		}
		Element tableElement = parentWapElement.addElement("table");
		tableElement.addAttribute("columns", "" + columns);
		writeHTMLNodes(table, tableElement, parentWapElementNames);
	}
	
	/**
	 * 处理表格行
	 * @param tr
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processTableRowElement(HTMLTableRowElement tr, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		if(parentWapElementNames.indexOf("tr")!=-1 || parentWapElementNames.indexOf("table")==-1) { //父元素已经包括表格,不允许嵌套
			//parentWapElement.addElement("br"); //不支持表格,换行
			writeHTMLNodes(tr, parentWapElement, parentWapElementNames);
		}
		else {
			writeHTMLNodes(tr, parentWapElement.addElement("tr"), parentWapElementNames);
		}
	}
	
	/**
	 * 处理表格单元格
	 * @param td
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processTableCellElement(HTMLTableCellElement td, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		if(parentWapElementNames.indexOf("td")!=-1 || parentWapElementNames.indexOf("tr")==-1) { //父元素已经包括表格,不允许嵌套
			if(td.getCellIndex()%2==0) {
				parentWapElement.addElement("br"); //不支持表格,换行
			}
			else {
				parentWapElement.addText(" "); //不支持表格,添加空格
			}
			writeHTMLNodes(td, parentWapElement, parentWapElementNames);
		}
		else {
			writeHTMLNodes(td, parentWapElement.addElement("td"), parentWapElementNames);
		}
	}
	
	/**
	 * 处理图片
	 * @param img
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processImageElement(HTMLImageElement img, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		if("validateCodeImage".equals(img.getId())) { //WAP不支持验证码
			removeBlankLine(parentWapElement); //删除最后一个空行
			return;
		}
		Element imgElement = parentWapElement.addElement("img");
		imgElement.addAttribute("src", img.getSrc());
		String alt = img.getAlt();
		imgElement.addAttribute("alt", (alt==null ? "" : alt));
	}
	
	/**
	 * 处理脚本
	 * @param script
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processScriptElement(HTMLScriptElement script, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		//检查是否html编辑器
		String js = htmlParser.getTextContent(script);
		if(js==null) {
			return;
		}
		String fieldName = null;
		String value = null;
		int index = js.indexOf("new HtmlEditor("); //new HtmlEditor(fieldName, height, fullPage, autoIndentation, plugins, commands, attachmentSelectorURL, parentElement)
		if(index!=-1) { //html编辑器
			int len = "new HtmlEditor(".length();
			fieldName = js.substring(len + 1, js.indexOf((js.charAt(len + 1)=='\"' ? "\"" : "'"), len + 1));
			
			//获取隐藏字段
			HTMLInputElement hidden = (HTMLInputElement)((HTMLDocument)script.getOwnerDocument()).getElementsByName(fieldName).item(0);
			value = hidden.getValue();
			hidden.setAttribute("type", "text"); //为避免表单重复提交，修改为text
		}
		else if(js.startsWith("new DropdownField(")) { //下拉
			//选项列表
			List optionValues = new ArrayList();
			List optionTitles = new ArrayList();
			String[] values = js.split("\', \'");
			fieldName = values[3]; //字段名称
			values = values[2].split("\\\\0");
			for(int i=0; i<values.length; i++) {
				String[] option = values[i].split("\\x7c");
				optionValues.add(option[option.length-1].trim());
				optionTitles.add(option[0].trim());
			}
			
			//获取隐藏字段
			HTMLInputElement hidden = (HTMLInputElement)((HTMLDocument)script.getOwnerDocument()).getElementsByName(fieldName).item(0);
			value = hidden.getValue();
			hidden.setAttribute("type", "text"); //为避免表单重复提交，修改为text

			//插入选择元素
			appendSelectElement(parentWapElement, fieldName, value, optionValues, optionTitles);
			return;
		}
		else if(js.startsWith("new TextField(") || js.startsWith("new TextAreaField(") || js.startsWith("new DateField(") || js.startsWith("new TimeField") || js.startsWith("new DateTimeField") || js.startsWith("new SelectField(")) {
			//获取字段名称
			index = js.indexOf("name=\"");
			if(index==-1) {
				return;
			}
			index += "name=\"".length();
			fieldName = js.substring(index, js.indexOf("\"", index));
			//获取字段值
			index = js.indexOf("value=\"");
			if(index!=-1) {
				index += "value=\"".length();
				value = js.substring(index, js.indexOf("\"", index));
			}
			if(value==null || value.equals("")) { //没有值,为避免输入错误,设置为当前时间
				if(js.startsWith("new DateField(")) {
					value = DateTimeUtils.formatDate(DateTimeUtils.date(), null);
				}
				else if(js.startsWith("new DateTimeField(")) {
					value = DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null);
				}
				else if(js.startsWith("new TimeField(")) {
					value = "08:00";
				}
				else if(js.startsWith("new DayField(")) {
					value = "01-01";
				}
			}
			//把script替换成input元素，使得wap表单提交时能够提交
			HTMLInputElement input = (HTMLInputElement)((HTMLDocument)script.getOwnerDocument()).createElement("input");
			input.setAttribute("type", "text");
			input.setName(fieldName);
			script.getParentNode().replaceChild(input, script);
		}
		if(fieldName==null) {
			return;
		}
		Element inputElement = parentWapElement.addElement("input");
		parentWapElement.addText("　");
		inputElement.addAttribute("name", fieldName);
		inputElement.addAttribute("type", "text");
		if(value!=null && !value.equals("")) {
			inputElement.addAttribute("value", StringUtils.filterHtmlElement(value, false));
		}
}
	
	/**
	 * 处理按钮
	 * @param button
	 * @param parentWapElement
	 * @param parentWapElementNames
	 * @throws ServiceException
	 */
	private void processButtonElement(HTMLButtonElement button, Element parentWapElement, List parentWapElementNames) throws ServiceException {
		String onclick = button.getAttribute("onclick");
		processJavaScript(onclick, button, parentWapElement, htmlParser.getTextContent(button), parentWapElementNames);
	}
	
	/**
	 * 添加input元素
	 * @param parentWapElement
	 * @param input
	 * @param type
	 */
	private void appendInputElement(Element parentWapElement, HTMLInputElement input, String type, String value) {
		String name = input.getName().replace('/', '_');
		if(name.equals("validateCode")) { //WAP不支持验证码
			//删除最后一个换行
			removeBlankLine(parentWapElement);
			return;
		}
		Element inputElement = parentWapElement.addElement("input");
		parentWapElement.addText("　");
		inputElement.addAttribute("name", name);
		inputElement.addAttribute("type", type);
		if(value!=null && !value.equals("")) {
			inputElement.addAttribute("value", value);
		}
		int maxLength = input.getMaxLength();
		if(maxLength>0) {
			inputElement.addAttribute("maxlength", "" + maxLength);
		}
		int tabIndex = input.getTabIndex();
		if(tabIndex>0) {
			inputElement.addAttribute("tabindex", "" + tabIndex);
		}
		String title = input.getTitle();
		if(title!=null && !title.equals("")) {
			inputElement.addAttribute("title", title);
		}
		String size = input.getSize();
		if(size!=null && !size.equals("")) {
			inputElement.addAttribute("size", size);
		}
		String styleClass = input.getAttribute("class");
		inputElement.addAttribute("emptyok", styleClass==null || styleClass.toLowerCase().indexOf("required")==-1 ? "true" : "false");
	}
	
	/**
	 * 添加列表选择输入框
	 * @param parentWapElement
	 * @param name
	 * @param value
	 * @param optionValues
	 * @param optionTitles
	 */
	private void appendSelectElement(Element parentWapElement, String name, String value, List optionValues, List optionTitles) {
		if(optionTitles==null || optionTitles.isEmpty()) {
			return;
		}
		Element selectElement = parentWapElement.addElement("select");
		parentWapElement.addText("　");
		selectElement.addAttribute("name", name);
		if(value!=null && !value.equals("")) {
			selectElement.addAttribute("value", value);
		}
		//添加选项
		for(int i=0; i<optionValues.size(); i++) {
			Element optionElement = selectElement.addElement("option");
			optionElement.addAttribute("value", "" + optionValues.get(i));
			optionElement.setText("" + optionTitles.get(i));
		}
	}
	
	/**
	 * 处理脚本
	 * @param js
	 * @param htmlElement
	 * @param parentWapElement
	 * @param buttonName
	 */
	private void processJavaScript(String js, HTMLElement htmlElement, Element parentWapElement, String buttonName, List parentWapElementNames) throws ServiceException {
		if(js.startsWith("history.back")) { //后退
			Element doElement = parentWapElement.addElement("do");
			doElement.addAttribute("type", "accept");
			doElement.addAttribute("lebel", buttonName);
			doElement.addElement("prev");
			return;
		}
		if(js.startsWith("window.open(") || js.startsWith("location.href=")) { //打开链接
			Element anchorElement = parentWapElement.addElement("a");
			int len = (js.startsWith("window.open(") ? "window.open(".length() : "location.href=".length());
			anchorElement.addAttribute("href", js.substring(len + 1, js.indexOf("'", len + 1)));
			if(buttonName==null) { //没有指定按钮名称
				writeHTMLNodes(htmlElement, anchorElement, parentWapElementNames);
			}
			else {
				anchorElement.setText(buttonName);
			}
			return;
		}
		String formAction = null;
		if(js.startsWith("FormUtils.doAction('")) { //执行操作
			int len = "FormUtils.doAction('".length();
			int index = js.indexOf("'", len);
			formAction = js.substring(len, index);
			//检查扩展参数
			index = js.indexOf("'", index + 2);
			if(index!=-1) {
				formAction += "?" + js.substring(index + 1, js.indexOf("'", index + 1));
			}
		}
		else if(js.indexOf("submit")==-1) { //不是提交脚本,跳过不处理 
			return;
		}
		//向上查找form
		HTMLFormElement form = null;
		Node node = htmlElement;
		while((node=node.getParentNode())!=null) {
			if("form".equalsIgnoreCase(node.getNodeName())) {
				form = (HTMLFormElement)node;
				break;
			}
		}
		if(form==null) {
			return;
		}
		//添加do
		Element doElement = parentWapElement.addElement("do");
		doElement.addAttribute("type", "accept");
		doElement.addAttribute("label", buttonName);
		//添加go
		Element goElement = doElement.addElement("go");
		goElement.addAttribute("href", (formAction==null ? form.getAction() : formAction));
		goElement.addAttribute("method", "get".equalsIgnoreCase(form.getMethod()) ? "get" : "post");
		goElement.addAttribute("accept-charset", "utf-8");
		//添加字段
		List fieldNames = new ArrayList();
		appendPostfield(goElement, form.getElementsByTagName("input"), fieldNames);
		appendPostfield(goElement, form.getElementsByTagName("textarea"), fieldNames);
	}
	
	/**
	 * 添加字段
	 * @param goElement
	 * @param elements
	 * @param fieldNames
	 * @throws ServiceException
	 */
	private void appendPostfield(Element goElement, NodeList elements, List fieldNames) throws ServiceException {
		if(elements==null || elements.getLength()==0) {
			return;
		}
		for(int i=0; i<elements.getLength(); i++) {
			HTMLElement element = (HTMLElement)elements.item(i);
			String name = element.getAttribute("name");
			if(fieldNames.indexOf(name)!=-1) { //已经添加过
				continue;
			}
			if(name.equals("validateCode")) { //WAP不支持验证码
				continue;
			}
			fieldNames.add(name);
			String type = element.getAttribute("type");
			Element postfieldElement = goElement.addElement("postfield");
			postfieldElement.addAttribute("name", name);
			if("hidden".equalsIgnoreCase(type)) { //隐藏字段
				postfieldElement.addAttribute("value", element.getAttribute("value"));
			}
			else {
				postfieldElement.addAttribute("value", "$(" + name + ")");
			}
		}
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
	
	public static void main(String[] args) throws Exception {
		WapPageServiceImpl service = new WapPageServiceImpl();
		service.setHtmlParser(new CyberNekoHTMLParser());
		OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream("c:/12.wml"), "UTF-8"); 
		service.writeWapPage("<html><head><title>测试</title></head><body></body></html>", fileWriter);
		fileWriter.close();
	}
}