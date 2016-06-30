<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定职业</td>
		<td class="tdcontent"><ext:field property="profession"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定类型</td>
		<td class="tdcontent"><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">鉴定级别</td>
		<td class="tdcontent"><ext:field property="level"/></td>
	</tr>
</table>