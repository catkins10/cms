<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newHonor() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/honor.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openHonor(honorId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/honor.shtml?id=<ext:write property="id"/>&honor.id=' + honorId, 500, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加企业荣誉" onclick="newHonor()" style="width:80px">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">年度</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">荣誉名称</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">授予单位</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">授予时间</td>
	</tr>
	<ext:iterate id="honor" indexId="honorIndex" property="honors">
		<tr style="cursor:pointer" valign="top" onclick="openHonor('<ext:write name="honor" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="honorIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:write name="honor" property="honorYear"/></td>
			<td class="tdcontent"><ext:write name="honor" property="honor"/></td>
			<td class="tdcontent"><ext:write name="honor" property="honorAwarding"/></td>
			<td class="tdcontent" align="center"><ext:write name="honor" property="honorDate" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>