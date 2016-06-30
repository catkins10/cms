/*
 * Copyright 2003-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuanluesoft.portal.container.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * The <CODE>StoredResponse</CODE> is a
 * <CODE>HttpServletResponse</CODE> that does not
 * write to the output stream directly but buffers
 * the markup.
 * Required to return the markup produced by a
 * <CODE>servlet</CODE> (e.g. a <CODE>WSRPlet</CODE>)
 * as a <CODE>String</CODE>.
 *
 * @see javax.servlet.http.HttpServletResponse
 * @version $Id: StoredResponse.java 375059 2006-02-05 17:29:15Z cziegeler $
 */
public class PortletStoredResponseImpl implements HttpServletResponse, Serializable {
    //default status code
    //private static final int DEFAULT_STATUS_CODE = 200;

    //writer
    private PrintWriter writer;

    //finished
    //private boolean isFinished;

    //status code
    //private int statusCode;

    //status message
    //private String statusMessage;

    //redirect URL
    //private String redirectURI;
    
    private String contentType;
    
    private String characterEncoding;

    //string writer
    private StringWriter stringWriter;

    //locale
    private Locale locale;
    
    private String outputBufferAsString;

    /**
     * Default constructor
     */
    public PortletStoredResponseImpl() {
        //statusCode = DEFAULT_STATUS_CODE;
        locale = new Locale("en", "US");
        setContentType("text/html");
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addCookie
     */
    public void addCookie(Cookie cookie) {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addDateHeader
     */
    public void addDateHeader(String a, long b) {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addHeader
     */
    public void addHeader(String a, String b) {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addIntHeader
     */
    public void addIntHeader(String a, int b) {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#containsHeader
     */
    public boolean containsHeader(String s) {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL
     */
    public String encodeRedirectURL(String s) {
        return s;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl
     */
    public String encodeRedirectUrl(String s) {
        return encodeRedirectURL(s);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeURL
     */
    public String encodeURL(String s) {
        return s;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeUrl
     */
    public String encodeUrl(String s) {
        return encodeURL(s);
    }

    /**
     * Internal method
     * Finish the response.
     */
    /*private void finish() throws IOException {
        synchronized (this) {
            if (isFinished)
                return;
            if (writer != null) {
                writer.flush();
                stringWriter.flush();
            }
            
            isFinished = true;
            setContentLength(stringWriter.getBuffer().length());
            writer = null;
        }
    }*/

    /**
     * @see javax.servlet.http.HttpServletResponse#flushBuffer()
     */
    public void flushBuffer() {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#getBufferSize()
     */
    public int getBufferSize() {
        return 4096;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return characterEncoding==null ? System.getProperty("default.client.encoding") : characterEncoding;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#getLocale()
     */
    public java.util.Locale getLocale() {
        return locale;
    }

    /**
     * Returns the output buffer content as String
     * @return java.lang.String
     */
    public String getOutputBufferAsString() throws IOException {
    	if(outputBufferAsString!=null) {
    		return outputBufferAsString;
    	}
        if(stringWriter == null) {
            return "";
        }
        return stringWriter.getBuffer().toString();
    }

	/**
	 * @param outputBufferAsString the outputBufferAsString to set
	 */
	public void setOutputBufferAsString(String outputBufferAsString) {
		this.outputBufferAsString = outputBufferAsString;
	}

    /**
     * @see javax.servlet.http.HttpServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#getWriter()
     */
    public PrintWriter getWriter() {
        if ((stringWriter == null) || (writer == null)) {
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
        }
        return writer;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#isCommitted()
     */
    public boolean isCommitted() {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#reset()
     */
    public void reset() {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int i) throws IOException {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int, String)
     */
    public void sendError(int i, String s) throws IOException {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(String)
     */
    public void sendRedirect(String s) {
        //redirectURI = s;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int i) {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setContentLength(int)
     */
    public void setContentLength(int i) {
        setIntHeader("content-length", i);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setContentType(String)
     */
    public void setContentType(String s) {
        setHeader("content-type", s);
        contentType = s;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(String, long)
     */
    public void setDateHeader(String s, long l) {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setHeader(String, String)
     */
    public void setHeader(String s, String s1) {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setIntHeader
     */
    public void setIntHeader(String s, int i) {
        // nothing to do?
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setLocale(Locale)
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int i) {
        //statusCode = i;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, String)
     */
    public void setStatus(int i, String s) {
        //statusCode = i;
        //statusMessage = s;
    }

    /**
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer() {
        // nothing to do?
    }

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}
}