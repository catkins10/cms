<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">选项名称：</td>
		<td><ext:field property="option.name" title="如：好、较好、一般、差"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">描述：</td>
		<td><ext:field property="option.description"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">选中时对应的分数：</td>
		<td><ext:field property="option.score" title="百分制"/></td>
	</tr>
</table>