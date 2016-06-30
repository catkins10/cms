<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellpadding="1" cellspacing="0">
	<tr>
		<td>统计时间：</td>
		<td width="120px"><ext:field property="beginDate"/></td>
		<td>&nbsp;至&nbsp;</td>
		<td width="120px"><ext:field property="endDate"/></td>
		<td>&nbsp;&nbsp;<input type="button" class="button" value="确定" onclick="refreshView()"></td>
	</tr>
</table>