<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/captureColumn">
	<script>
		function doOk() {
			DialogUtils.getDialogOpener().addCaptureColumn(document.getElementsByName('id')[0].value, document.getElementsByName('key')[0].value, document.getElementsByName('columnIds')[0].value, document.getElementsByName('columnNames')[0].value, document.getElementsByName('issue')[0].checked);
			DialogUtils.closeDialog();
		}
		function doRemove() {
			DialogUtils.getDialogOpener().removeCaptureColumn(document.getElementsByName('id')[0].value);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr>
			<td nowrap="nowrap">关 键 字：</td>
			<td><ext:field property="key"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">栏目选择：</td>
			<td><ext:field property="columnNames"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">直接发布：</td>
			<td><ext:field property="issue"/></td>
		</tr>
	</table>
</ext:form>