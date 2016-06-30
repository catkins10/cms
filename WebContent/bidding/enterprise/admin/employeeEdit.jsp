<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onEmployeeTypeChanged() {
		var isPermanent = document.getElementsByName("employee.isPermanent")[0].checked;
		var tryRows = "tryEndDateRow,depositRow,damageRow".split(",");
		var permanentRows = "saleRow".split(",");
		for(var i=0; i<tryRows.length; i++) {
			document.getElementById(tryRows[i]).style.display = isPermanent ? 'none' : '';
		}
		for(var i=0; i<permanentRows.length; i++) {
			document.getElementById(permanentRows[i]).style.display = isPermanent ? '' : 'none';
		}
	}
	function writeKey() {
		if(document.getElementsByName("employee.ekeyNO")[0].value=="") {
			alert("EKEY编号不能为空");
			return;
		}
		var enterpriseName = '<ext:write property="employee.enterpriseName"/>';
		var enterpriseId = '<ext:write property="employee.enterpriseId"/>';
		var employeeLoginName = document.getElementsByName("employee.loginName")[0].value;
		var employeeId = '<ext:write property="employee.id"/>';
		var keyId = document.getElementById("KeyWriter").writeKey(enterpriseName, enterpriseId, employeeLoginName, employeeId);
		if(keyId!='') {
			document.getElementsByName('employee.ekeyId')[0].value = keyId;
			FormUtils.doAction('writeKey');
		}
	}
	function reclaim() {
		if(!confirm('是否确定回收EKEY？')) {
			return;
		}
		document.getElementsByName("employee.ekeyNO")[0].value = "";
		document.getElementsByName("employee.ekeyId")[0].value = "";
		FormUtils.submitForm();
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
	<tr>
		<td align="right" width="80px">用户姓名：</td>
		<td><ext:field property="employee.name"/></td>
	</tr>
	<tr>
		<td align="right" width="80px">登录用户名：</td>
		<td><ext:field property="employee.loginName"/></td>
	</tr>
	<tr>
		<td align="right">登录密码：</td>
		<td><ext:field property="employee.password"/></td>
	</tr>
	<tr>
		<td align="right">是否停用：</td>
		<td><ext:field property="employee.halt"/></td>
	</tr>
	<tr>
		<td align="right">联系电话：</td>
		<td><ext:field property="employee.tel"/></td>
	</tr>
	<tr>
		<td align="right">备注：</td>
		<td><ext:field property="employee.remark"/></td>
	</tr>
</table>
<html:hidden property="employee.isPermanent" value="1"/>
<object classid="clsid:6BFFD153-8E38-4D84-B711-954EB50DCD0E" id="KeyWriter" style="width:0;height:0"></object>
<script language="jscript" FOR="KeyWriter" event="writeError(errorDescription)">alert(errorDescription)</script>