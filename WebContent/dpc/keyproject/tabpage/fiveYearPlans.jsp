<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newFiveYearPlan() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/fiveYearPlan.shtml?id=<ext:write property="id"/>', 500, 280);
}
function openFiveYearPlan(fiveYearPlanId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/fiveYearPlan.shtml?id=<ext:write property="id"/>&projectFiveYearPlan.id=' + fiveYearPlanId, 500, 280);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加五年计划" style="width:150px" onclick="newFiveYearPlan()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="100px">第几个五年</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">上个五年计划完成投资(万元)</td>
		<td class="tdtitle" nowrap="nowrap" width="180px">当前五年计划规划投资(万元)</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">当前五年计划工作目标</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="fiveYearPlan" property="fiveYearPlans">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("fiveYearPlan")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openFiveYearPlan('<ext:write name="fiveYearPlan" property="id"/>')">
				<td class="tdcontent" align="center"><ext:write name="fiveYearPlan" property="fiveYearPlanNumber"/></td>
				<td class="tdcontent" align="center"><ext:write name="fiveYearPlan" property="previousFiveYearInvest" format="#.######"/></td>
				<td class="tdcontent" align="center"><ext:write name="fiveYearPlan" property="currentFiveYearInvest" format="#.######"/></td>
				<td class="tdcontent"><ext:write name="fiveYearPlan" property="currentFiveYearObjective"/></td>
				<td class="tdcontent" align="center"><ext:write name="fiveYearPlan" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>