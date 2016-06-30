/*
 * Created on 2005-3-2
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;


/**
 * 
 * @author linchuan
 *
 */
public class NotEmptyTag extends EmptyTag {
    /**
     * Perform the test required for this particular tag, and either evaluate
     * or skip the body of this tag.
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {
        if (!condition(true))
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);

    }
}
