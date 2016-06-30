<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/doSendMessageByTel">
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>手机号码：</td>
			<td><ext:field property="receiverNumbers"/></td>
		</tr>
		<tr>
			<td>发送时间：</td>
			<td><ext:field property="sendTime"/></td>
		</tr>
		<tr>
			<td nowrap valign="top">短信内容：</td>
			<td><ext:field property="message"/></td>
		</tr>
	</table>
</ext:form>