<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="width:400px">
	<table valign="middle" width="100%" style="table-layout:fixed" border="0" cellpadding="3" cellspacing="0" bordercolor="black">
		<col width="80px" align="right">
		<col width="100%">
		<tr>
			<td valign="top" style="padding-top:8px">撤销原因：</td>
			<td><ext:field property="undoReason"/></td>
		</tr>
		<tr>
			<td>已签收单位：</td>
			<td><ext:field property="resign"/></td>
		</tr>
	</table>
	<script>
		function doUnissue() {
			if(document.getElementsByName("undoReason")[0].value=="") {
				alert('撤销原因未填写。');
				return;
			}
			FormUtils.doAction('unissueDocument');
		}
		document.getElementsByName("resign")[0].checked = true;
	</script>
</div>