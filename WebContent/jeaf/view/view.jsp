<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>

<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table style="table-layout:fixed" class="view" width="<ext:write property="<%=viewPackageName + ".view.width"%>"/>" border="0" cellpadding="0" cellspacing="1">
	<ext:iterate id="column" indexId="columnIndex" property="<%=viewPackageName + ".view.columns"%>">
		<col width="<ext:write name="column" property="width"/>" class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>">
	</ext:iterate>
	<tr align="center" valign="middle">
		<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
			<td align="center" class="viewHeader" style="<ext:equal value="0" name="column" property="width">display:none;</ext:equal><ext:notEmpty name="column" property="name">cursor:pointer;</ext:notEmpty>" <%if(!"true".equals(request.getAttribute("sortDisabled"))) {%> <ext:notEmpty name="column" property="name">onclick="sort('<%=viewPackageName%>', '<ext:write name="column" property="name"/>');"</ext:notEmpty><%}%>>
				<ext:equal value="select" name="column" property="type">
					<input class="checkbox" type="checkbox" id="<%=viewPackageName%>.selectAllBox" onclick="selectAll('<%=viewPackageName%>')">
				</ext:equal>
				<ext:notEqual value="select" name="column" property="type">
					<ext:notEqual value="true" name="column" property="hideTitle">
						<ext:write name="column" property="title" filter="false"/>
					</ext:notEqual>
					<ext:present property="<%=viewPackageName + ".sortColumn"%>">
						<ext:equal name="column" property="name" propertyCompare="<%=viewPackageName + ".sortColumn"%>">
							<span class="<ext:equal value="true" property="<%=viewPackageName + ".descendingSort"%>">viewSortDesc</ext:equal><ext:notEqual value="true" property="<%=viewPackageName + ".descendingSort"%>">viewSortAsc</ext:notEqual>"></span>
						</ext:equal>
					</ext:present>
				</ext:notEqual>
			</td>
		</ext:iterate>
	</tr>
	<ext:iterate id="record" property="<%=viewPackageName + ".records"%>">
		<tr valign="middle">
			<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
				<td align="<ext:write name="column" property="align"/>" class="viewData">
					<ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/>
				</td>
			</ext:iterate>
		</tr>
	</ext:iterate>
</table>
<script>initViewSelectBox("<%=viewPackageName%>")</script>