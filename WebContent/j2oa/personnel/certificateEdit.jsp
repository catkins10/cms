<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<col align="right">
	<col width="100%">
	<tr>
		<td>获取时间：</td>
		<td><ext:field property="certificate.issueDate"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">证书或者资质名称：</td>
		<td><ext:field property="certificate.name"/></td>
	</tr>
	<tr>
		<td>备注：</td>
		<td><ext:field property="certificate.remark"/></td>
	</tr>
</table>