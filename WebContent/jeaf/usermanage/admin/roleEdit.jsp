<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="roleName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">岗位</td>
		<td class="tdcontent"><ext:field property="isPost"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">成员列表</td>
		<td class="tdcontent"><ext:field property="memberNames"/></td>
	</tr>
</table>