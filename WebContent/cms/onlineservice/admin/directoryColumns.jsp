<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newDirectoryColumn() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/directoryColumn.shtml?id=<ext:write property="id"/>&directoryType=<ext:write property="directoryType"/>', 360, 200);
}
function openDirectoryColumn(directoryColumnId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/onlineservice/admin/directoryColumn.shtml?id=<ext:write property="id"/>&directoryColumn.id=' + directoryColumnId + '&directoryType=<ext:write property="directoryType"/>', 360, 200);
}
</script>
<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加栏目" onclick="newDirectoryColumn()">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">名称</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">栏目</td>
	</tr>
	<ext:iterate id="directoryColumn" indexId="directoryColumnIndex" property="directoryColumns">
		<tr valign="top" onclick="openDirectoryColumn('<ext:write name="directoryColumn" property="id"/>')">
			<td class="tdcontent" align="center"><ext:writeNumber name="directoryColumnIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="directoryColumn" property="name"/></td>
			<td class="tdcontent"><ext:write name="directoryColumn" property="columnName"/></td>
		</tr>
	</ext:iterate>
</table>