<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newDutyChange() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/dutyChange.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openDutyChange(dutyChangeId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/dutyChange.shtml?id=<ext:write property="id"/>&dutyChange.id=' + dutyChangeId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newDutyChange()">
	</div>
<%}%>

<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="100px" nowrap="nowrap" class="tdtitle">时间</td>
		<td width="100px" nowrap="nowrap" class="tdtitle">历史岗位</td>
		<td width="100px" nowrap="nowrap" class="tdtitle">现在岗位</td>
		<td nowrap="nowrap" class="tdtitle">岗位变动原因</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="dutyChange" indexId="dutyChangeIndex" property="dutyChanges">
		<tr onclick="openDutyChange('<ext:write name="dutyChange" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="dutyChange" property="changeDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="dutyChange" property="previousDuty"/></td>
			<td class="tdcontent"><ext:write name="dutyChange" property="newDuty"/></td>
			<td class="tdcontent" align="left"><ext:write name="dutyChange" property="changeReason"/></td>
			<td class="tdcontent" align="left"><ext:write name="dutyChange" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>