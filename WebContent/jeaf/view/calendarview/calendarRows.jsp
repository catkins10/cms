<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table cellpadding="0" cellspacing="0" border="0">
	<ext:iterate id="record" name="CALENDAR_DATA">
		<tr valign="middle" height="15px">
			<td nowrap>
				<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
					<ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/>
				</ext:iterate>
			</td>
		</tr>
	</ext:iterate>
</table>