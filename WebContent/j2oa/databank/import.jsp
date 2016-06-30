<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doImport">
   	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">源目录</td>
			<td class="tdcontent"><ext:field property="systemDirectory" title="服务器能访问到的目录"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">资料库目录</td>
			<td class="tdcontent"><ext:field property="databankDirectoryName"/></td>
		</tr>
	</table>
</ext:form>