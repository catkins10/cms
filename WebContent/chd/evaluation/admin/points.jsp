<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newPoint() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/point.shtml?id=<ext:write property="id"/>', 400, 300);
}
function openPoint(pointId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/point.shtml?id=<ext:write property="id"/>&point.id=' + pointId, 400, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加评价要点" onclick="newPoint()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('chd/evaluation', 'admin/point', '评价要点', 600, 380, 'detailId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">要点</td>
	</tr>
	<ext:iterate id="point" indexId="pointIndex" property="points">
		<tr style="cursor:pointer" valign="top" onclick="openPoint('<ext:write name="point" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="pointIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="point" property="point"/></td>
		</tr>
	</ext:iterate>
</table>