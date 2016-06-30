/*
 * Created on 2007-7-20
 *
 */
package com.yuanluesoft.jeaf.htmlparser.cyberneko;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.base.BaseHTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.FileUtils;


/**
 * 
 * @author linchuan
 *
 */
public class CyberNekoHTMLParser extends BaseHTMLParser implements HTMLParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HTMLParser#parseHTMLFile(java.lang.String)
	 */
	public HTMLDocument parseHTMLFile(String htmlFileName) throws ServiceException {
		try {
			String encoding = FileUtils.getCharset(htmlFileName);
			InputStream inputStream = new FileInputStream(htmlFileName);
			int skip = 0;
			if("UTF-8".equals(encoding)) {
				skip = 3;
			}
			else if("Unicode".equals(encoding)) {
				skip = 2;
			}
			inputStream.skip(skip);
			HTMLDocument htmlDocument = parse(inputStream, encoding);
			String charset = getHTMLDocumentCharset(htmlDocument);
			if(charset!=null && charset.compareToIgnoreCase(encoding)!=0 && (!encoding.equals("GBK") || !charset.toLowerCase().startsWith("gb"))) {
				//字符集和原先判断的不一致,重新解析
				htmlDocument = parse(new FileInputStream(htmlFileName), charset);
			}
			return htmlDocument;
		}
		catch(FileNotFoundException e) {
			Logger.error("FileNotFoundException:" + e.getMessage());
			return null;
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.common.parser.HTMLParser#parseHTMLString(java.lang.String)
	 */
	public HTMLDocument parseHTMLString(String htmlString, String encoding) throws ServiceException {
		if(htmlString==null || htmlString.equals("")) {
			return null;
		}
		if(encoding==null) {
			encoding = "utf-8";
		}
		try {
			if(htmlString.startsWith("<script") || htmlString.startsWith("<SCRIPT")) {
				htmlString = "<b></b>" + htmlString;
			}
			byte[] htmlBytes = htmlString.getBytes(encoding);
			int skip = 0;
			if(256 + htmlBytes[0]==0xEF && 256 + htmlBytes[1]==0xBB && 256 + htmlBytes[2]==0xBF) {
				skip = 3;
			}
			else if(256 + htmlBytes[0]==0xFF && 256 + htmlBytes[1]==0xFE) {
				skip = 2;
			}
			return parse(new ByteArrayInputStream(htmlBytes, skip, htmlBytes.length-skip), encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 解析HTML
	 * @param inputStream
	 * @param encoding
	 * @return
	 * @throws ServiceException
	 */
	protected HTMLDocument parse(InputStream inputStream, String encoding) throws ServiceException {
		try {
			InputSource inputSource = new InputSource(inputStream);
			if(encoding!=null) {
				inputSource.setEncoding(encoding);
			}
		    DOMParser parser = new DOMParser();
		    parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
		    parser.setFeature("http://xml.org/sax/features/validation", false);
		    parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
		    //parser.setProperty("http://cyberneko.org/html/properties/names/elems", "match"); 
	        parser.parse(inputSource);
	        return (HTMLDocument)parser.getDocument();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
}