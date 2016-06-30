<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">角色名称</td>
		<td class="tdcontent"><ext:field property="role"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">默认的人员列表</td>
		<td class="tdcontent"><ext:field property="roleMemberNames"/></td>
	</tr>
</table>