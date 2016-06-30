package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

/**
 * 
 * @author linchuan
 *
 */
public class NotEmptyImageTag extends EmptyImageTag {
	
	public int doStartTag() throws JspException {
        if (!condition())
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);
    }
}
