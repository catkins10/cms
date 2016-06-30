<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newProgress() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/progress.shtml?id=<ext:write property="id"/>', 580, 350);
}
function openProgress(progressId, completed) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/progress.shtml?id=<ext:write property="id"/>&progress.id=' + progressId + (completed ? '&progress.completed=1' : ''), 580, 350);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="进度安排" style="width:150px" onclick="newProgress()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">安排</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">完成情况</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
		<td class="tdtitle" nowrap="nowrap"></td>
	</tr>
	<ext:iterate id="progress" property="progresses">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("progress")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" align="center">
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progressYear"/></td>
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progressMonthTitle"/></td>
				<td class="tdcontent" align="left" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="plan"/></td>
				<td class="tdcontent" align="left" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="progress"/></td>
				<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')"><ext:write name="progress" property="needApprovalTitle"/></td>
				<ext:equal value="1" name="progress" property="completed">
					<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')" nowrap="nowrap" style="padding:5px"></a></td>
				</ext:equal>
				<ext:notEqual value="1" name="progress" property="completed">
					<ext:equal value="true" name="editabled">
						<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>', true)" nowrap="nowrap" style="padding:5px">提交完成情况</a></td>
					</ext:equal>
					<ext:notEqual value="true" name="editabled">
						<td class="tdcontent" onclick="openProgress('<ext:write name="progress" property="id"/>')" nowrap="nowrap" style="padding:5px"></a></td>
					</ext:notEqual>
				</ext:notEqual>
			</tr>
<%		} %>
	</ext:iterate>
</table>