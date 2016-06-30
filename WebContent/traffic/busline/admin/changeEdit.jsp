<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td valign="top" nowrap="nowrap">变更说明：</td>
		<td><ext:field property="change.content"/></td>
	</tr>
	<tr>
		<td>临时变更：</td>
		<td><ext:field property="change.interim"  onclick="changeInterimMode()"/></td>
	</tr>
	<tr>
		<td>变更时间：</td>
		<td>
			<ext:field property="change.beginDate"/>
		</td>
	</tr>
	<tr id="trEndDate">
		<td style="<ext:notEqual property="change.interim" value="1">visibility:hidden;</ext:notEqual>">结束时间：</td>
		<td style="<ext:notEqual property="change.interim" value="1">visibility:hidden;</ext:notEqual>"><ext:field property="change.endDate"/></td>
	</tr>
</table>

<script>
	function changeInterimMode() {
		var tr = document.getElementById('trEndDate');
		tr.cells[0].style.visibility = tr.cells[1].style.visibility = document.getElementsByName('change.interim')[0].checked ? 'visible' : 'hidden';
	}
</script>