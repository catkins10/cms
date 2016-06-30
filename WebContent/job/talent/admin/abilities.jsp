<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">名称</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">描述</td>
	</tr>
	<ext:iterate id="ability" property="abilities">
		<tr>
			<td class="tdcontent"><ext:field writeonly="true" name="ability" property="name"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="ability" property="description"/></td>
		</tr>
	</ext:iterate>
</table>