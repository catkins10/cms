<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newSpeak() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewSpeak.shtml?id=<ext:write property="id"/>', 600, 430);
}
function openSpeak(speakId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/interview/admin/interviewSpeak.shtml?id=<ext:write property="id"/>&interviewSpeak.id=' + speakId, 600, 430);
}
</script>
<div style="padding-bottom:8px">
	<input type="button" class="button" value="添加访谈发言" style="width:120px" onclick="newSpeak()">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="36px">序号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100px">发言人姓名</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">内容</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="110px">发布时间</td>
	</tr>
	<ext:iterate id="speak" indexId="speakIndex" property="interviewSpeaks">
		<tr style="cursor:pointer" align="center">
			<td class="tdcontent" onclick="openSpeak('<ext:write name="speak" property="id"/>')"><ext:writeNumber name="speakIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openSpeak('<ext:write name="speak" property="id"/>')"><ext:write name="speak" property="speaker"/></td>
			<td class="tdcontent" onclick="openSpeak('<ext:write name="speak" property="id"/>')" align="left"><ext:write name="speak" property="content" filter="false"/></td>
			<td class="tdcontent" onclick="openSpeak('<ext:write name="speak" property="id"/>')"><ext:write name="speak" property="publishTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>