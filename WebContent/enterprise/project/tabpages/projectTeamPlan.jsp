<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newTeamPlan(teamId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectTeamPlan.shtml?id=<ext:write property="id"/>&plan.teamId=' + teamId, 640, 430);
	}
	function openTeamPlan(planId, teamId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectTeamPlan.shtml?id=<ext:write property="id"/>&plan.teamId=' + teamId + '&plan.id=' + planId, 640, 430);
	}
</script>
<ext:equal value="true" name="projectTeam" property="manager">
	<ext:empty name="projectTeam" property="completionDate">
		<div style="padding-bottom:5px">
			<input type="button" class="button" value="添加安排" style="width:80px" onclick="newTeamPlan('<ext:write name="projectTeam" property="id"/>')">
		</div>
	</ext:empty>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="180px">项目组成员</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">工作内容</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="88px">计划开始时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="88px">计划结束时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="88px">实际完成时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="200px">实际完成情况</td>
	</tr>
	<ext:iterate id="teamPlan" name="projectTeam" property="plans">
		<tr style="cursor:pointer" align="center" valign="top" onclick="openTeamPlan('<ext:write name="teamPlan" property="id"/>', '<ext:write name="teamPlan" property="teamId"/>')">
			<td class="tdcontent" align="left"><ext:write name="teamPlan" property="memberNames"/></td>
			<td class="tdcontent" align="left"><ext:write name="teamPlan" property="workContent"/></td>
			<td class="tdcontent"><ext:write name="teamPlan" property="beginDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="teamPlan" property="endDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="teamPlan" property="completionDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="left"><ext:write name="teamPlan" property="completionDescription"/></td>
		</tr>
	</ext:iterate>
</table>