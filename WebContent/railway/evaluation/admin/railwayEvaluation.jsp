<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellpadding="1" cellspacing="0">
	<tr>
		<td>岗位:</td>
		<td width="120px"><ext:field property="postNames"/></td>
		<td>&nbsp;部门:</td>
		<td width="120px"><ext:field property="orgNames"/></td>
		<td>&nbsp;</td>
		<td width="50px"><ext:field property="year"/></td>
		<td>年</td>
		<td width="36px"><ext:field property="month"/></td>
		<td>月</td>
		<td>&nbsp;<input type="button" class="button" value="确定" onclick="refreshView()"></td>
	</tr>
</table>