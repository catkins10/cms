<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.yuanluesoft.jeaf.view.model.ViewPackage" %>

<%String viewPackageName = (String)request.getAttribute(ViewPackage.VIEW_PACKAGE_NAME);%>
<table class="view" width="100%" border="0" cellpadding="0" cellspacing="1">
	<ext:iterate id="column" indexId="columnIndex" property="<%=viewPackageName + ".view.columns"%>">
		<col width="<ext:write name="column" property="width"/>" class="<ext:mod mod="2" value="0" name="columnIndex">viewColEven</ext:mod><ext:mod mod="2" value="1" name="columnIndex">viewColOdd</ext:mod>">
	</ext:iterate>
	<tr align="center" valign="middle">
		<ext:iterate id="column" property="<%=viewPackageName + ".view.columns"%>">
			<td align="center" class="viewHeader" id="<ext:write name="column" property="name"/>" nowrap>
				<ext:notEqual value="true" name="column" property="hideTitle"><ext:write name="column" property="title"/></ext:notEqual>
				<font style="font-size:1px">&nbsp;</font>
			</td>
		</ext:iterate>
	</tr>
	<ext:iterate id="record" indexId="recordIndex" property="<%=viewPackageName + ".records"%>">
		<tr id="<ext:write name="record" property="id"/>" style="cursor:pointer" onclick="onSelectData(this)" ondblclick="onDblClickData(this)">
			<ext:iterate id="column" indexId="columnIndex" property="<%=viewPackageName + ".view.columns"%>">
				<td align="<ext:write name="column" property="align"/>" class="viewData">
					<ext:viewField propertyViewPackage="<%=viewPackageName%>" nameColumn="column" nameRecord="record"/>
				</td>
			</ext:iterate>
		</tr>
	</ext:iterate>
</table>