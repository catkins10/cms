<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newIndicator() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/indicator.shtml?id=<ext:write property="id"/>', 430, 300);
}
function openIndicator(indicatorId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/chd/evaluation/admin/indicator.shtml?id=<ext:write property="id"/>&indicator.id=' + indicatorId, 430, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加指标" onclick="newIndicator()">
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('chd/evaluation', 'admin/indicator', '指标', 600, 380, 'plantTypeId=<ext:write property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">指标名称</td>
		<td class="tdtitle" nowrap="nowrap" width="100">单位</td>
	</tr>
	<ext:iterate id="indicator" indexId="indicatorIndex" property="indicators">
		<tr style="cursor:pointer" valign="top" onclick="openIndicator('<ext:write name="indicator" property="id"/>')" align="left">
			<td class="tdcontent" align="center"><ext:writeNumber name="indicatorIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="indicator" property="name"/></td>
			<td class="tdcontent" align="center"><ext:write name="indicator" property="unit"/></td>
		</tr>
	</ext:iterate>
</table>