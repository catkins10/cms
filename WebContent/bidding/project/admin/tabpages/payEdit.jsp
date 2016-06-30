<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位缴费金额</td>
		<td class="tdcontent"><ext:field property="pay.tendereeMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">中标单位缴费金额</td>
		<td class="tdcontent"><ext:field property="pay.pitchonMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">缴费时间</td>
		<td class="tdcontent"><ext:field property="pay.payTime"/></td>
	</tr>
</table>