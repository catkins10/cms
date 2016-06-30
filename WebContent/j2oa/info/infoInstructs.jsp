<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newInstruct() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/info/instruct.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openInstruct(instructId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/info/instruct.shtml?id=<ext:write property="id"/>&instruct.id=' + instructId, 500, 300);
}
</script>
<ext:equal value="true" property="magazineEditor">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加批示" onclick="newInstruct()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">领导姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">领导级别</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">批示内容</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">批示时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">录入人</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">录入时间</td>
	</tr>
	<ext:iterate id="instruct" indexId="instructIndex" property="instructs">
		<tr valign="top" align="center" onclick="openInstruct('<ext:write name="instruct" property="id"/>')" style="cursor: pointer">
			<td class="tdcontent"><ext:writeNumber name="instructIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="instruct" property="leader"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="level"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="instruct" property="instruct"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="instructTime"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="creator"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="created"/></td>
		</tr>
	</ext:iterate>
</table>