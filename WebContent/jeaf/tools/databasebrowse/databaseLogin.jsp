<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/databaseBrowseLogin">
   	<table border="0" cellpadding="3" cellspacing="3" width="500px">
   		<col align="right">
   		<col width="100%">
   		<tr>
   			<td nowrap="nowrap">数据库URL：</td>
   			<td><ext:field property="jdbcURL"/></td>
   		</tr>
   		<tr>
   			<td nowrap="nowrap">数据库用户名：</td>
   			<td><ext:field property="jdbcUserName"/></td>
   		</tr>
   		<tr>
   			<td nowrap="nowrap">数据库密码：</td>
   			<td><ext:field property="jdbcPassword"/></td>
   		</tr>
   	</table>
</ext:form>