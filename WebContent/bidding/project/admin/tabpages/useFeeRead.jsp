<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">场地费金额</td>
		<td class="tdcontent"><ext:field writeonly="true" property="useFee.money"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">开票时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="useFee.billingTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">收款时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="useFee.payTime"/></td>
	</tr>
</table>