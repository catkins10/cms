<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<script>
function topUp() {
	var selectServicePrices = document.getElementsByName("selectServicePrice");
	if(selectServicePrices && selectServicePrices.length>0) {
		var ids = "";
		for(var i=0; i<selectServicePrices.length; i++) {
			if(selectServicePrices[i].checked) {
				ids += (ids=="" ? "" : ",") + selectServicePrices[i].value;
			}
		}
		if(ids=="") {
			alert("选择充值卡关联的服务.");
			return;
		}
		document.getElementsByName("selectedServicePriceIds")[0].value = ids;
	}
	FormUtils.submitForm();	
}
</script>
<table border="0" cellpadding="3" cellspacing="3" style="color:#000000; width:430px" align="center">
	<col align="right" style="font-weight: bold;">
	<col width="100%">
	<tr>
		<td nowrap>用户名：</td>
		<td><ext:write name="SessionInfo" property="userName" scope="session"/></td>
	</tr>
	<tr>
		<td nowrap>充值卡名称：</td>
		<td><ext:write property="cardOrder.cardName"/></td>
	</tr>
	<tr>
		<td nowrap>充值金额:：</td>
		<td><ext:write property="cardOrder.cardMoney"/></td>
	</tr>
	<ext:notEmpty property="servicePricesForPerson">
		<tr>
			<ext:equal value="3" property="cardOrder.serviceSelect">
				<td nowrap valign="top" style="padding-top: 8px">充值卡关联的服务：</td>
				<td>
					<ext:iterate id="servicePrice" property="servicePricesForPerson">
						<div style="padding-top:5px"><ext:write name="servicePrice" property="title"/></div>
					</ext:iterate>
				</td>
			</ext:equal>
			<ext:notEqual value="3" property="cardOrder.serviceSelect">
				<ext:sizeEqual name="cardTopUp" property="servicePricesForPerson" value="1">
					<td nowrap valign="top" style="padding-top: 8px">充值卡关联的服务：</td>
					<td>
						<ext:iterate id="servicePrice" property="servicePricesForPerson">
							<div style="padding-top:5px"><ext:write name="servicePrice" property="title"/></div>
						</ext:iterate>
					</td>
				</ext:sizeEqual>
				<ext:sizeNotEqual name="cardTopUp" property="servicePricesForPerson" value="1">
					<td nowrap valign="top" style="padding-top: 8px">选择充值卡关联的服务：</td>
					<td>
						<ext:iterate id="servicePrice" property="servicePricesForPerson">
							<div style="padding-top:5px">
								<ext:equal value="1" property="cardOrder.serviceSelect"><input type="radio" class="radio" value="<ext:write name="servicePrice" property="id"/>" name="selectServicePrice"/></ext:equal>
								<ext:equal value="2" property="cardOrder.serviceSelect"><input type="checkbox" class="checkbox" value="<ext:write name="servicePrice" property="id"/>" name="selectServicePrice"/></ext:equal>
								<ext:write name="servicePrice" property="title"/>
							</div>
						</ext:iterate>
					</td>
				</ext:sizeNotEqual>
			</ext:notEqual>
		</tr>
	</ext:notEmpty>
	<tr>
		<td colspan="2" align="center">
			<br>
			<input type="button" value="充值" class="button" onclick="topUp()">
		</td>
	</tr>
</table>
<html:hidden property="cardNumber"/>
<html:hidden property="cardPassword"/>
<html:hidden property="selectedServicePriceIds"/>