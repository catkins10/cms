<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col valign="middle" width="36px" align="right">
	<col valign="middle" width="100%">
	<tr>
		<td nowrap>类别</td>
		<td><ext:field onchange="getChargeStandard()" property="charge.chargeCategory"/></td>
	</tr>
	<tr>
		<td nowrap>标准</td>
		<td><ext:field property="charge.chargeStandard" readonly="true"/></td>
	</tr>
	<tr>
		<td nowrap>时间</td>
		<td><ext:field property="charge.time"/></td>
	</tr>
	<tr>
		<td nowrap>金额</td>
		<td><ext:field property="charge.money"/></td>
	</tr>
	<tr>
		<td nowrap>用途</td>
		<td><ext:field property="charge.purpose"/></td>
	</tr>
	<tr>
		<td nowrap>备注</td>
		<td><ext:field property="charge.remark"/></td>
	</tr>
	<tr>
		<td nowrap>票据</td>
		<td><ext:field property="charge.bills"/></td>
	</tr>
</table>
<script>
function getChargeStandard() {
	ScriptUtils.appendJsFile(document, "getChargeStandard.shtml?chargeCategory=" + StringUtils.utf8Encode(document.getElementsByName("charge.chargeCategory")[0].value) + "&seq=" + Math.random(), "scriptGetChargeStandard");
}
function afterGetChargeStandard(standard) {
	if(standard!="NOSESSIONINFO") {
		document.getElementsByName("charge.chargeStandard")[0].value = standard;
	}
}
</script>