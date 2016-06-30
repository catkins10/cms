<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newEmployee() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/emp/employee.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openEmployee(empId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/emp/employee.shtml?id=<ext:write property="id"/>&employ.id=' + empId, 500, 290);
	}
</script>
<div style="padding-bottom:5px">
	<input type="button" class="button" value="添加员工" style="width:80px" onclick="newEmployee()">
</div>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="100px" nowrap="nowrap" class="tdtitle">姓名</td>
		<td width="120px" nowrap="nowrap" class="tdtitle">性别</td>
		<td width="160px" nowrap="nowrap" class="tdtitle">年龄</td>
		<td width="100px" nowrap="nowrap" class="tdtitle">出生日期</td>
		<td width="150px" nowrap="nowrap" class="tdtitle">电话</td>
		<td width="150px" nowrap="nowrap" class="tdtitle">联系地址</td>
		<td nowrap="nowrap" class="tdtitle">邮箱</td>
	</tr>
	<ext:iterate id="employ" indexId="employIndex" property="emps">
		<tr onclick="openEmployee('<ext:write name="employ" property="id"/>')" align="center">
			<td class="tdcontent" align="left"><ext:write name="employ" property="empname"/></td>
			<td class="tdcontent"><ext:write name="employ" property="sex"/></td>
			<td class="tdcontent"><ext:write name="employ" property="age"/></td>
			<td class="tdcontent"><ext:write name="employ" property="birthday"/></td>
			<td class="tdcontent"><ext:write name="employ" property="phone"/></td>
			<td class="tdcontent"><ext:write name="employ" property="address"/></td>
			<td class="tdcontent"><ext:write name="employ" property="email"/></td>
		</tr>
	</ext:iterate>
</table>