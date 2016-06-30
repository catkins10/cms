/*
 * Created on 2004-12-23
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

/**
 * 
 * @author linchuan
 *
 */
public class NotEqualTag extends EqualTag {
	/**
	 * Perform the test required for this particular tag, and either evaluate
	 * or skip the body of this tag.
	 *
	 * @exception JspException if a JSP exception occurs
	 */
	public int doStartTag() throws JspException {
		return super.doStartTag()==SKIP_BODY ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
}
