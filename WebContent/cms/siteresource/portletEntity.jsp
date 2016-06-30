<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/savePortletEntity">
   	<table width="100%" border="0" cellpadding="2" cellspacing="0">
		<tr>
			<td nowrap="nowrap" align="right">分类：</td>
			<td width="100%"><ext:field property="category"/></td>
		</tr>
		<ext:notEqual value="create" property="act">
			<tr>
				<td nowrap="nowrap" align="right">名称：</td>
				<td><ext:field property="entityName"/></td>
			</tr>
		</ext:notEqual>
		<jsp:include page="/portal/portletPreferenceFields.jsp"/>
		<tr>
			<td nowrap="nowrap" align="right">描述：</td>
			<td><ext:field property="description"/></td>
		</tr>
		<ext:equal value="-1" property="siteId">
			<tr>
				<td nowrap="nowrap" align="right">访问者：</td>
				<td><ext:field property="entityVisitors.visitorNames" title="访问者"/></td>
			</tr>
		</ext:equal>
	</table>
</ext:form>