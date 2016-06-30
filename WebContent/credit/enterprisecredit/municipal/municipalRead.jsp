<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdcontent" ><ext:write property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">立案时间</td>
		<td class="tdcontent" ><ext:write property="filingTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">违法行为</td>
		<td class="tdcontent"><ext:write property="breakLaw"/></td>
		<td class="tdtitle" nowrap="nowrap">处罚法律依据</td>
		<td class="tdcontent"><ext:write property="basics"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">决定书文号</td>
		<td class="tdcontent"><ext:write property="bookNum"/></td>
		<td class="tdtitle" nowrap="nowrap">下达决定书时间</td>
		<td class="tdcontent"><ext:write property="bookTime"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">处罚金额</td>
		<td class="tdcontent"><ext:write property="money"/></td>
		<td class="tdtitle" nowrap="nowrap">办理机关</td>
		<td class="tdcontent"><ext:write property="deparment"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">结案时间</td>
		<td class="tdcontent"><ext:write property="endTime"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:write property="remark"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>