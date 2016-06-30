<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/report/saveEnsureColumnConfig">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">栏目：</td>
			<td><ext:field property="columnType" onclick="onColumnTypeChanged()"/></td>
		</tr>
		<tr id="trColumnNames">
			<td></td>
			<td id="trColumnNames"><ext:field property="columnNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">计分方式：</td>
			<td><ext:field property="mode"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">分数：</td>
			<td><ext:field property="score"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">信息抓取分数：</td>
			<td><ext:field property="captureScore"/></td>
		</tr>
	</table>
</ext:form>

<script>
	function onColumnTypeChanged() {
		document.getElementById("trColumnNames").style.display = document.getElementsByName("columnType")[0].checked ? "" : "none";
	}
	onColumnTypeChanged();
</script>