<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveSendMessageNews">
	<table border="0" width="<ext:equal value="0" property="receiveMessageId">800px</ext:equal>" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">标题：</td>
			<td><ext:field property="messageNews.title"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接：</td>
			<td><ext:field property="messageNews.url"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">图片：</td>
			<td><ext:field property="messageNews.image"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">封面：</td>
			<td><ext:field property="messageNews.showCoverPic"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" valign="top">描述：</td>
			<td><ext:field property="messageNews.description"/></td>
		</tr>
		<ext:equal value="0" property="receiveMessageId">
			<tr>
				<td nowrap="nowrap" valign="top">内容：</td>
				<td><ext:field property="messageNews.content"/></td>
			</tr>
		</ext:equal>
	</table>
</ext:form>