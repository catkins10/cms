<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>
<%
String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);
if(viewPackageName==null || viewPackageName.equals("")) {
	viewPackageName = "viewPackage";
}
%>
<table style="table-layout:fixed" class="view" width="<ext:write property="<%=viewPackageName + ".view.width"%>"/>" border="0" cellpadding="0" cellspacing="1">
	<ext:iterate id="column" indexId="columnIndex" property="<%=viewPackageName + ".view.columns"%>">
		<col width="<ext:write name="column" property="width"/>" align="<ext:write name="column" property="align"/>" class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>">
		<ext:notEmpty name="column" property="groupValues">
			<logic:iterate id="groupValue" name="column" property="groupValues" offset="1">
				<col valign="middle" width="<ext:write name="column" property="width"/>" class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>">
			</logic:iterate>
		</ext:notEmpty>
	</ext:iterate>
	<ext:equal value="1" property="<%=viewPackageName + ".view.headRowCount"%>">
		<tr height="23" align="center" valign="middle">
			<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
				<td align="center"  class="viewHeader">
					<logic:notEqual value="true" name="column" property="hideTitle"><ext:write name="column" property="title" filter="false"/></logic:notEqual>
					<logic:present name="<%=viewPackageName%>" property="sortColumn"><ext:equal name="column" property="name" nameCompare="<%=viewPackageName%>" propertyCompare="sortColumn"><img src="<%=request.getContextPath()%>/jeaf/form/img/sort_<logic:equal value="true" name="<%=viewPackageName%>" property="descendingSort">desc</logic:equal><logic:notEqual value="true" name="<%=viewPackageName%>" property="descendingSort">asc</logic:notEqual>.jpg"></ext:equal></logic:present>
				</td>
			</ext:iterate>
		</tr>
	</ext:equal>
	<ext:notEqual value="1" property="<%=viewPackageName + ".view.headRowCount"%>">
		<tr height="23" align="center" valign="middle">
			<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
				<ext:empty name="column" property="groupValues">
					<td class="viewData" align="center" rowspan="<ext:write property="<%=viewPackageName + ".view.headRowCount"%>"/>">
						<logic:notEqual value="true" name="column" property="hideTitle"><ext:write name="column" property="title" filter="false"/></logic:notEqual>
						<logic:present name="<%=viewPackageName%>" property="sortColumn"><ext:equal name="column" property="name" nameCompare="<%=viewPackageName%>" propertyCompare="sortColumn"><img src="<%=request.getContextPath()%>/jeaf/form/img/sort_<logic:equal value="true" name="<%=viewPackageName%>" property="descendingSort">desc</logic:equal><logic:notEqual value="true" name="<%=viewPackageName%>" property="descendingSort">asc</logic:notEqual>.jpg"></ext:equal></logic:present>
					</td>
				</ext:empty>
				<ext:notEmpty name="column" property="groupValues">
					<td class="viewData" align="center" colspan="<ext:write name="column" property="groupValuesSize"/>">
						<ext:write name="column" property="title"/>
						<logic:present name="<%=viewPackageName%>" property="sortColumn"><ext:equal name="column" property="name" nameCompare="<%=viewPackageName%>" propertyCompare="sortColumn"><img src="<%=request.getContextPath()%>/jeaf/form/img/sort_<logic:equal value="true" name="<%=viewPackageName%>" property="descendingSort">desc</logic:equal><logic:notEqual value="true" name="<%=viewPackageName%>" property="descendingSort">asc</logic:notEqual>.jpg"></ext:equal></logic:present>
					</td>
				</ext:notEmpty>
			</ext:iterate>
		</tr>
		<tr height="23" align="center" valign="middle">
			<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
				<ext:notEmpty name="column" property="groupValues">
					<logic:iterate id="groupValue" name="column" property="groupValues">
						<td class="viewData" align="center">
							<ext:write name="groupValue"/>
						</td>
					</logic:iterate>
				</ext:notEmpty>
			</ext:iterate>
		</tr>
	</ext:notEqual>
	<ext:iterate id="record" property="<%=viewPackageName + ".records"%>">
		<tr class="viewRow">
			<ext:empty name="record" property="statisticTitle">
				<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
					<ext:notEqual value="statistic" name="column" property="type">
						<td class="viewData" align="<ext:write name="column" property="align"/>"><ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/></td>
					</ext:notEqual>
					<ext:equal value="statistic" name="column" property="type">
						<ext:empty name="column" property="groupValues">
							<td class="viewData" align="<ext:write name="column" property="align"/>"><ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/></td>
						</ext:empty>
						<ext:notEmpty name="column" property="groupValues">
							<ext:notEmpty name="record" nameProperty="column" propertyProperty="name">
								<ext:iterate id="statisticValue" name="record" nameList="column" propertyList="name">
									<td class="viewData" align="<ext:write name="column" property="align"/>"><ext:write name="statisticValue"/></td>
								</ext:iterate>
							</ext:notEmpty>
							<ext:empty name="record" nameProperty="column" propertyProperty="name">
								<ext:iterate id="groupValue" name="column" property="groupValues"><td class="viewData"></td></ext:iterate>
							</ext:empty>
						</ext:notEmpty>
					</ext:equal>
				</ext:iterate>
			</ext:empty>
			<ext:notEmpty name="record" property="statisticTitle">
				<td class="viewData" colspan="<ext:write property="<%=viewPackageName + ".view.headColCount"%>"/>"><ext:write name="record" property="statisticTitle"/></td>
				<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>" propertyOffset="<%=viewPackageName + ".view.headColCount"%>">
					<ext:empty name="column" property="groupValues">
						<td class="viewData" align="<ext:write name="column" property="align"/>"><ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/></td>
					</ext:empty>
					<ext:notEmpty name="column" property="groupValues">
						<ext:notEmpty name="record" nameProperty="column" propertyProperty="name">
							<ext:iterate id="statisticValue" name="record" nameList="column" propertyList="name">
								<td class="viewData" align="<ext:write name="column" property="align"/>"><ext:write name="statisticValue"/></td>
							</ext:iterate>
						</ext:notEmpty>
						<ext:empty name="record" nameProperty="column" propertyProperty="name">
							<logic:iterate id="groupValue" name="column" property="groupValues">
								<td class="viewData" align="<ext:write name="column" property="align"/>">&nbsp;</td>
							</logic:iterate>
						</ext:empty>
					</ext:notEmpty>
				</ext:iterate>
			</ext:notEmpty>
		</tr>
	</ext:iterate>
</table>