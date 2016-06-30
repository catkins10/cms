<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveMailbox.shtml">
	<script>
		function doOK() {
			if(document.getElementsByName("mailboxName")[0].value=='') {
				alert('邮箱名称不能为空。');
				return;
			}
			FormUtils.submitForm();
		}
	</script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="80px" valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr valign="middle">
			<td>邮箱名称：</td>
			<td><ext:field property="mailboxName"/></td>
		</tr>
	</table>
</ext:form>