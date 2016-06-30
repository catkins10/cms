/*
 * Created on 2005-10-12
 *
 */
package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.jasper.runtime.JspRuntimeLibrary;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 子表单标签
 * @author linchuan
 *
 */
public class SubFormTag extends BodyTagSupport {

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		ActionForm actionForm = (ActionForm)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, null, null);
		try {
			JspRuntimeLibrary.include(pageContext.getRequest(), pageContext.getResponse(), actionForm.getSubForm(), pageContext.getOut(), false);
		}
		catch(Exception e) {
			
		}
		return SKIP_BODY;
	}
}