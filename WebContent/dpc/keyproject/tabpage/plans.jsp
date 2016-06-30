<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newPlan() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/plan.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openPlan(planId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/plan.shtml?id=<ext:write property="id"/>&projectPlan.id=' + planId, 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加工作安排" style="width:150px" onclick="newPlan()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">项目参建单位新进场</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">工作安排及需要调解决的意见</td>
		<td class="tdtitle" nowrap="nowrap"l width="50px">待审核</td>
	</tr>
	<ext:iterate id="plan" property="plans">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("plan")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openPlan('<ext:write name="plan" property="id"/>')">
				<td class="tdcontent" align="center"><ext:write name="plan" property="planYear"/></td>
				<td class="tdcontent" align="center"><ext:write name="plan" property="planMonth"/></td>
				<td class="tdcontent"><ext:write name="plan" property="enterUnit"/></td>
				<td class="tdcontent"><ext:write name="plan" property="plan"/></td>
				<td class="tdcontent" align="center"><ext:write name="plan" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>