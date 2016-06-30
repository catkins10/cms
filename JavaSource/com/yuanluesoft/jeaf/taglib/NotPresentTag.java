/*
 * Created on 2006-5-14
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.Constants;

/**
 * 
 * @author linchuan
 *
 */
public class NotPresentTag extends org.apache.struts.taglib.logic.NotPresentTag {

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        if(name==null) {
            name = Constants.BEAN_KEY;
        }
        return super.doStartTag();
    }
}
