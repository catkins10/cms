<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="0" cellpadding="3" cellspacing="0">
	<col align="right">
	<col width="100%">
	<tr>
		<td nowrap="nowrap">分类：</td>
		<td><ext:field property="item.category"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">项目名称：</td>
		<td><ext:field property="item.name"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" valign="top">描述：</td>
		<td><ext:field property="item.description"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap">分值(百分制)：</td>
		<td><ext:field property="item.score" title="所有的项目分数加一块是100,如果等于0,使用平均值"/></td>
	</tr>
</table>