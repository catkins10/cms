<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>借阅人：</td>
		<td><ext:field property="borrow.personName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">借阅时间：</td>
		<td><ext:field property="borrow.borrowTime"/></td>
	</tr>
	<ext:notEqual value="1" property="borrow.isReturned">
		<tr>
			<td nowrap="nowrap">拟归还时间：</td>
			<td><ext:field property="borrow.returnTime"/></td>
		</tr>
	</ext:notEqual>
	<ext:equal value="1" property="borrow.isReturned">
		<tr>
			<td nowrap="nowrap">归还时间：</td>
			<td><ext:field writeonly="true" property="borrow.returnTime"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td>备注：</td>
		<td><ext:field property="borrow.remark"/></td>
	</tr>
</table>