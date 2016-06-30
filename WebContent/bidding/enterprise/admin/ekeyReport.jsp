<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/writeEKeyReport" target="_blank" method="get">
   	<table border="0" cellpadding="3" cellspacing="0" width="100%">
   		<tr>
   			<td width="80px" nowrap="nowrap" align="right">开始日期：</td>
   			<td width="100%"><ext:field property="beginDate"/></td>
   		</tr>
   		<tr>
   			<td width="80px" nowrap="nowrap" align="right">结束日期：</td>
   			<td width="100%"><ext:field property="endDate"/></td>
   		</tr>
   	</table>
</ext:form>