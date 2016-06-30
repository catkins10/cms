/*
 * Created on 2006-5-6
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

/**
 * 
 * @author linchuan
 *
 */
public class NotInstanceofTag extends InstanceofTag {

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        return super.doStartTag()==SKIP_BODY ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
