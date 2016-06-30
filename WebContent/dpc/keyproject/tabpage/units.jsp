<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newUnit() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/unit.shtml?id=<ext:write property="id"/>', 680, 390);
}
function openUnit(unitId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/unit.shtml?id=<ext:write property="id"/>&projectUnit.id=' + unitId, 680, 390);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加参建单位" style="width:150px" onclick="newUnit()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">单位类型</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">参建单位名称</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">资质等级</td>
		<td class="tdtitle" nowrap="nowrap" width="150px">承担合同段名称</td>
		<td class="tdtitle" nowrap="nowrap">合同金额（万元）</td>
		<td class="tdtitle" nowrap="nowrap">合同开始时间</td>
		<td class="tdtitle" nowrap="nowrap">合同结束时间</td>
		<td class="tdtitle" nowrap="nowrap">待审核</td>
	</tr>
	<ext:iterate id="unit" property="units">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("unit")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openUnit('<ext:write name="unit" property="id"/>')">
				<td class="tdcontent" align="center"><ext:write name="unit" property="type"/></td>
				<td class="tdcontent"><ext:write name="unit" property="name"/></td>
				<td class="tdcontent" align="center"><ext:write name="unit" property="qualificationLevel"/></td>
				<td class="tdcontent"><ext:write name="unit" property="partName"/></td>
				<td class="tdcontent" align="center"><ext:write name="unit" property="contractAmount"/></td>
				<td class="tdcontent" align="center"><ext:write name="unit" property="contractBegin" format="yyyy-MM-dd"/></td>
				<td class="tdcontent" align="center"><ext:write name="unit" property="contractEnd" format="yyyy-MM-dd"/></td>
				<td class="tdcontent" align="center"><ext:write name="unit" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>