<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newLevel() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/level.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openLevel(levelId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/level.shtml?id=<ext:write property="id"/>&level.id=' + levelId, 500, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加评价等级" onclick="newLevel()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('chd/evaluation', 'admin/level', '评价等级', 600, 380, 'companyId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="150px">等级</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">描述</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">奖励</td>
	</tr>
	<ext:iterate id="level" indexId="levelIndex" property="levels">
		<tr style="cursor:pointer" valign="top" onclick="openLevel('<ext:write name="level" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="levelIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:write name="level" property="level"/></td>
			<td class="tdcontent"><ext:write name="level" property="description"/></td>
			<td class="tdcontent"><ext:write name="level" property="award"/></td>
		</tr>
	</ext:iterate>
</table>