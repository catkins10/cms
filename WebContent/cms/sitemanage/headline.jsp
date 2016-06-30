<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveHeadline">
	<script type="text/javascript">
		function doOK() {
			if(document.getElementsByName("headlineName")[0].value=="") {
				alert("标题不能为空");
				return;
			}
			if(document.getElementsByName("headlineURL")[0].value=="") {
				alert("链接不能为空");
				return;
			}
			FormUtils.submitForm();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="50px" valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr valign="middle">
			<td>站点：</td>
			<td><ext:field property="selectedDirectoryIds"/></td>
		</tr>
		<tr valign="middle">
			<td>标题：</td>
			<td><ext:field property="headlineName"/></td>
		</tr>
		<tr valign="middle">
			<td>链接：</td>
			<td><ext:field property="headlineURL"/></td>
		</tr>
		<tr>
			<td valign="top">概述：</td>
			<td><ext:field property="summarize"/></td>
		</tr>
	</table>
</ext:form>