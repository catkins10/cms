<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newStageProgress() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/stageProgress.shtml?id=<ext:write property="id"/>', 600, 400);
}
function openStageProgress(stageProgressId, completed) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/stageProgress.shtml?id=<ext:write property="id"/>&stageProgress.id=' + stageProgressId + (completed ? '&stageProgress.completed=1' : ''), 600, 400);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="关键节点进度安排" style="width:150px" onclick="newStageProgress()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="180px">节点名称</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">完成时限</td>
		<td class="tdtitle" nowrap="nowrap" width="160px">责任单位</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">责任人</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">安排</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">完成情况</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
		<td class="tdtitle" nowrap="nowrap"></td>
	</tr>
	<ext:iterate id="stageProgress" property="stageProgresses">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("stageProgress")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" align="center">
				<td class="tdcontent" align="left" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="stage"/></td>
				<td class="tdcontent" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="timeLimit"/></td>
				<td class="tdcontent" align="left" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="responsibleUnit"/></td>
				<td class="tdcontent" align="left" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="responsiblePerson"/></td>
				<td class="tdcontent" align="left" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="plan"/></td>
				<td class="tdcontent" align="left" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="progress"/></td>
				<td class="tdcontent" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')"><ext:write name="stageProgress" property="needApprovalTitle"/></td>
				<ext:equal value="1" name="stageProgress" property="completed">
					<td class="tdcontent" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')" nowrap="nowrap" style="padding:5px"></a></td>
				</ext:equal>
				<ext:notEqual value="1" name="stageProgress" property="completed">
					<ext:equal value="true" name="editabled">
						<td class="tdcontent" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>', true)" nowrap="nowrap" style="padding:5px">提交完成情况</a></td>
					</ext:equal>
					<ext:notEqual value="true" name="editabled">
						<td class="tdcontent" onclick="openStageProgress('<ext:write name="stageProgress" property="id"/>')" nowrap="nowrap" style="padding:5px"></a></td>
					</ext:notEqual>
				</ext:notEqual>
			</tr>
<%		} %>
	</ext:iterate>
</table>