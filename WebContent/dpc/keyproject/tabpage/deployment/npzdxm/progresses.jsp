<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newProgress() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/progress.shtml?id=<ext:write property="id"/>', 580, 320);
}
function openProgress(progressId, completed) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/progress.shtml?id=<ext:write property="id"/>&progress.id=' + progressId + (completed ? '&progress.completed=1' : ''), 580, 320);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加形象进度" style="width:150px" onclick="newProgress()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">年初至报告期形象进度</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="progress" property="progresses">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("progress")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" align="center">
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progressYear"/></td>
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progressMonth"/></td>
				<td class="tdcontent" align="left" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progress"/></td>
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>