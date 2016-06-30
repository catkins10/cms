<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
window.onload = function() {
	if(document.getElementsByName('adjust.beginTime')[0].value=='') {
		DateTimeField.setValue('adjust.beginTime', 'hour', '0');
		DateTimeField.setValue('adjust.beginTime', 'minute', '0');
		DateTimeField.setValue('adjust.beginTime', 'second', '0');
	}
	if(document.getElementsByName('adjust.endTime')[0].value=='') {
		DateTimeField.setValue('adjust.endTime', 'hour', '23');
		DateTimeField.setValue('adjust.endTime', 'minute', '59');
		DateTimeField.setValue('adjust.endTime', 'second', '59');
	}
}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
	<col width="90px" align="right">
	<col width="100%">
	<tr>
		<td>开始时间：</td>
		<td><ext:field property="adjust.beginTime"/></td>
	</tr>
	<tr>
		<td>结束时间：</td>
		<td><ext:field property="adjust.endTime"/></td>
	</tr>
</table>