<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/displayStatistics" applicationName="jeaf/stat" pageName="statistics">
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdContent" nowrap="nowrap">总访问量：</td>
		<td class="tdContent"><ext:write property="statistics.totalAccess"/></td>
	</tr>
	<ext:notEqual value="0" name="statistics" property="statistics.infoPublicAccess">
		<tr>
			<td class="tdContent" nowrap="nowrap">信息公开访问量：</td>
			<td><ext:write property="statistics.infoPublicAccess"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdContent" nowrap="nowrap">本月访问量：</td>
		<td  class="tdContent"><ext:write property="statistics.currentMonthAccess"/></td>
	</tr>
	<tr>
		<td class="tdContent" nowrap="nowrap">上月访问量：</td>
		<td class="tdContent"><ext:write property="statistics.previousMonthAccess"/></td>
	</tr>
	<tr>
		<td class="tdContent" nowrap="nowrap">今天访问量：</td>
		<td class="tdContent"><ext:write property="statistics.todayAccess"/></td>
	</tr>
	<tr>
		<td class="tdContent" nowrap="nowrap">昨天访问量：</td>
		<td class="tdContent"><ext:write property="statistics.yesterdayAccess"/></td>
	</tr>
	<tr>
		<td class="tdContent" nowrap>平均每天访问量：</td>
		<td class="tdContent"><ext:write property="statistics.averageAccess"/></td>
	</tr>
</table>
</ext:form>