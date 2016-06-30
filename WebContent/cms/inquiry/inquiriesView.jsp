<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>

<ext:iterate id="record" property="<%=viewPackageName + ".records"%>">
	<a href="" onclick="window.open('inquirySubject.shtml?id=<ext:write name="record" property="id" />', 'inquiry', 'width=380,height=500,left=100,top=50,scrollbars=yes,status=no,resizable=yes,toolbar=no,menubar=no,location=no', false);return false;"><ext:write name="record" property="subject"/></a>
</ext:iterate>