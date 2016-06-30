<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
	String years = "" + year;
	for(int i=year-1; i>=year-10; i--) {
		years += "\\0" + i;
	}
%>
<ext:form action="/dispalyCompanyQuery" applicationName="fet/tradestat" pageName="statQuery">
	<table border="0" cellpadding="0" cellspacing="3">
		<tr>
			<td nowrap="nowrap">&nbsp;企业名称：</td>
			<td width="120px"><html:text property="queryCompany"/></td>
			<td>&nbsp;<ext:button name="查询" width="50" onclick="FormUtils.doAction('companyQuery')"/></td>
			<td>
				&nbsp;&nbsp;
				<a href="exportQuery.shtml?siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>" style="color:#2050ff">出口数据查询</a>
				<a href="importQuery.shtml?siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>" style="color:#2050ff">进口数据查询</a>
			</td>
		</tr>
	</table>
   	<jsp:include page="/cms/view/viewPackage.jsp" />
   	<script>
   		document.title = "<ext:write property="viewPackage.view.title"/>" + (document.title=="" ? "" : " - " + document.title);
   	</script>
</ext:form>