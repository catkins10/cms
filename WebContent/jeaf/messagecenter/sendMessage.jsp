<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doSendMessage">
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000" width="100%">
		<col align="right">
		<col>
		<tr>
			<td>接收人：</td>
			<td><ext:field property="receivePersonNames"/></td>
		</tr>
		<tr>
			<td>发送时间：</td>
			<td><ext:field property="sendTime"/></td>
		</tr>
		<tr>
	        <td>优先级：</td>
			<td><ext:field property="priorityTitle"/></td>
		</tr>
	    <tr>
	        <td nowrap="nowrap">绑定发送方式：</td>
			<td><ext:field property="bindSendModeName"/></td>
		</tr>
		<tr>
			<td valign="top">内容：</td>
			<td><ext:field property="content"/></td>
	    </tr>
	    <tr>
		    <td>显示的链接：</td>
		    <td><ext:field property="webLink"/></td>
	    </tr>
	</table>
</ext:form>