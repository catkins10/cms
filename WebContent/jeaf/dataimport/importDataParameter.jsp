<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="80%" style="margin: 30px;" align="center" border="0" cellpadding="3" cellspacing="3">
	<col align="right" style="font-weight: bold">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">目标站点：</td>
		<td><ext:field writeonly="true" property="targetSiteName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据导入服务名称：</td>
		<td><ext:field writeonly="true" property="dataImportServiceName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据导入服务类名称：</td>
		<td><ext:field property="dataImportServiceClass" readonly="true"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">JDBC驱动：</td>
		<td><ext:field property="parameter.jdbcDriver"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">JDBC URL：</td>
		<td><ext:field property="parameter.jdbcURL"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据库用户名：</td>
		<td><ext:field property="parameter.jdbcUserName"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">数据库密码：</td>
		<td><ext:field property="parameter.jdbcPassword"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">源站点URL：</td>
		<td><ext:field property="parameter.sourceSiteURL"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">模板路径：</td>
		<td><ext:field property="parameter.templatePath"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">模板URL：</td>
		<td><ext:field property="parameter.templateUrl"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">最大导入的记录数：</td>
		<td><ext:field property="parameter.maxImport"/></td>
	</tr>
	<tr>
		<td></td>
		<td align="center"><input type="button" class="button" value="下一步"  onclick="FormUtils.doAction('saveParameter')"></td>
	</tr>
</table>