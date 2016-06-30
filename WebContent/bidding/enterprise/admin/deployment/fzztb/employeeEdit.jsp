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
		<td align="right" width="100px">用户姓名：</td>
		<td><ext:field property="employee.name"/></td>
		<td align="right" width="100px">用户类型：</td>
		<td><ext:field property="employee.isPermanent" onclick="onEmployeeTypeChanged()"/></td>
	</tr>
	<tr id="tryEndDateRow" style="<ext:equal value="1" property="employee.isPermanent">display:none</ext:equal>">
		<td align="right">试用截止时间：</td>
		<td><ext:field property="employee.tryEndDate"/></td>
	</tr>
	<tr>
		<td align="right">登录用户名：</td>
		<td><ext:field property="employee.loginName"/></td>
		<td align="right">登录密码：</td>
		<td><ext:field property="employee.password"/></td>
	</tr>
	<tr>
		<td align="right">EKEY编号：</td>
		<td><ext:field property="employee.ekeyNO"/></td>
		<td align="right">领取软件时间：</td>
		<td><ext:field property="employee.drawTime"/></td>
	</tr>
	<tr>
		<td align="right">经办人：</td>
		<td><ext:field property="employee.enterpriseTransactor"/></td>
		<td align="right">联系电话：</td>
		<td><ext:field property="employee.tel"/></td>
	</tr>
	<tr>
		<td align="right">合同编号：</td>
		<td><ext:field property="employee.contractNo"/></td>
		<td align="right">收据编号：</td>
		<td><ext:field property="employee.receiptNo"/></td>
	</tr>
	<tr id="depositRow" style="<ext:equal value="1" property="employee.isPermanent">display:none</ext:equal>">
		<td align="right">已收押金：</td>
		<td><ext:field property="employee.deposit"/></td>
		<td align="right">押金收据编号：</td>
		<td><ext:field property="employee.depositBill"/></td>
	</tr>
	<tr id="damageRow" style="<ext:equal value="1" property="employee.isPermanent">display:none</ext:equal>">
		<td align="right">损坏内容：</td>
		<td><ext:field property="employee.damage"/></td>
		<td align="right">已收赔偿金额：</td>
		<td><ext:field property="employee.damageMoney"/></td>
	</tr>
	<tr id="saleRow" style="<ext:notEqual value="1" property="employee.isPermanent">display:none</ext:notEqual>">
		<td align="right">已收销售款：</td>
		<td><ext:field property="employee.saleMoney"/></td>
		<td align="right">发票编号：</td>
		<td><ext:field property="employee.saleBill"/></td>
	</tr>
	<tr>
		<td align="right">备注：</td>
		<td colspan="3"><ext:field property="employee.remark"/></td>
	</tr>
</table>
<object classid="clsid:6BFFD153-8E38-4D84-B711-954EB50DCD0E" id="KeyWriter" style="width:0;height:0"></object>
<script language="jscript" FOR="KeyWriter" event="writeError(errorDescription)">alert(errorDescription)</script>