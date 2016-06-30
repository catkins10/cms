<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<body>
	<link href="<%=request.getContextPath()%>/cms/css/form.css" rel="stylesheet" type="text/css"/>
<%	request.setAttribute("formName", "portletPreferences");	%>
	<form action="<%=((javax.portlet.RenderResponse)response).createActionURL().toString()%>" method="post" name="<%=((javax.portlet.RenderResponse)response).getNamespace() + "form"%>" class="form" style="margin:0px;">
		<table border="0" cellpadding="3" cellspacing="0" width="100%">
			<jsp:include page="portletPreferenceFields.jsp"/>
			<tr>
				<td colspan="2" align="right" style="padding-right: 10px">
					<input type="button" class="button" value="保存" onclick="document.forms['<%=((javax.portlet.RenderResponse)response).getNamespace() + "form"%>'].submit();">
					<input type="button" class="button" value="取消" onclick="var form=document.forms['<%=((javax.portlet.RenderResponse)response).getNamespace() + "form"%>'];form.action='<%=((javax.portlet.RenderResponse)response).createRenderURL().toString()%>';form.submit();">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>