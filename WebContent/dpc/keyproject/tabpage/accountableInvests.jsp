<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newAccountableInvest() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/accountableInvest.shtml?id=<ext:write property="id"/>', 500, 300);
}
function openAccountableInvest(investId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/accountableInvest.shtml?id=<ext:write property="id"/>&projectAccountableInvest.id=' + investId, 500, 300);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加项目总投资（责任制）" onclick="newAccountableInvest()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="150px">资金来源</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">资金金额</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">来源说明</td>
		<td class="tdtitle" nowrap="nowrap"l width="50px">待审核</td>
	</tr>
	<ext:iterate id="invest" property="accountableInvests">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("invest")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openAccountableInvest('<ext:write name="invest" property="id"/>')">
				<td class="tdcontent"><ext:write name="invest" property="fullSource"/></td>
				<td class="tdcontent"><ext:write name="invest" property="amount"/></td>
				<td class="tdcontent"><ext:write name="invest" property="remark"/></td>
				<td class="tdcontent" align="center"><ext:write name="invest" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>