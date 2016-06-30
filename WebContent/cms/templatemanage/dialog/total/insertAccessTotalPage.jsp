<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/insertIssueTotals">
	<script>
		function doOk() {
			var pageTitle = document.getElementsByName('pageTitle')[0].value;
			if(pageTitle=="") {
				alert("页面不能为空");
				return false;
			}
			var totalTitle = document.getElementsByName('totalTitle')[0].value;
			if(totalTitle=="") {
				alert("显示名称不能为空");
				return false;
			}
			DialogUtils.getDialogOpener().doAddTotalPage(pageTitle, document.getElementsByName('pageName')[0].value, totalTitle, document.getElementsByName('totalLink')[0].value, document.getElementsByName('recordId')[0].value);
			DialogUtils.closeDialog();
		}
		function afterPageSelected() {
			var totalTitle = document.getElementsByName('totalTitle')[0];
			if(totalTitle.value=="") {
				var pageTitle = document.getElementsByName('pageTitle')[0].value;
				totalTitle.value = pageTitle.substring(pageTitle.lastIndexOf('/') + 1);
			}
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">页面：</td>
			<td><ext:field property="pageTitle" onchange="afterPageSelected()"/></td>
		</tr>
		<tr>
			<td>记录ID：</td>
			<td><ext:field property="recordId"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">显示名称：</td>
			<td><ext:field property="totalTitle"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接地址：</td>
			<td><ext:field property="totalLink"/></td>
		</tr>
	</table>
</ext:form>