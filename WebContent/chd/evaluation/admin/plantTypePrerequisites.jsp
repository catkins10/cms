<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newPrerequisites() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/prerequisites.shtml?id=<ext:write property="id"/>', 550, 300);
}
function openPrerequisites(prerequisitesId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/prerequisites.shtml?id=<ext:write property="id"/>&evaluationPrerequisites.id=' + prerequisitesId, 550, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加必备条件" onclick="newPrerequisites()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('chd/evaluation', 'admin/prerequisites', '必备条件', 600, 380, 'plantTypeId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">必备条件</td>
	</tr>
	<ext:iterate id="prerequisites" indexId="prerequisitesIndex" property="prerequisites">
		<tr style="cursor:pointer" valign="top" onclick="openPrerequisites('<ext:write name="prerequisites" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="prerequisitesIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="prerequisites" property="prerequisites"/></td>
		</tr>
	</ext:iterate>
</table>