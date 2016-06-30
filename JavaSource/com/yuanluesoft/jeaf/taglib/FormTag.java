/*
 * Created on 2005-10-12
 *
 */
package com.yuanluesoft.jeaf.taglib;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.jasper.runtime.JspRuntimeLibrary;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.cms.pagebuilder.SiteFormService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.dialog.pages.DialogPageService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 表单标签,如果表单为空或者displayMode为空输出方式
 * @author linchuan
 *
 */
public class FormTag extends org.apache.struts.taglib.html.FormTag {
	private FormDefineService formDefineService = null; //表单定义服务
	private String applicationName = null; //应用名称
	private String pageName = null; //页面名称
	private String siteFormServiceName = null; //应用表单服务名称
	private String type; //类型
	//私有属性
	private SiteFormService defaultSiteFormService = null; //默认的表单服务
	private SiteFormService userFormService = null; //用户表单服务
	private DialogPageService dialogPageService; //对话框页面服务
	private String html =  null;
	private int formBodyEnd = 0;

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        //判断表单是否已经加载过,如果已经加载过,不允许再次加载,避免死循环,比如subForm设置成当前jsp
		if("true".equals(request.getAttribute("FORM_LOADED"))) {
			throw new JspException("form duplicate loaded");
		}
		request.setAttribute("FORM_LOADED", "true");
        ActionForm actionForm = getActionForm();
        //重新打开页面，且处于对话框提交模式
        if(Boolean.TRUE.equals(request.getAttribute("reload")) && actionForm.isInternalForm() && actionForm.getInnerDialog()==null && "dialog".equals(actionForm.getDisplayMode())) {
        	JspWriter writer = pageContext.getOut();
        	try {
				writer.write("<html>" +
							 "	<body>" +
							 (actionForm.getRefeshOpenerScript()==null ? "" : "<script language=\"JavaScript\">" + actionForm.getRefeshOpenerScript() + "</script>") +
							 "		<script language=\"JavaScript\">window.top.location='" + actionForm.getReloadPageURL() + "';</script>" +
							 "	</body>" +
							 "</html>");
			}
        	catch(IOException e) {
				
			}
			return SKIP_BODY;
		}
		// Create an appropriate "form" element based on our parameters
        StringBuffer results = new StringBuffer();
        if(onsubmit==null) {
        	 onsubmit = "return formOnSubmit()";
        }
        if(this.action==null) {
        	results.append("<form>");
        }
        else {
            // Look up the form bean name, scope, and type if necessary
    		lookup();
    		results.append(this.renderFormStartElement());
    		results.append(this.renderToken());
	        // Store this tag itself as a page attribute
	        pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);
	        initFormBean();
        }
        if(pageContext.getAttribute(Constants.BEAN_KEY, PageContext.REQUEST_SCOPE)==null) {
        	pageContext.setAttribute(Constants.BEAN_KEY, actionForm, PageContext.REQUEST_SCOPE);
        }
        if(pageContext.getAttribute(Constants.FORM_KEY, PageContext.REQUEST_SCOPE)==null) {
        	pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);
        }
        if(actionForm!=null && actionForm.getFormTitle()==null) {
        	actionForm.setFormTitle("");
        }
        long siteId; //站点ID
		try {
			siteId = ((Long)TagUtils.getInstance().lookup(pageContext, Constants.BEAN_KEY, "siteId", null)).longValue();
		}
		catch(Exception e) {
			try {
				String name = ((ActionConfig)TagUtils.getInstance().lookup(pageContext, "org.apache.struts.action.mapping.instance", null)).getName();
				siteId = ((Long)TagUtils.getInstance().lookup(pageContext, name, "siteId", null)).longValue();
			}
			catch(Exception ex) {
				siteId = RequestUtils.getParameterLongValue(request, "siteId");
			}
		}
		try {
			if(pageName==null && 
			   (actionForm==null || actionForm.getDisplayMode()==null || actionForm.getDisplayMode().isEmpty() || //表单为空或者自定义输出方式
			    ("window".equals(actionForm.getDisplayMode()) && (actionForm.getFormActions()==null || actionForm.getFormActions().isEmpty())))) { //输出方式为window, 且没有操作按钮
				html = "##APPLICATON_FORM##\n##APPLICATON_FORM_BODY##\n##APPLICATON_FORM_END##";
			}
			else if(actionForm!=null && "dialog".equals(actionForm.getDisplayMode()) && (pageName==null || "result".equals(type))) { //对话框
				generateDialogHtml(actionForm, siteId, request);
			}
			else {
				generateFormHtml(actionForm, siteId);
			}
			//输出隐藏字段
			String hiddenFilds = writeHiddenFields(actionForm);
			//表单HTML
			String formHtml = results.toString() +
							  (hiddenFilds==null ? "" : hiddenFilds) + "\n" +
							  (hiddenFilds!=null && hiddenFilds.indexOf("\"siteId\"")!=-1 ? "" : "<input type=\"hidden\" name=\"siteId\" value=\"" + siteId + "\">\n");
			writeForm(actionForm, true, formHtml); //输出表单
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return actionForm.isInternalForm() && "dialog".equals(actionForm.getDisplayMode()) && (actionForm.getPrompt()!=null || (actionForm.getErrors()!=null && !actionForm.getErrors().isEmpty())) ? SKIP_BODY : EVAL_BODY_INCLUDE;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		//Render a tag representing the end of our current form
        StringBuffer results = new StringBuffer("</form>");
        //Render JavaScript to set the input focus if required
        if(this.focus != null) {
            results.append(this.renderFocusJavascript());
        }
        ActionForm actionForm = getActionForm();
        try {
        	writeForm(actionForm, false, results.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
      	//Remove the page scope attributes we created
        pageContext.removeAttribute(Constants.BEAN_KEY, PageContext.REQUEST_SCOPE);
        pageContext.removeAttribute(Constants.FORM_KEY, PageContext.REQUEST_SCOPE);
        applicationName = null;
		pageName = null;
		html = null;
		formBodyEnd = 0;
		siteFormServiceName = null;
        return EVAL_PAGE; // Continue processing this page
	}
	
	/**
	 * 生成页面HTML
	 * @param actionForm
	 * @param siteId
	 * @throws Exception
	 */
	protected void generateFormHtml(ActionForm actionForm, long siteId) throws Exception {
		SiteFormService siteFormService = null;
		if(pageName!=null) { //指定了页面名称
			if(defaultSiteFormService==null) {
				defaultSiteFormService = (SiteFormService)Environment.getService("siteFormService");
			}
			siteFormService = defaultSiteFormService;
		}
		else { //未指定页面名称
			if(userFormService==null) {
				userFormService = (SiteFormService)Environment.getService("userFormService");
			}
			siteFormService = userFormService;
			applicationName = "jeaf/usermanage";
			pageName = "internalForm"; //后台管理：表单页面
			actionForm.setInternalForm(true);
		}
		if(siteFormServiceName!=null) {
			siteFormService = (SiteFormService)Environment.getService(siteFormServiceName);
		}
		String parameter = pageContext.getRequest().getParameter("templateId");
		long templateId = (parameter==null ? 0 : Long.parseLong(parameter));
		html = siteFormService.generateApplicationForm(applicationName, pageName, siteId, templateId, actionForm, (HttpServletRequest)pageContext.getRequest());
	}
	
	/**
	 * 生成对话框HTML
	 * @param actionForm
	 * @param siteId
	 * @param request
	 */
	protected void generateDialogHtml(ActionForm actionForm, long siteId, HttpServletRequest request) {
		String htmlBody;
		if("result".equals(type)) { //操作结果页面
			if(actionForm.getActionResult()==null || actionForm.getActionResult().isEmpty()) { //不需要提示
				htmlBody = "<script language=\"javascript\">DialogUtils.closeDialog();</script>"; //直接关闭对话框
			}
			else {
				actionForm.setFormTitle("系统提示");
				htmlBody = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
						   "	<tr>" +
						   "		<td class=\"promptImage\" valign=\"top\">&nbsp;</td>" + //消息提示图片
						   "		<td class=\"promptText\" valign=\"middle\">" + actionForm.getActionResult() + "</td>" + //消息提示内容
						   "	</tr>" +
						   "</table>";
			}
		}
		else if(actionForm.getInnerDialog()!=null) { //有内置对话框
			htmlBody = "##INCLUDE_JSP:formInnerDialog##";
		}
		else if(actionForm.isInternalForm() && (actionForm.getPrompt()!=null || (actionForm.getErrors()!=null && !actionForm.getErrors().isEmpty()))) {
			actionForm.setFormTitle("系统提示");
			htmlBody = "<script language=\"JavaScript\">window.top.submitting = false;</script>" +
					   "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
					   "	<tr>" +
					   "		<td class=\"promptImage" + (actionForm.getPrompt()!=null ? "" : " errorImage") + "\" valign=\"top\">&nbsp;</span>" + //消息提示图片
			   		   "		<td class=\"promptText\" valign=\"middle\">";
			if(actionForm.getPrompt()!=null) {
				htmlBody += actionForm.getPrompt();
			}
			else {
				for(int i = 0; i < actionForm.getErrors().size(); i++) {
					htmlBody += (i==0 ? "" : "<br/>") + (actionForm.getErrors().size()==1 ? "" :  (i + 1) + "、") + actionForm.getErrors().get(i);
				}
			}
			htmlBody += "		</td>" +
						"	</tr>" +
						"</table>";
			actionForm.getFormActions().clear();
			actionForm.getFormActions().addFormAction(-1, "确定", "DialogUtils.closeDialog();", true);
			String act = StringUtils.getQueryParameter(actionForm.getQueryString(), "act");
			String newAct = StringUtils.getQueryParameter(actionForm.getReloadPageURL(), "act");
			if((act==null ? "" : act).equals(newAct==null ? "" : newAct)) {
				actionForm.setReloadPageURL(null);
			}
			//htmlBody += "<script language=\"JavaScript\">window.setTimeout(function(){DialogUtils.closeDialog();}, 3000);</script>";
		}
		else { //没有有内置对话框
			htmlBody = "##APPLICATON_FORM_BODY##";
		}
		String styleHTML = null;
		try {
			if(dialogPageService==null) {
				dialogPageService = (DialogPageService)Environment.getService("dialogPageService");
			}
			styleHTML = dialogPageService.getDialogStyleHTML(request);
		}
		catch(ServiceException e) {
			
		}
		html = "<html>\n" +
			   "<head>\n" +
			   "	<title>" + actionForm.getFormTitle() + "</title>\n" +
			   (styleHTML==null ? "" : styleHTML) +
			   "</head>\n";
		if(actionForm.getFormActions()==null || actionForm.getFormActions().isEmpty()) { //没有按钮
			html += "<body class=\"dialogBody\" style=\"overflow:auto\">\n" +
			   		"	##APPLICATON_FORM##\n" +
					"		" + htmlBody + "\n";
		}
		else {
			html += "<body class=\"dialogBody\" style=\"overflow:hidden;\">\n" +
					"	##APPLICATON_FORM##\n" +
					"	<table id=\"dialogTable\" border=\"0\" height=\"100%\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed\">\n" +
					"		<tr>\n" +
					"			<td class=\"dialogContent\" height=\"100%\" valign=\"top\">\n" +
					"				<div id=\"dialogWidthCheck\" style=\"float:left; height:100%; display:block;\">\n" +
					"					" + htmlBody + "\n" +
					"					<div id=\"dialogHeightCheck\" style=\"clear:both; width:1px; height:1px; display:block; font-size:1px\">&nbsp;</div>\n" +
					"				</div>\n" +
					"			</td>\n" +
					"		</tr>\n" +
					"		<tr>\n" +
					"			<td align=\"right\" class=\"dialogButtonBar\" id=\"dialogButtonBar\">\n";
			for(Iterator iterator = actionForm.getFormActions().iterator(); iterator.hasNext();) {
				FormAction formAction = (FormAction)iterator.next();
				html += "			<input id=\"button_" + formAction.getTitle() + "\" type=\"button\" class=\"button\" onclick=\"" + formAction.getExecute() + "\" value=\"" + formAction.getTitle() + "\">\n";
			}
			html += "			</td>\n" +
					"		</tr>\n" +
					"	</table>\n";
		}
		html += "		##APPLICATON_FORM_END##" +
			    "	</body>\n" +
			    "</html>";
	}

	/**
	 * 输出应用表单
	 * @param writeHeader
	 * @param formElementHTML
	 * @throws Exception
	 */
	protected void writeForm(ActionForm actionForm, boolean writeHeader, String formElementHTML) throws Exception {
		if(html==null) {
			return;
		}
		int endIndex = (writeHeader ? 0 : formBodyEnd);
		JspWriter writer = pageContext.getOut();
		for(int beginIndex = html.indexOf("##", endIndex); beginIndex!=-1; beginIndex=html.indexOf("##", endIndex)) {
			//输出标记之前的HTML
			writer.write(html.substring(endIndex, beginIndex));
			//查找标记的结束位置
			beginIndex += 2;
			endIndex = html.indexOf("##", beginIndex);
			String element = html.substring(beginIndex, endIndex);
			endIndex += 2;
			if("APPLICATON_FORM".equals(element)) { //输出form的头部
				writer.write("<div id=\"divLoading\" style=\"background-color:#ffffff; position:absolute; width:100%; height:100%; filter:alpha(opacity=0); opacity:0; font-family:\u5B8B\u4F53; font-size:12px\"></div>");
				writer.write(formElementHTML);
				//输出common.js
				writer.write("<script language=\"JavaScript\" charset=\"utf-8\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/common.js\"></script>");
				//输出解锁URL
				SessionInfo sessionInfo = RequestUtils.getSessionInfo((HttpServletRequest)pageContext.getRequest());
				if(actionForm.isLocked() && sessionInfo!=null && !sessionInfo.isAnonymous() && actionForm.getUnlockUrl()!=null) {
					writer.write("<script language=\"JavaScript\">" +
								"	EventUtils.addEvent(window, 'unload', function() {" +
								"		if(!window.submitting && !window.unlocked) {" +
								"			new Ajax().openRequest('post', '" + actionForm.getUnlockUrl() + "', '', true);" +
								"			window.unlocked = true;" +
								"		}" +
								"	});" +
								"</script>");
				}
				//添加更新主窗口的脚本
				if(actionForm!=null && actionForm.getRefeshOpenerScript()!=null) {
					writer.write("<script language=\"JavaScript\">" + actionForm.getRefeshOpenerScript() + "</script>");
				}
			}
			else if("APPLICATON_FORM_END".equals(element)) { //输出form的尾部
				if(writeHeader) {
					formBodyEnd = beginIndex - 2;
					if(actionForm.getInnerDialog()!=null) { //有内置对话框
						writer.write("<div style=\"display: none;\">"); //隐藏form主体
					}
					return;
				}
				if(actionForm.getInnerDialog()!=null) { //有内置对话框
					writer.write("</div>");
				}
				writer.write(formElementHTML);
				writer.write("<script language=\"JavaScript\">var divLoading = document.getElementById(\"divLoading\"); divLoading.parentNode.removeChild(divLoading);</script>");
		        //检查是否有错误或者提示
		        if(actionForm!=null && //表单不为空
		           ((actionForm.getErrors()!=null && !actionForm.getErrors().isEmpty()) || //有错误信息
		        	 actionForm.getPrompt()!=null ||  //有提示信息
		        	 (actionForm.getActionResult()!=null && !"result".equals(type))) && //有操作结果,且不是结果页面
		           (!"dialog".equals(actionForm.getDisplayMode()) || !actionForm.isInternalForm()) &&
		           !"true".equals(pageContext.getRequest().getAttribute("writeFormPrompt"))) { //未输出过提示信息
		        	try {
						JspRuntimeLibrary.include(pageContext.getRequest(), pageContext.getResponse(), "/jeaf/form/alert.jsp", writer, false);
					}
		        	catch(Exception e) {
						e.printStackTrace();
					}
		        }
			}
			else if("APPLICATON_FORM_BODY".equals(element)) { //输出form的主体
				if(writeHeader) {
					formBodyEnd = endIndex;
					return;
				}
			}
			else if(element.startsWith("INCLUDE_JSP:")) { //引入jsp
				String jspFile = element.substring("INCLUDE_JSP:".length());
				if("formInnerDialog".equals(jspFile)) {
					jspFile = actionForm.getInnerDialog();
				}
				else {
					jspFile = jspFile + ".jsp";
				}
				JspRuntimeLibrary.include(pageContext.getRequest(), pageContext.getResponse(), jspFile, writer, false);
			}
		}
		//输出剩余的html
		writer.write(html.substring(endIndex));
	}

	/**
	 * 输出表单的隐藏字段
	 * @return
	 * @throws JspException
	 */
	protected String writeHiddenFields(ActionForm actionForm) throws JspException {
		if(formDefineService==null) {
			try {
				formDefineService = (FormDefineService)Environment.getService("formDefineService"); //加载业务逻辑定义服务
			}
			catch(ServiceException e) {
				
			}
		}
		if(actionForm==null) {
			return null;
		}
		if(actionForm.getFormDefine().getClassName()==null) { //没有加载表单定义
			try {
				Form formDefine = formDefineService.loadFormDefine(actionForm.getClass());
				if(formDefine!=null) {
					actionForm.setFormDefine(formDefine);
				}
			}
			catch (ServiceException e) {
				
			}
		}
		//获取字段列表
		List hiddenFields = FieldUtils.listFormFields(actionForm.getFormDefine(), null, null, "hidden", null, false, false, false, false, 0);
		if(hiddenFields==null || hiddenFields.isEmpty()) {
			return null;
		}
		StringBuffer results = new StringBuffer();
		//自动插入隐藏字段
		for(Iterator iterator = hiddenFields.iterator(); iterator.hasNext();) {
			Field fieldDefine = (Field)iterator.next();
			if(actionForm.getAct()!=null && !"".equals(actionForm.getAct()) && !"create".equals(actionForm.getAct()) && "true".equals(fieldDefine.getParameter("newFormOnly"))) { //参数 newFormOnly: 是否只在创建新记录时有效,默认false
				continue;
			}
			//检查对应的component字段的输入模式是否为只读,如果是则不输出
			int index = fieldDefine.getName().lastIndexOf('.');
			if(index!=-1) {
				Field componentField = (Field)ListUtils.findObjectByProperty(hiddenFields, "name", fieldDefine.getName().substring(0, index));
				if(componentField!=null && "readonly".equals(componentField.getInputMode())) {
					continue;
				}
			}
			
			//获取字段值
			String value;
			try {
				HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
				Object fieldValue = FieldUtils.getFieldValue(actionForm, fieldDefine.getName(), request);
				if(fieldValue==null) {
					try {
						fieldValue = FieldUtils.getFieldDefaultValue(fieldDefine, false, applicationName, actionForm, request);
					}
					catch(Exception e) {
						
					}
				}
				value = FieldUtils.formatFieldValue(fieldValue, fieldDefine, actionForm, false, null, true, false, false, 0, null, null, request);
			}
			catch(Exception e) {
				continue;
			}
			//输出hidden
			results.append("\r\n<input");
			//输出类型
			results.append(" type=\"hidden\"");
	        //输出字段名称
	        results.append(" name=\"");
	        results.append(fieldDefine.getName());
	        results.append("\"");
	        //输出值
	        results.append(" value=\"");
	        results.append(value);
	        results.append('"');
	        results.append(isXhtml() ? " />" : ">");
		}
		return results.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#renderFocusJavascript()
	 */
	protected String renderFocusJavascript() {
		StringBuffer results = new StringBuffer();

        results.append(lineEnd);
        results.append("<script type=\"text/javascript\"");
        if (!this.isXhtml() && this.scriptLanguage) {
            results.append(" language=\"JavaScript\"");
        }
        results.append(">");
        results.append(lineEnd);

        // xhtml script content shouldn't use the browser hiding trick
        if (!this.isXhtml()) {
            results.append("  <!--");
            results.append(lineEnd);
        }

        // Construct the control name that will receive focus.
        // This does not include any index.
        StringBuffer focusControl = new StringBuffer("document.forms[\"");
        focusControl.append(beanName);
        focusControl.append("\"].elements[\"");
        focusControl.append(this.focus);
        focusControl.append("\"]");

        results.append("  var focusControl = ");
        results.append(focusControl.toString());
        results.append(";");
        results.append(lineEnd);
        results.append(lineEnd);

        results.append("  if (focusControl && focusControl != \"" + beanName + "\" && focusControl.type != \"hidden\" && !focusControl.disabled) {");
        results.append(lineEnd);

        // Construct the index if needed and insert into focus statement
        String index = "";
        if (this.focusIndex != null) {
            StringBuffer sb = new StringBuffer("[");
            sb.append(this.focusIndex);
            sb.append("]");
            index = sb.toString();
        }
        //增加出错处理
        results.append("try {");
        results.append("     focusControl");
        results.append(index);
        results.append(".focus();");
        results.append(lineEnd);
        results.append("} catch(e) {}");
        
        results.append("  }");
        results.append(lineEnd);

        if (!this.isXhtml()) {
            results.append("  // -->");
            results.append(lineEnd);
        }

        results.append("</script>");
        results.append(lineEnd);
        return results.toString();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		super.release();
		applicationName = null;
		pageName = null;
		html = null;
		formBodyEnd = 0;
		siteFormServiceName = null;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#lookup()
	 */
	protected void lookup() throws JspException {
		try {
			super.lookup();
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.FormTag#initFormBean()
	 */
	protected void initFormBean() throws JspException {
		try {
			super.initFormBean();
		}
		catch(Exception e) {
			
		}
	}
	
	/**
	 * 获取表单
	 * @return
	 */
	private ActionForm getActionForm() {
		Object bean = null;
		try {
			bean = TagUtils.getInstance().lookup(pageContext, beanName, null, null);
		}
		catch(Exception e) {
			
		}
		if(bean==null) {
			try {
				String name = ((ActionConfig)TagUtils.getInstance().lookup(pageContext, "org.apache.struts.action.mapping.instance", null)).getName();
				bean = TagUtils.getInstance().lookup(pageContext, name, null, null);
				//pageContext.setAttribute(Constants.BEAN_KEY, bean, PageContext.REQUEST_SCOPE);
			}
			catch(Exception e) {
				
			}
		}
		return (bean==null || !(bean instanceof ActionForm) ? null :  (ActionForm)bean);
	}
	
	/**
	 * 是否XHTML
	 * @return
	 */
	private boolean isXhtml() {
		return TagUtils.getInstance().isXhtml(this.pageContext);
    }

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the formName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param formName the formName to set
	 */
	public void setPageName(String formName) {
		this.pageName = formName;
	}


	/**
	 * @return the applicationFormServiceName
	 */
	public String getSiteFormServiceName() {
		return siteFormServiceName;
	}


	/**
	 * @param applicationFormServiceName the applicationFormServiceName to set
	 */
	public void setSiteFormServiceName(String applicationFormServiceName) {
		this.siteFormServiceName = applicationFormServiceName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dialogPageService
	 */
	public DialogPageService getDialogPageService() {
		return dialogPageService;
	}

	/**
	 * @param dialogPageService the dialogPageService to set
	 */
	public void setDialogPageService(DialogPageService dialogPageService) {
		this.dialogPageService = dialogPageService;
	}
}