<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellpadding="1" cellspacing="0">
	<tr>
		<td>年度:</td>
		<td width="80px"><ext:field property="year"/></td>
		<td>&nbsp;</td>
		<td>分类:</td>
		<td width="120px"><ext:field property="category"/></td>
		<td>月</td>
		<td>&nbsp;<input type="button" class="button" value="确定" onclick="refreshView()"></td>
	</tr>
</table>