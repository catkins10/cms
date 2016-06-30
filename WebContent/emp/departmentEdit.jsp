<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<tr>
		<td class="tdtitle" nowrap="nowrap">部门名称</td>
		<td class="tdcontent"><ext:field property="departName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
</table>
