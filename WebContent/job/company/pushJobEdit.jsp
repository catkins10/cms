<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">邮件标题：</td>
		<td><ext:field property="mailSubject"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">邮件内容：</td>
		<td><ext:field property="mailContent"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">推送范围：</td>
		<td>
			<table border="0" width="100%" cellspacing="0" cellpadding="0">
				<tr>
					<td nowrap="nowrap"><ext:field property="range" onclick="document.getElementById('tdReceivers').style.display=document.getElementsByName('range')[0].checked ? 'none' : '';"/></td>
					<td id="tdReceivers" style="padding-left:5px; display:none;"><ext:field property="jobPush.receivers"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>