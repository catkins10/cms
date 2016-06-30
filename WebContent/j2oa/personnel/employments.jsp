<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newEmployment() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/employment.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openEmployment(employmentId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/employment.shtml?id=<ext:write property="id"/>&employment.id=' + employmentId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newEmployment()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="80px" nowrap="nowrap" class="tdtitle">开始时间</td>
		<td width="80px" nowrap="nowrap" class="tdtitle">结束时间</td>
		<td width="160px" nowrap="nowrap" class="tdtitle">所在单位</td>
		<td width="80px" nowrap="nowrap" class="tdtitle">职务</td>
		<td nowrap="nowrap" class="tdtitle">工作情况概述</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="employment" indexId="employmentIndex" property="employments">
		<tr onclick="openEmployment('<ext:write name="employment" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="employment" property="beginDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="employment" property="endDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="left"><ext:write name="employment" property="company"/></td>
			<td class="tdcontent"><ext:write name="employment" property="duty"/></td>
			<td class="tdcontent" align="left"><ext:write name="employment" property="description"/></td>
			<td class="tdcontent" align="left"><ext:write name="employment" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>