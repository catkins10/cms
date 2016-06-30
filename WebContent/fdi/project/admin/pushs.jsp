<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newPush() {
	PageUtils.newrecord('fdi/project', 'admin/projectPush', 'mode=dialog,width=600,height=400', 'id=<ext:write property="id"/>');
}
function openPush(pushId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/fdi/project/admin/projectPush.shtml?id=<ext:write property="id"/>&push.id=' + pushId, 600, 400);
}
</script>
<ext:equal name="editabled" value="true">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记推进情况" onclick="newPush()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="32px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">经办人</td>
		<td class="tdtitle" nowrap="nowrap">洽谈与推进内容</td>
	</tr>
	<ext:iterate id="push" indexId="pushIndex" property="pushs">
		<tr style="cursor:pointer" valign="top" onclick="openPush('<ext:write name="push" property="id"/>')">
			<td class="tdcontent" align="center"><ext:writeNumber name="pushIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:field writeonly="true" name="push" property="pushTime"/></td>
			<td class="tdcontent" align="center"><ext:write name="push" property="transactor"/></td>
			<td class="tdcontent"><ext:write name="push" property="content"/></td>
		</tr>
	</ext:iterate>
</table>