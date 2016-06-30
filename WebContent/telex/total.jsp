<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doTotal">
   	<div style="font-weight: bold; padding-bottom: 3px">统计条件：</div>
   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">开始时间：</td>
			<td class="tdcontent"><ext:field property="beginDate"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">结束时间：</td>
			<td class="tdcontent"><ext:field property="endDate"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">发电单位：</td>
			<td class="tdcontent"><ext:field property="fromUnitName"/>
			</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">电报等级：</td>
			<td class="tdcontent"><ext:field property="selectedTelegramLevels"/></td>
		</tr>
	</table>
<%	if(!request.getMethod().toLowerCase().equals("get")) { %>
		<br>
	   	<ext:iterate id="totalBySecurityLevel" property="totals">
	   		<br>
	   		<div style="font-weight: bold; padding-bottom: 3px"><ext:write name="totalBySecurityLevel" property="securityLevel"/>：</div>
		   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		   		<tr align="center">
					<td class="tdtitle">电报类型</td>
					<td class="tdtitle">收/发电报数量</td>
					<td class="tdtitle">收/发电报份数</td>
					<td class="tdtitle">页数</td>
					<td class="tdtitle">总页数</td>
				</tr>
				<ext:iterate id="totalResult" name="totalBySecurityLevel" property="totals">
					<tr>
						<td class="tdtitle"><ext:write name="totalResult" property="type"/></td>
						<td class="tdcontent" align="center"><ext:write name="totalResult" property="count"/></td>
						<td class="tdcontent" align="center"><ext:write name="totalResult" property="unitCount"/></td>
						<td class="tdcontent" align="center"><ext:write name="totalResult" property="pages"/></td>
						<td class="tdcontent" align="center"><ext:write name="totalResult" property="unitPages"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:iterate>
<%		}%>
   	<br>
</ext:form>