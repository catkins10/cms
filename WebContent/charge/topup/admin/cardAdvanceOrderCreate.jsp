<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<script>
function calculateCardNumer() {
	var cardNumber = Number(document.getElementsByName("cardNumber")[0].value);
	if(cardNumber + "" != "NaN" && cardNumber>0) {
		return;
	}
	var money = Number(document.getElementsByName("money")[0].value);
	if(money + "" == "NaN" || money<=0) {
		return;
	}
	var cardMoney = Number(document.getElementsByName("cardMoney")[0].value);
	if(cardMoney + "" == "NaN" || cardMoney<=0) {
		return;
	}
	document.getElementsByName("cardNumber")[0].value = Math.ceil(money/cardMoney);
}
</script>
<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="120px" class="tdtitle">
	<col valign="middle" width="100%" class="tdcontent">
	<tr>
		<td class="tdtitle" nowrap="nowrap">运营商</td>
		<td class="tdcontent">
			<html:hidden property="carrierId"/>
			<ext:field property="carrierName" selectOnly="true" styleClass="field required" itemsProperty="carriers" itemValueProperty="id" itemTitleProperty="carrierName" valueField="carrierId" titleField="carrierName"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">金额</td>
		<td class="tdcontent"><html:text property="money" styleClass="field required" onchange="calculateCardNumer()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡名称</td>
		<td class="tdcontent"><html:text property="cardName" styleClass="field required"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡面额</td>
		<td class="tdcontent"><html:text property="cardMoney" styleClass="field required" onchange="calculateCardNumer()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">充值卡数量</td>
		<td class="tdcontent"><html:text property="cardNumber" styleClass="field required"/></td>
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
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><html:text property="remark"/></td>
	</tr>
</table>