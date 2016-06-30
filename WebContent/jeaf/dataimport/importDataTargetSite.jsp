<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="80%" style="margin: 30px;" align="center" border="0" cellpadding="3" cellspacing="3">
	<col align="right" style="font-weight: bold">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">目标站点：</td>
		<td><ext:field property="targetSiteName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据导入服务：</td>
		<td><ext:field property="dataImportServiceName"/></td>
	</tr>
	<tr>
		<td></td>
		<td align="center"><input type="button" class="button" value="下一步"  onclick="FormUtils.doAction('importData')"></td>
	</tr>
</table>