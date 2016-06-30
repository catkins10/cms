<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/generateDocWord">
	<script>
		function doOK() {
			var parameter = "docMark=" + StringUtils.utf8Encode(document.getElementsByName("docMark")[0].value);
			parameter += "&markYear=" + StringUtils.utf8Encode(document.getElementsByName("markYear")[0].value);
			parameter += "&markSequence=" + StringUtils.utf8Encode(document.getElementsByName("markSequence")[0].value);
			DialogUtils.getDialogOpener().setTimeout("FormUtils.doAction('generateDocWord', '" + parameter + "')", 1);
			DialogUtils.closeDialog();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="80px" valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr valign="middle">
			<td>机关代字：</td>
			<td><ext:field property="docMark"/></td>
		</tr>
		<tr valign="middle">
			<td>发文年度：</td>
			<td><ext:field property="markYear"/></td>
		</tr>
		<tr>
			<td>发文编号：</td>
			<td><ext:field property="markSequence"/></td>
		</tr>
	</table>
</ext:form>