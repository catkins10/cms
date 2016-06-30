<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newInvestComplete() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investComplete.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openInvestComplete(investCompleteId, completed) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/investComplete.shtml?id=<ext:write property="id"/>&investComplete.id=' + investCompleteId + (completed ? '&investComplete.completed=1' : ''), 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记投资完成情况"  onclick="newInvestComplete()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap">完成投资（万元）</td>
		<td class="tdtitle" nowrap="nowrap">年初至报告期累计完成投资（万元）</td>
		<td class="tdtitle" nowrap="nowrap">占年计划（%）</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">开工至报告期累计完成投资（万元）</td>
		<td class="tdtitle" nowrap="nowrap">占总投资（%）</td>
		<td class="tdtitle" nowrap="nowrap">待审核</td>
	</tr>
	<ext:iterate id="investComplete" property="investCompletes">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("investComplete")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" align="center">
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="completeYear"/></td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="completeMonthTitle"/></td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="completeInvest" format="#.####"/></td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="yearInvest" format="#.####"/></td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="percentage" format="#.##"/>%</td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="totalComplete" format="#.####"/></td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="completePercentage" format="#.##"/>%</td>
				<td class="tdcontent" onclick="openInvestComplete('<ext:write name="investComplete" property="id"/>')"><ext:write name="investComplete" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>