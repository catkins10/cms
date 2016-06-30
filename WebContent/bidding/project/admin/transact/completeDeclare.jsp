<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	request.setAttribute("width", "380px");
	request.setAttribute("title", "完成报建");
	java.util.ArrayList buttons = new java.util.ArrayList();
	buttons.add(new com.yuanluesoft.jeaf.form.model.FormAction("完成", "FormUtils.doAction('completeDeclare')"));
	com.yuanluesoft.bidding.project.forms.admin.Project projectForm = (com.yuanluesoft.bidding.project.forms.admin.Project)request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY);
	buttons.add(new com.yuanluesoft.jeaf.form.model.FormAction("取消", "window.location='project.shtml?act=edit&id=" + projectForm.getId() + "&workItemId=" + projectForm.getWorkItemId() + "&req=" + System.currentTimeMillis() + "'"));

	request.setAttribute("buttons", buttons);
%>
<div style="width:380px">
	<br>
	<div style="font-size:14px">
		&nbsp;&nbsp;是否确定完成报建?
	</div>
	<br>
</div>