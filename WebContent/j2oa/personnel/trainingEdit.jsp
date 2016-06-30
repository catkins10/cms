<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>开始时间：</td>
		<td><ext:field property="training.beginDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">结束时间：</td>
		<td><ext:field property="training.endDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">培训内容：</td>
		<td><ext:field property="training.description"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="training.remark"/></td>
	</tr>
</table>