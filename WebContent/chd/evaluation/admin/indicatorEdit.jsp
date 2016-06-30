<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table  width="100%" border="0" cellpadding="0" cellspacing="3">
	<col valign="middle" align="right">
	<col valign="middle" width="100%">
	<ext:equal value="0" property="indicator.sourceRecordId">
		<tr>
			<td nowrap="nowrap">指标名称：</td>
			<td><ext:field property="indicator.name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">单位：</td>
			<td><ext:field property="indicator.unit"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="0" property="indicator.sourceRecordId">
		<tr>
			<td nowrap="nowrap">指标名称：</td>
			<td><ext:field writeonly="true" property="indicator.name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">单位：</td>
			<td><ext:field writeonly="true" property="indicator.unit"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td nowrap="nowrap">备注：</td>
		<td><ext:field property="indicator.remark"/></td>
	</tr>
</table>