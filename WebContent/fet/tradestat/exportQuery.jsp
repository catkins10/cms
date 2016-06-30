<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/displayExportQuery" applicationName="fet/tradestat" pageName="statQuery">
	<table border="0" cellpadding="0" cellspacing="3">
		<tr>
			<td nowrap="nowrap">年度：</td>
			<td width="50px"><ext:field property="queryYear"/></td>
			<td nowrap="nowrap">&nbsp;月份：</td>
			<td width="50px"><ext:field property="queryMonth"/></td>
			<ext:notEqual value="100" name="SessionInfo" property="userType" scope="session">
				<td nowrap="nowrap">&nbsp;企业名称：</td>
				<td><ext:field property="queryCompany" style="width:80px"/></td>
			</ext:notEqual>
			<td>&nbsp;<ext:button name="查询" width="50" onclick="FormUtils.doAction('exportQuery')"/></td>
			<td>&nbsp;<ext:button name="导出数据" width="68" onclick="forms[0].action=forms[0].action.substring(0,forms[0].action.lastIndexOf('/'))+'/downloadExportData.shtml';forms[0].submit();"/></td>
			<td>
				&nbsp;&nbsp;
				<a href="importQuery.shtml?siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>" style="color:#2050ff">进口数据查询</a>
				<ext:equal value="100" name="SessionInfo" property="userType" scope="session">
					<a href="company.shtml?siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>" style="color:#2050ff">修改企业信息</a>
				</ext:equal>
				<ext:notEqual value="100" name="SessionInfo" property="userType" scope="session">
					<a href="companyQuery.shtml?siteId=<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>" style="color:#2050ff">企业信息查询</a>
				</ext:notEqual>
				<a href="javascript:DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/sso/changePassword.shtml', 430, 260);" style="color:#2050ff">密码修改</a>
			</td>
		</tr>
	</table>
   	<jsp:include page="/cms/view/viewPackage.jsp" />
   	<script>
   		document.title = "<ext:write property="viewPackage.view.title"/>" + (document.title=="" ? "" : " - " + document.title);
   	</script>
</ext:form>