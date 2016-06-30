<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.yuanluesoft.jeaf.view.sqlview.model.SqlViewPackage;"%>

<%
String viewPackageName = (String)request.getAttribute(SqlViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>

<table style="table-layout:fixed" class="view" width="<ext:write property="<%=viewPackageName + ".view.width"%>"/>" border="0" cellpadding="0" cellspacing="1">
	<tr align="center" valign="middle">
		<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
			<td align="center" class="viewHeader" <ext:equal value="0" name="column" property="width">style="display:none"</ext:equal> width="<ext:write name="column" property="width"/>"><ext:notEqual value="true" name="column" property="hideTitle"><ext:write name="column" property="title" /></ext:notEqual></td>
		</ext:iterate>
	</tr>
	<ext:iterate id="record" property="<%=viewPackageName + ".records"%>">
		<tr valign="top" class="viewRow">
			<ext:iterate id="column" indexId="columnIndex" property="<%=viewPackageName + ".view.columns"%>">
				<td class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>" align="<ext:write name="column" property="align"/>">
					<a <ext:notEmpty name="column" property="link">target="<ext:write name="column" property="link.target"/>" href="标记已失效<ext:link name="record" nameLink="column" propertyLink="link"/>"</ext:notEmpty>>
						<ext:equal value="rownum" name="column" property="type">
							<ext:write property="<%=viewPackageName + ".rowNum"%>" />
						</ext:equal>
						<ext:equal value="field" name="column" property="type">
							<ext:write  nameEllipsis="column" propertyEllipsis="ellipsis" name="record" nameMapKey="column" propertyMapKey="name" nameFormat="column" propertyFormat="format" />
						</ext:equal>
					</a>
				</td>
			</ext:iterate>
		</tr>
	</ext:iterate>
</table>