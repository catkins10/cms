<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" class="tdtitle">编号</td>
		<td class="tdcontent"><ext:field property="unitCode"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="tdtitle">机构或问题</td>
		<td class="tdcontent"><ext:field property="unit"/></td>
	</tr>
</table>