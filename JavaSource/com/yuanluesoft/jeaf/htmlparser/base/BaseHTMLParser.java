/*
 * Created on 2007-7-20
 *
 */
package com.yuanluesoft.jeaf.htmlparser.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.enhydra.xml.io.DOMFormatter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLMetaElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.html.HTMLStyleElement;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.htmlparser.model.StyleDefine;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public abstract class BaseHTMLParser implements HTMLParser {
	protected final String DEFAULT_CHARSET = "GBK";

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HtmlParser#parseHTMLFile(java.lang.String)
	 */
	public abstract HTMLDocument parseHTMLFile(String htmlFileName) throws ServiceException;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HtmlParser#parseHTMLString(java.lang.String)
	 */
	public abstract HTMLDocument parseHTMLString(String htmlString, String encoding) throws ServiceException;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HtmlParser#getHTMLDocumentCharset(org.w3c.dom.html.HTMLDocument)
	 */
	public String getHTMLDocumentCharset(HTMLDocument htmlDocument) throws ServiceException {
		NodeList nodes = htmlDocument.getElementsByTagName("META");
		if(nodes!=null) { //获取页面编码
			for(int i=0; i<nodes.getLength(); i++) {
				String content = ((HTMLMetaElement)nodes.item(i)).getContent();
				int index = content.indexOf("charset=");
				if(index!=-1) {
					return content.substring(index + "charset=".length());
				}
			}
		}
		return DEFAULT_CHARSET;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HtmlParser#saveHTMLAsFile(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String)
	 */
	public void saveHTMLDocumentToFile(HTMLDocument htmlDocument, String htmlFileName, String encoding) throws ServiceException {
		OutputStreamWriter fileWriter = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(htmlFileName);
		}
		catch(FileNotFoundException e) {
			htmlFileName = htmlFileName.replaceAll("\\\\", "/");
			FileUtils.createDirectory(htmlFileName.substring(0, htmlFileName.lastIndexOf('/')));
		}
		try {
			if(out==null) {
				out = new FileOutputStream(htmlFileName);
			}
			if("UTF-8".equalsIgnoreCase(encoding)) {
				out.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
			}
			else if("UNICODE".equalsIgnoreCase(encoding)) {
				out.write(new byte[]{(byte)0xFF, (byte)0xFE});
			}
			fileWriter = new OutputStreamWriter(out, "UTF-8");
			writeHTMLDocument(htmlDocument, fileWriter, encoding);
		}
		catch (Exception e) {
			Logger.exception(e);
			new File(htmlFileName).delete();
			throw new ServiceException();
		}
		finally {
			try {
				fileWriter.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#writeHTMLDocument(org.w3c.dom.html.HTMLDocument, java.io.Writer, java.lang.String)
	 */
	public void writeHTMLDocument(HTMLDocument htmlDocument, Writer writer, String encoding) throws ServiceException {
		if(encoding!=null) { //设置字符集
			setHTMLDocumentCharset(htmlDocument, encoding);
		}
		try {
	        DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlDocument));
	        formatter.getOutputOptions().setEncoding(encoding);
	        if(htmlDocument.getDoctype()!=null && htmlDocument.getDoctype().getPublicId()==null && htmlDocument.getDoctype().getSystemId()==null) {
	        	writer.write("<!DOCTYPE " + htmlDocument.getDoctype().getName() + ">\r\n");
	        }
            formatter.write(htmlDocument, writer);
        }
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HTMLParser#getBodyHTML(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public String getBodyHTML(HTMLDocument htmlDocument, String encoding, boolean htmlLowerCase) throws ServiceException {
		DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlDocument));
		if(htmlLowerCase) {
			formatter.getOutputOptions().setForceHTMLLowerCase(true);
		}
		formatter.getOutputOptions().setEncoding(encoding);
		NodeList nodes = htmlDocument.getBody().getChildNodes();
		int count = nodes.getLength();
		if(count==0) {
			return null;
		}
		StringBuffer html = new StringBuffer();
		for(int i=0; i<count; i++) {
			html.append(formatter.toString(nodes.item(i)));
		}
		return html.toString();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#getElementHTML(org.w3c.dom.html.HTMLElement, java.lang.String)
	 */
	public String getElementHTML(HTMLElement htmlElement, String encoding) throws ServiceException {
		DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlElement.getOwnerDocument()));
		formatter.getOutputOptions().setEncoding(encoding);
		return formatter.toString(htmlElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#getElementInnerHTML(org.w3c.dom.html.HTMLElement, java.lang.String)
	 */
	public String getElementInnerHTML(HTMLElement htmlElement, String encoding) throws ServiceException {
		NodeList nodes = htmlElement.getChildNodes();
		if(nodes==null || nodes.getLength()==0) {
			return null;
		}
		DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlElement.getOwnerDocument()));
		formatter.getOutputOptions().setEncoding(encoding);
		String innerHTML = "";
		for(int i=0; i<nodes.getLength(); i++) {
			innerHTML += formatter.toString(nodes.item(i));
		}
		return innerHTML;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HTMLParser#getDocumentHTML(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public String getDocumentHTML(HTMLDocument htmlDocument, String encoding) throws ServiceException {
		if(encoding!=null) { //设置字符集
			setHTMLDocumentCharset(htmlDocument, encoding);
		}
		DOMFormatter formatter = new DOMFormatter(DOMFormatter.getDefaultOutputOptions(htmlDocument));
		formatter.getOutputOptions().setEncoding(encoding);
		String html = "";
		if(htmlDocument.getDoctype()!=null && htmlDocument.getDoctype().getPublicId()==null && htmlDocument.getDoctype().getSystemId()==null) {
			html = "<!DOCTYPE " + htmlDocument.getDoctype().getName() + ">\r\n";
        }
		return html + formatter.toString(htmlDocument);
	}

	/**
	 * 设置HTML的字符集
	 * @param htmlPage
	 * @param charset
	 * @throws ServiceException
	 */
	protected void setHTMLDocumentCharset(HTMLDocument htmlPage, String charset) throws ServiceException {
		NodeList nodes = htmlPage.getElementsByTagName("META");
		if(nodes!=null) { //设置页面编码
			for(int i=0; i<nodes.getLength(); i++) {
				HTMLMetaElement metaElement = (HTMLMetaElement)nodes.item(i);
				String content = metaElement.getContent();
				int index = content.indexOf("charset=");
				if(index!=-1) {
					metaElement.setContent(content.substring(0, index + "charset=".length()) + charset);
					return;
				}
			}
		}
		HTMLHeadElement htmlHead = getHTMLHeader(htmlPage, true); //获取head元素
		//添加meta元素
		HTMLMetaElement metaElement = (HTMLMetaElement)htmlPage.createElement("meta");
		metaElement.setContent("text/html; charset=utf-8");
		metaElement.setHttpEquiv("Content-Type");
		//设置为head的第一个对象
		NodeList childNodes = htmlHead.getChildNodes();
		if(childNodes==null || childNodes.getLength()==0) {
			htmlHead.appendChild(metaElement);
		}
		else {
			htmlHead.insertBefore(metaElement, childNodes.item(0));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#getHTMLHeader(org.w3c.dom.html.HTMLDocument, boolean)
	 */
	public HTMLHeadElement getHTMLHeader(HTMLDocument htmlPage, boolean createIfNotExist) throws ServiceException {
		NodeList nodes = htmlPage.getElementsByTagName("head");
		HTMLHeadElement htmlHead = null;
		if(nodes!=null && nodes.getLength()>0) {
			htmlHead = (HTMLHeadElement)nodes.item(0);
		}
		else if(createIfNotExist) {
			htmlHead = (HTMLHeadElement)htmlPage.createElement("head");
			Node firstNode = null;
			nodes = htmlPage.getChildNodes();
			if(nodes!=null && nodes.getLength()>0) {
				firstNode = nodes.item(0);
				if(firstNode instanceof HTMLElement) {
					nodes = firstNode.getChildNodes();
					if(nodes==null || nodes.getLength()==0) {
						firstNode = null;
					}
					else {
						firstNode = nodes.item(0);
					}
				}
			}
			if(firstNode!=null) {
				htmlHead = (HTMLHeadElement)firstNode.getParentNode().insertBefore(htmlHead, firstNode);
			}
			else {
				htmlHead = (HTMLHeadElement)htmlPage.appendChild(htmlHead);
			}
		}
		return htmlHead;
	}
	

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#appendCssFile(org.w3c.dom.html.HTMLDocument, java.lang.String, boolean)
	 */
	public HTMLLinkElement appendCssFile(HTMLDocument htmlPage, String cssFileName, boolean firstCssFile) throws ServiceException {
		if(cssFileName==null || cssFileName.isEmpty()) {
			return null;
		}
		//检查是否已经引入过
		NodeList linkElements = htmlPage.getElementsByTagName("link");
		if(linkElements!=null && linkElements.getLength()>=0) {
			for(int i=linkElements.getLength()-1; i>=0; i--) {
				HTMLLinkElement linkElement = (HTMLLinkElement)linkElements.item(i);
				if(linkElement.getHref()!=null && linkElement.getHref().endsWith(cssFileName)) { //已经引入过,不再引入
					return linkElement;
				}
			}
		}
		//引入css文件
		HTMLLinkElement linkElement =  (HTMLLinkElement)htmlPage.createElement("link");
		linkElement.setRel("stylesheet");
		linkElement.setType("text/css");
		linkElement.setHref(cssFileName);
		HTMLHeadElement htmlHead = getHTMLHeader(htmlPage, true); //获取head元素
		if(!firstCssFile) {
			htmlHead.appendChild(linkElement);
			return linkElement;
		}
		NodeList nodes = htmlHead.getChildNodes();
		if(nodes==null || nodes.getLength()==0) {
			htmlHead.appendChild(linkElement);
			return linkElement;
		}
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node instanceof HTMLLinkElement || node instanceof HTMLStyleElement) {
				htmlHead.insertBefore(linkElement, node);
				return linkElement;
			}
		}
		htmlHead.appendChild(linkElement);
		return linkElement;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#appendScriptFile(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public void appendScriptFile(HTMLDocument htmlPage, String scriptFileName) throws ServiceException {
		//检查是否已经引入过
		NodeList scriptElements = htmlPage.getElementsByTagName("script");
		if(scriptElements!=null && scriptElements.getLength()>=0) {
			for(int i=scriptElements.getLength()-1; i>=0; i--) {
				HTMLScriptElement scriptElement = (HTMLScriptElement)scriptElements.item(i);
				String src = scriptElement.getSrc();
				if(src!=null && src.endsWith(scriptFileName)) { //已经引入过,不再引入
					return;
				}
			}
		}
		//引入js文件
		HTMLScriptElement scriptElement =  (HTMLScriptElement)htmlPage.createElement("script");
		scriptElement.setCharset("utf-8");
		scriptElement.setSrc(scriptFileName);
		HTMLHeadElement htmlHead = getHTMLHeader(htmlPage, true); //获取head元素
		htmlHead.appendChild(scriptElement);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#appendScript(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public void appendScript(HTMLDocument htmlPage, String script) throws ServiceException {
		HTMLScriptElement scriptElement = (HTMLScriptElement)htmlPage.createElement("script");
		scriptElement.setLang("javascript");
		setTextContent(scriptElement, script);
		HTMLHeadElement head = getHTMLHeader(htmlPage, true);
		head.appendChild(scriptElement);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#appendHiddenField(java.lang.String, java.lang.String, org.w3c.dom.html.HTMLElement)
	 */
	public void appendHiddenField(String fieldName, String fieldValue, HTMLElement pageElement) throws ServiceException {
		HTMLInputElement hiddenElement = null;
		NodeList inputs = pageElement.getElementsByTagName("input");
		for(int i=0; hiddenElement==null && i<(inputs==null ? 0 : inputs.getLength()); i++) {
			HTMLInputElement input = (HTMLInputElement)inputs.item(i);
			if("hidden".equalsIgnoreCase(input.getType()) && fieldName.equals(input.getName())) {
				hiddenElement = input;
			}
		}
		if(hiddenElement==null) {
			hiddenElement = (HTMLInputElement)pageElement.getOwnerDocument().createElement("input");
			hiddenElement.setAttribute("type", "hidden");
			hiddenElement.setAttribute("name", fieldName);
			pageElement.appendChild(hiddenElement);
		}
		hiddenElement.setAttribute("value", fieldValue);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#appendMeta(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String)
	 */
	public void appendMeta(HTMLDocument htmlPage, String name, String content) throws ServiceException {
		HTMLMetaElement metaElement = (HTMLMetaElement)htmlPage.createElement("meta");
		metaElement.setName(name);
		metaElement.setContent(content);
		
		//获取头部
		HTMLHeadElement head = getHTMLHeader(htmlPage, true);
		//获取最后一个meta
		Node lastMeta = getLastElement(head, "meta");
		if(lastMeta==null) {
			lastMeta = getLastElement(head, "title");
		}
		if(lastMeta!=null) {
			lastMeta = lastMeta.getNextSibling();
		}
		if(lastMeta==null) {
			head.appendChild(metaElement);
		}
		else {
			head.insertBefore(metaElement, lastMeta);
		}
	}
	
	/**
	 * 获取最后一个元素
	 * @param parentElement
	 * @param tagName
	 * @return
	 */
	private HTMLElement getLastElement(HTMLElement parentElement, String tagName) {
		NodeList elements = parentElement.getElementsByTagName(tagName);
		return (HTMLElement)(elements==null || elements.getLength()==0 ? null : elements.item(elements.getLength()-1));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#getMeta(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public String getMeta(HTMLDocument htmlPage, String name) throws ServiceException {
		NodeList metas = htmlPage.getElementsByTagName("meta");
		if(metas==null) {
			return null;
		}
		for(int i=metas.getLength()-1; i>=0; i--) {
			HTMLMetaElement metaElement = (HTMLMetaElement)metas.item(i);
			if(name.equals(metaElement.getName())) {
				return metaElement.getContent();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#removeMeta(org.w3c.dom.html.HTMLDocument, java.lang.String)
	 */
	public void removeMeta(HTMLDocument htmlPage, String name) throws ServiceException {
		NodeList metas = htmlPage.getElementsByTagName("meta");
		if(metas==null) {
			return;
		}
		for(int i=metas.getLength()-1; i>=0; i--) {
			HTMLMetaElement metaElement = (HTMLMetaElement)metas.item(i);
			if(name.equals(metaElement.getName())) {
				metaElement.getParentNode().removeChild(metaElement);
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#appendDisableErrorAlertScript(org.w3c.dom.html.HTMLDocument)
	 */
	public void appendDisableErrorAlertScript(HTMLDocument htmlPage) throws ServiceException {
		HTMLHeadElement htmlHead = getHTMLHeader(htmlPage, true); //获取head元素
		NodeList scripts = htmlHead.getElementsByTagName("script");
		String js = "window.onerror=function(){return true;}";
		//检查是否已经加入
		if(scripts!=null) {
			for(int i=0; i<scripts.getLength(); i++) {
				HTMLScriptElement scriptElement = (HTMLScriptElement)scripts.item(i);
				if(js.equals(scriptElement.getText().trim())) {
					return; //已经插入过
				}
			}
		}
		//插入脚本
		HTMLScriptElement scriptElement = (HTMLScriptElement)htmlPage.createElement("script");
		scriptElement.setText(js);
		NodeList nodes = htmlHead.getChildNodes();
		if(nodes==null || nodes.getLength()==0) {
			htmlHead.appendChild(scriptElement);
		}
		else {
			htmlHead.insertBefore(scriptElement, nodes.item(0));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#getTextContent(org.w3c.dom.Node)
	 */
	public String getTextContent(Node node) {
		//$nbsp;替换为0xa0
		if(node instanceof Text) {
			return ((Text)node).getTextContent();
		}
		StringBuffer stringbuffer = new StringBuffer();
		getTextContent(node, stringbuffer);
		return stringbuffer.toString();
	}
	
	/**
	 * 递归获取文本
	 * @param parentNode
	 * @param stringbuffer
	 */
	private void getTextContent(Node parentNode, StringBuffer stringbuffer) {
		for(Node node = parentNode.getFirstChild(); node != null; node = node.getNextSibling()) {
			if(node.getNodeType()==Node.TEXT_NODE) {
				stringbuffer.append(node.getNodeValue());
			}
			else if(node.getNodeType() != Node.COMMENT_NODE && node.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE) {
				getTextContent(node, stringbuffer);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.htmlparser.HTMLParser#setTextContent(org.w3c.dom.Node, java.lang.String)
	 */
	public void setTextContent(Node node, String text) {
		//删除原来的文本
		Node childNode;
		while((childNode=node.getFirstChild())!=null) { 
			node.removeChild(childNode);
		}
		if(text!=null && !text.isEmpty()) {
			node.appendChild(node.getOwnerDocument().createTextNode(text));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#importNodes(org.w3c.dom.NodeList, org.w3c.dom.html.HTMLElement, boolean)
	 */
	public void importNodes(NodeList toImportNodes, Node targetNode, boolean insertBefore) {
		if(toImportNodes==null) {
			return;
		}
		HTMLDocument document = (HTMLDocument)targetNode.getOwnerDocument();
		for(int i=0; i<toImportNodes.getLength(); i++) {
			if(insertBefore) {
				targetNode.getParentNode().insertBefore(document.importNode(toImportNodes.item(i), true), targetNode);
			}
			else {
				targetNode.appendChild(document.importNode(toImportNodes.item(i), true));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#moveNodes(org.w3c.dom.html.HTMLElement, org.w3c.dom.html.HTMLElement)
	 */
	public void moveChildNodes(HTMLElement fromElement, HTMLElement toElement) {
		NodeList nodes = fromElement.getChildNodes();
		if(nodes==null) {
			return;
		}
		Node lastNode = null;
		for(int i=nodes.getLength()-1; i>=0; i--) {
			Node node = nodes.item(i);
			if(lastNode==null) {
				lastNode = toElement.appendChild(node);
			}
			else {
				lastNode = toElement.insertBefore(node, lastNode);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.kdsoft.jeaf.htmlparser.HTMLParser#getElementById(org.w3c.dom.html.HTMLElement, java.lang.String, java.lang.String)
	 */
	public HTMLElement getElementById(HTMLElement parentElement, String tagName, final String elementId) throws ServiceException {
		if(tagName==null) {
			final HTMLElement[] found = {null};
			traversalChildNodes(parentElement.getChildNodes(), true, new HTMLTraversalCallback() {
				public boolean processNode(Node node) {
					if(elementId.equals(((HTMLElement)node).getId())) {
						found[0] = (HTMLElement)node;
						return true;
					}
					return false;
				}
			});
			return found[0];
		}
		NodeList elements = parentElement.getElementsByTagName(tagName);
		if(elements==null || elements.getLength()==0) {
			return null;
		}
		for(int i=0; i<elements.getLength(); i++) {
			HTMLElement element = (HTMLElement)elements.item(i);
			if(elementId.equals(element.getId())) {
				return element;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#traversalChildNodes(org.w3c.dom.NodeList, boolean, com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback)
	 */
	public void traversalChildNodes(NodeList nodes, boolean htmlElementOnly, HTMLTraversalCallback callback) throws ServiceException {
		for(int i=(nodes==null ? -1 : nodes.getLength()-1); i>=0; i--) {
			Node childNode = nodes.item(i);
			if(htmlElementOnly && !(childNode instanceof HTMLElement)) {
				continue;
			}
			//处理节点
			if(!callback.processNode(childNode) && childNode.getParentNode()!=null) { //未删除
				traversalChildNodes(childNode.getChildNodes(), htmlElementOnly, callback); //递归查找下一级
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.kdsoft.jeaf.htmlparser.HTMLParser#getElementByName(org.w3c.dom.html.HTMLElement, java.lang.String, java.lang.String)
	 */
	public HTMLElement getElementByName(HTMLElement parentElement, String tagName, String elementName) throws ServiceException {
		NodeList elements = parentElement.getElementsByTagName(tagName);
		if(elements==null || elements.getLength()==0) {
			return null;
		}
		for(int i=0; i<elements.getLength(); i++) {
			HTMLElement element = (HTMLElement)elements.item(i);
			if(elementName.equals(element.getAttribute("name"))) {
				return element;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#setStyle(org.w3c.dom.html.HTMLElement, java.lang.String, java.lang.String)
	 */
	public void setStyle(HTMLElement element, String styleName, String style) {
		String css = element.getAttribute("style");
		css = StringUtils.removeStyles(css, styleName);
		if(style!=null && !style.isEmpty()) {
			css = (css==null || css.isEmpty() ? "" : css + ";") + styleName  + ":" + style;
		}
		element.setAttribute("style", css);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.htmlparser.HTMLParser#parseStyleDefines(java.lang.String)
	 */
	public List parseStyleDefines(String styleDefine) throws ServiceException {
		if(styleDefine==null || styleDefine.isEmpty()) {
			return null;
		}
		List defines = new ArrayList();
		String[] styleDefines = styleDefine.split("\\$\\$");
		for(int i=0; i<styleDefines.length; i++) {
			String[] values = styleDefines[i].split("##");
			StyleDefine define = new StyleDefine();
			define.setStyleName(values[0]); //样式名称
			define.setIconUrl(values[1]); //图标URL
			define.setIconWidth(StringUtils.parseInt(values[2], 0)); //图标宽度
			define.setIconHeight(StringUtils.parseInt(values[3], 0)); //图标高度
			define.setCssFileName(values[4]); //样式表文件名称
			if(values.length>5) {
				define.setCssUrl(values[5]);
			}
			defines.add(define);
		}
		return defines;
	}
}