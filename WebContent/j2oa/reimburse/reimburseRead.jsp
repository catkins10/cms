<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%	request.setAttribute("chargeReadonly", "true"); %>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="50%">
	<col valign="middle">
	<col valign="middle" width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">报销人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="personName"/></td>
		<td class="tdtitle" nowrap="nowrap">部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="departmentName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报销类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
		<td class="tdtitle" nowrap="nowrap">出差路线</td>
		<td class="tdcontent"><ext:field writeonly="true" property="journey"/></td></td>
	</tr>
	<tr>
		<td class="tdtitle" class="tdtitle" nowrap="nowrap">开始时间</td>
		<td class="tdcontent" class="tdcontent"><ext:field writeonly="true" property="beginTime"/></td>
		<td class="tdtitle" class="tdtitle" nowrap="nowrap">结束时间</td>
		<td class="tdcontent" class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总天数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="dayCount"/></td>
		<td class="tdtitle" nowrap="nowrap">票据张数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="billCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top" style="padding-top:6px">事件描述</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总金额</td>
		<td class="tdcontent"><ext:field writeonly="true" property="money"/></td>
		<td class="tdtitle" nowrap="nowrap">大写</td>
		<td class="tdcontent"><ext:field writeonly="true" property="moneyCapital"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际支付金额</td>
		<td class="tdcontent"><ext:field writeonly="true" property="payMoney"/></td>
		<td class="tdtitle" nowrap="nowrap">大写</td>
		<td class="tdcontent"><ext:field writeonly="true" property="payMoneyCapital"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报销时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="reimburseDate"/></td>
		<td class="tdtitle" nowrap="nowrap">支付时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="payDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>