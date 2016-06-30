<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function openContract(contractId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/project/projectContract.shtml?id=<ext:write property="id"/>&openerTabPage=contractApproval&contract.id=' + contractId, 640, 450);
}
</script>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100px">合同编号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">合同名称</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">合同金额(元)</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="70px">创建时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="70px">盖章时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="70px">签订时间</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="70px">存档时间</td>
	</tr>
	<ext:iterate id="contract" property="contracts">
		<tr style="cursor:pointer" align="center" valign="top" onclick="openContract('<ext:write name="contract" property="id"/>')">
			<td class="tdcontent"><ext:write name="contract" property="contractNo"/></td>
			<td class="tdcontent" align="left"><ext:write name="contract" property="contractName"/></td>
			<td class="tdcontent"><ext:write name="contract" property="contractValue" format="#.##"/></td>
			<td class="tdcontent"><ext:write name="contract" property="created" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="contract" property="sealDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="contract" property="signDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="contract" property="archiveDate" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>