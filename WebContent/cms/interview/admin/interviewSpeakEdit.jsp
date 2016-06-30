<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="2px" style="table-layout:fixed">
	<tr>
		<td width="80px" align="right">发言人姓名：</td>
		<td><ext:field property="interviewSpeak.speaker"/></td>
	</tr>
	<tr>
		<td align="right">发言人IP：</td>
		<td><ext:field property="interviewSpeak.speakerIP"/></td>
	</tr>
	<tr>
		<td align="right">联系方式：</td>
		<td><ext:field property="interviewSpeak.speakerContacts"/></td>
	</tr>
	<tr>
		<td align="right">发言时间：</td>
		<td><ext:field property="interviewSpeak.speakTime"/></td>
	</tr>
	<tr>
		<td align="right">发布时间：</td>
		<td><ext:field property="interviewSpeak.publishTime"/></td>
	</tr>
	<tr>
		<td align="right" valign="top">内容：</td>
		<td><ext:field property="interviewSpeak.content"/></td>
	</tr>
</table>