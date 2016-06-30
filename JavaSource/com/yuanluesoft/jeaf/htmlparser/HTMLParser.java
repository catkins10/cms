/*
 * Created on 2007-7-14
 *
 */
package com.yuanluesoft.jeaf.htmlparser;

import java.io.Writer;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLLinkElement;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface HTMLParser {
	
	/**
	 * 解析HTML文件
	 * @param htmlFileName
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument parseHTMLFile(String htmlFileName) throws ServiceException;
	
	/**
	 * 解析HTML文本
	 * @param htmlString
	 * @param encoding
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument parseHTMLString(String htmlString, String encoding) throws ServiceException;
	
	/**
	 * 保存HTML页面
	 * @param htmlPage
	 * @param htmlFileName
	 * @param encoding null则不改变编码
	 * @throws ServiceException
	 */
	public void saveHTMLDocumentToFile(HTMLDocument htmlDocument, String htmlFileName, String encoding) throws ServiceException;
	
	/**
	 * 输出HTML页面
	 * @param htmlPage
	 * @param writer
	 * @param encoding
	 * @throws ServiceException
	 */
	public void writeHTMLDocument(HTMLDocument htmlDocument, Writer writer, String encoding) throws ServiceException;
	
	/**
	 * 获取正文HTML文本
	 * @param encoding
	 * @param htmlLowerCase
	 * @param htmlPage
	 * @return
	 * @throws ServiceException
	 */
	public String getBodyHTML(HTMLDocument htmlDocument, String encoding, boolean htmlLowerCase) throws ServiceException;
	
	/**
	 * 获取HTML元素的HTML文本
	 * @param htmlElement
	 * @param encoding
	 * @return
	 * @throws ServiceException
	 */
	public String getElementHTML(HTMLElement htmlElement, String encoding) throws ServiceException;
	
	/**
	 * 获取HTML元素的内部HTML文本
	 * @param htmlElement
	 * @param encoding
	 * @return
	 * @throws ServiceException
	 */
	public String getElementInnerHTML(HTMLElement htmlElement, String encoding) throws ServiceException;
	
	/**
	 * 获取HTML文本
	 * @param htmlPage
	 * @param encoding
	 * @return
	 * @throws ServiceException
	 */
	public String getDocumentHTML(HTMLDocument htmlDocument, String encoding) throws ServiceException;
	
	/**
	 * 获取HTML页面的字符集
	 * @param htmlDocument
	 * @return
	 * @throws ServiceException
	 */
	public String getHTMLDocumentCharset(HTMLDocument htmlDocument) throws ServiceException;
	
	/**
	 * 引入css文件
	 * @param htmlPage
	 * @param cssFileName
	 * @param firstCssFile 是否作为第一个CSS文件
	 * @throws ServiceException
	 */
	public HTMLLinkElement appendCssFile(HTMLDocument htmlPage, String cssFileName, boolean firstCssFile) throws ServiceException;
	
	/**
	 * 引入脚本文件到HEAD
	 * @param htmlPage
	 * @param cssFileName
	 * @throws ServiceException
	 */
	public void appendScriptFile(HTMLDocument htmlPage, String scriptFileName) throws ServiceException;
	
	/**
	 * 添加脚本到HEAD
	 * @param htmlPage
	 * @param script
	 * @throws ServiceException
	 */
	public void appendScript(HTMLDocument htmlPage, String script) throws ServiceException;
	
	/**
	 * 添加META
	 * @param htmlPage
	 * @param name
	 * @param content
	 * @throws ServiceException
	 */
	public void appendMeta(HTMLDocument htmlPage, String name, String content) throws ServiceException;
	
	/**
	 * 获取MEAT的值
	 * @param htmlPage
	 * @param name
	 * @throws ServiceException
	 */
	public String getMeta(HTMLDocument htmlPage, String name) throws ServiceException;
	
	/**
	 * 删除META
	 * @param htmlPage
	 * @param name
	 * @throws ServiceException
	 */
	public void removeMeta(HTMLDocument htmlPage, String name) throws ServiceException;
	
	/**
	 * 插入隐藏字段
	 * @param fieldName
	 * @param fieldValue
	 * @param pageElement
	 */
	public void appendHiddenField(String fieldName, String fieldValue, HTMLElement pageElement) throws ServiceException;
	
	/**
	 * 插入屏蔽脚本错误的脚本
	 * @param htmlPage
	 * @throws ServiceException
	 */
	public void appendDisableErrorAlertScript(HTMLDocument htmlPage) throws ServiceException;
	
	/**
	 * 获取文本,$nbsp;替换为0xa0
	 * @param node
	 * @return
	 */
	public String getTextContent(Node node);
	
	/**
	 * 设置文本内容
	 * @param node
	 * @param text
	 */
	public void setTextContent(Node node, String text);
	
	/**
	 * 引入HTML元素列表
	 * @param toImportNodes
	 * @param targetElement
	 * @param insertBefore 是否插入到targetElement的前面
	 */
	public void importNodes(NodeList toImportNodes, Node targetNode, boolean insertBefore);
	
	/**
	 * 移动子节点
	 * @param fromElement
	 * @param toElement
	 */
	public void moveChildNodes(HTMLElement fromElement, HTMLElement toElement);
	
	/**
	 * 获取页面HEAD元素
	 * @param htmlPage
	 * @param createIfNotExist
	 * @return
	 * @throws ServiceException
	 */
	public HTMLHeadElement getHTMLHeader(HTMLDocument htmlPage, boolean createIfNotExist) throws ServiceException;
	
	/**
	 * 按ID获取HTML元素
	 * @param parentElement
	 * @param tagName
	 * @param elementId
	 * @return
	 * @throws ServiceException
	 */
	public HTMLElement getElementById(HTMLElement parentElement, String tagName, String elementId) throws ServiceException;
	
	/**
	 * 遍历HTML节点
	 * @param nodes
	 * @param htmlElementOnly 是否只遍历HTMLElement
	 * @param callback
	 * @throws ServiceException
	 */
	public void traversalChildNodes(NodeList nodes, boolean htmlElementOnly, HTMLTraversalCallback callback) throws ServiceException;
	
	/**
	 * 按名称获取HTML元素
	 * @param parentElement
	 * @param tagName
	 * @param elementName
	 * @return
	 * @throws ServiceException
	 */
	public HTMLElement getElementByName(HTMLElement parentElement, String tagName, String elementName) throws ServiceException;
	
	/**
	 * 设置样式
	 * @param element
	 * @param styleName
	 * @param style
	 */
	public void setStyle(HTMLElement element, String styleName, String style);
	
	/**
	 * 解析样式定义
	 * @param styleDefine
	 * @return
	 * @throws ServiceException
	 */
	public List parseStyleDefines(String styleDefine) throws ServiceException;
}