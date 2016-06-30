<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="120px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡名称</td>
		<td class="tdcontent"><html:text property="cardName" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡数量</td>
		<td class="tdcontent"><ext:write property="cardNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡面额</td>
		<td class="tdcontent"><ext:write property="cardMoney"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务绑定</td>
		<td class="tdcontent">
			<html:hidden property="servicePriceIds"/>
			<ext:field property="servicePriceNames" selectOnly="true" onSelect="DialogUtils.openSelectDialog('charge/servicemanage', 'selectServicePrice', 760, 430, true, 'servicePriceIds{id},servicePriceNames{title|服务包|100%}')"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">服务超过一个时</td>
		<td class="tdcontent">
			<html:radio property="serviceSelect" value="1" styleClass="radio" styleId="serviceSingleSelect"/><label for="paymentEnalbe">&nbsp;单选</label>
			<html:radio property="serviceSelect" value="2" styleClass="radio" styleId="serviceMultiSelect"/><label for="paymentDisable">&nbsp;多选</label>
			<html:radio property="serviceSelect" value="3" styleClass="radio" styleId="serviceAllSelect"/><label for="paymentDisable">&nbsp;全选</label>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">允许增值消费</td>
		<td class="tdcontent">
			<html:radio property="paymentEnable" value="1" styleClass="radio" styleId="paymentEnalbe"/><label for="paymentEnalbe">&nbsp;允许</label>
			<html:radio property="paymentEnable" value="0" styleClass="radio" styleId="paymentDisable"/><label for="paymentDisable">&nbsp;禁止</label>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值截至日期</td>
		<td class="tdcontent"><ext:field property="topUpEnd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><html:text property="remark"/></td>
	</tr>
</table>