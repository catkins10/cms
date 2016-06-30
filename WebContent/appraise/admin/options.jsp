<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newOption() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/appraise/admin/option.shtml?id=<ext:write property="id"/>', 430, 280);
}
function openOption(optionId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/appraise/admin/option.shtml?id=<ext:write property="id"/>&option.id=' + optionId, 430, 280);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加选项" onclick="newOption()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap">选项名称</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">选项类型</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">短信选项</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">分值</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">是否弃权</td>
	</tr>
	<ext:iterate id="option" indexId="optionIndex" property="options">
		<tr valign="top" align="center" onclick="openOption('<ext:write name="option" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="optionIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="option" property="option"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="option" property="type"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="option" property="smsOption"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="option" property="score"/></td>
			<td class="tdcontent"><ext:equal value="1" name="option" property="abstain">√</ext:equal></td>
		</tr>
	</ext:iterate>
</table>