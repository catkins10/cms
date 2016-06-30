<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newEmployee() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/employee.shtml?id=<ext:write property="id"/>' + "&employee.enterpriseName=" + StringUtils.utf8Encode('<ext:write property="name"/>'), 600, 360);
}
function openEmployee(employeeId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/employee.shtml?id=<ext:write property="id"/>&employee.id=' + employeeId, 600, 360);
}
</script>
<div style="padding-bottom:8px">
	<input type="button" class="button" value="注册用户" style="width:90px" onclick="newEmployee()">
</div>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100px">用户姓名</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100%">登录用户名</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="100px">永久性用户</td>
		<td align="center" class="tdtitle" nowrap="nowrap" width="160px">试用截止时间</td>
	</tr>
	<ext:iterate id="employee" property="employees">
		<tr style="cursor:pointer" align="center">
			<td class="tdcontent" onclick="openEmployee('<ext:write name="employee" property="id"/>')" align="left"><ext:write name="employee" property="name"/></td>
			<td class="tdcontent" onclick="openEmployee('<ext:write name="employee" property="id"/>')"><ext:write name="employee" property="loginName"/></td>
			<td class="tdcontent" onclick="openEmployee('<ext:write name="employee" property="id"/>')"><ext:equal value="1" name="employee" property="isPermanent">是</ext:equal><ext:equal value="0" name="employee" property="isPermanent">否</ext:equal></td>
			<td class="tdcontent" onclick="openEmployee('<ext:write name="employee" property="id"/>')"><ext:equal value="0" name="employee" property="isPermanent"><ext:write name="employee" property="tryEndDate" format="yyyy-MM-dd"/></ext:equal></td>
		</tr>
	</ext:iterate>
</table>