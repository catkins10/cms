<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newAnnualObjective() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/annualObjective.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openAnnualObjective(annualObjectiveId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/annualObjective.shtml?id=<ext:write property="id"/>&annualObjective.id=' + annualObjectiveId, 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加年度目标" onclick="newAnnualObjective()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="100px">年度</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">计划完成投资</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">已完成投资</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">形象进度目标</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="annualObjective" property="annualObjectives">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("annualObjective")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openAnnualObjective('<ext:write name="annualObjective" property="id"/>')" align="center">
				<td class="tdcontent"><ext:write name="annualObjective" property="objectiveYear"/></td>
				<td class="tdcontent"><ext:write name="annualObjective" property="investPlan" format="#.####"/></td>
				<td class="tdcontent"><ext:write name="annualObjective" property="investCompleted" format="#.####"/></td>
				<td class="tdcontent" align="left"><ext:write name="annualObjective" property="objective"/></td>
				<td class="tdcontent"><ext:write name="annualObjective" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>