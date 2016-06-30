<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newObjective() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/objective.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openObjective(objectiveId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/objective.shtml?id=<ext:write property="id"/>&objective.id=' + objectiveId, 500, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加年度目标" onclick="newObjective()" style="width:80px">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">年度</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">目标</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">完成情况</td>
	</tr>
	<ext:iterate id="objective" indexId="objectiveIndex" property="objectives">
		<tr style="cursor:pointer" valign="top" onclick="openObjective('<ext:write name="objective" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="objectiveIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:write name="objective" property="objectiveYear"/></td>
			<td class="tdcontent"><ext:write name="objective" property="objective"/></td>
			<td class="tdcontent"><ext:write name="objective" property="result"/></td>
		</tr>
	</ext:iterate>
</table>