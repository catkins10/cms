<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveResponseMessageNews">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
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
			<td nowrap="nowrap" valign="top">描述：</td>
			<td><ext:field property="messageNews.description"/></td>
		</tr>
	</table>
</ext:form>