<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>

<script>
function onPaymentModeChange(value) {
	var trDeductDay = document.getElementById("trDeductDay");
	var trDeductPolicy = document.getElementById("trDeductPolicy");
	if(value=="一次性付款" || value=="按天") {
		trDeductDay.style.display = "none";
		trDeductPolicy.style.display = "none";
	}
	else {
		trDeductDay.style.display = "";
		trDeductPolicy.style.display = (document.getElementsByName("orderTimeDeduct")[0].checked ? "none" : "");
		document.getElementById("deductDayCustomByMonth").style.display = (value!="按月" ? "none" : "");
		document.getElementById("deductDayCustomByWeek").style.display = (value!="按周" ? "none" : "");
		document.getElementById("deductDayCustomByYear").style.display = (value!="按年" ? "none" : "");
	}
}
function onOrderTimeDeduct(value) { //扣款时间就是订购时间
	document.getElementById("trDeductPolicy").style.display = (value ? "none" : "");
	document.getElementById("deductDayCustom").style.display = (value ? "none" : "");
}
function onDeductPolicyChange(value) {
	document.getElementById("deductPolicyCustom").style.display = (value=="day" ? "none" : "");
}
function onPolicyComparisonChange(value){//策略比较方式变动
	document.getElementById("dayRange").style.display = (value!="介于" ? "none" : "");
}
function deletePolicy(policyId){//删除策略
	document.getElementsByName("policyId")[0].value=policyId;
	FormUtils.doAction('deleteDeductPolicy');
}
</script>

<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="90px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<col valign="middle" width="80px" class="tdtitle">
	<col valign="middle" width="50%" class="tdcontent">
	<tr>
		<td>名称</td>
		<td colspan="3"><html:text property="name"/></td>
	</tr>
	<tr>
		<td>服务名称</td>
		<td colspan="3"><ext:write property="service.serviceName"/></td>
	</tr>
	<tr>
		<td>订购方式</td>
		<td colspan="3">
			<html:multibox styleClass="checkbox" property="orderModesArray" value="carrier" styleId="orderModeCarrier"/>&nbsp;<label for="orderModeCarrier">电信运营商</label>&nbsp;
			<html:multibox styleClass="checkbox" property="orderModesArray" value="card" styleId="orderModeCard"/>&nbsp;<label for="orderModeCard">充值卡</label>&nbsp;
			<html:multibox styleClass="checkbox" property="orderModesArray" value="web" styleId="orderModeWeb"/>&nbsp;<label for="orderModeWeb">网上订购</label>&nbsp;
		</td>
	</tr>
	<tr>
		<td>计费方式</td>
		<td colspan="3">
			<html:radio onclick="onPaymentModeChange(value)" styleClass="radio" property="paymentMode" value="按天" styleId="paymentModeByDay"/>&nbsp;<label for="paymentModeByDay">按天</label>&nbsp;
			<html:radio onclick="onPaymentModeChange(value)" styleClass="radio" property="paymentMode" value="按周" styleId="paymentModeByWeek"/>&nbsp;<label for="paymentModeByWeek">按周</label>&nbsp;
			<html:radio onclick="onPaymentModeChange(value)" styleClass="radio" property="paymentMode" value="按月" styleId="paymentModeByMonth"/>&nbsp;<label for="paymentModeByMonth">按月</label>&nbsp;
			<html:radio onclick="onPaymentModeChange(value)" styleClass="radio" property="paymentMode" value="按年" styleId="paymentModeByYear"/>&nbsp;<label for="paymentModeByYear">按年</label>&nbsp;
			<html:radio onclick="onPaymentModeChange(value)" styleClass="radio" property="paymentMode" value="一次性付款" styleId="paymentModeOnce"/>&nbsp;<label for="paymentModeOnce">一次性付款</label>&nbsp;
		</td>
	</tr>
	<tr>
		<td>计费周期</td>
		<td>
			<html:text property="paymentPeriod" />
		</td>
		<td>价格(元)</td>
		<td>
			<html:text property="servicePrice"/>
		</td>
	</tr>
	<tr>
		<td>自动重复计费</td>
		<td colspan="3">
			<html:radio styleClass="radio" property="reiteration" value="1" styleId="reiterationYesBox"/>&nbsp;<label for="reiterationYesBox">是</label>&nbsp;
			<html:radio styleClass="radio" property="reiteration" value="0" styleId="reiterationNoBox"/>&nbsp;<label for="reiterationNoBox">否</label>&nbsp;
		</td>
	</tr>
	<tr id="trDeductDay" style="<ext:equal property="paymentMode" value="一次性付款">display:none;</ext:equal><ext:equal property="paymentMode" value="按天">display:none;</ext:equal>">
		<td>扣费时间</td>
		<td id="tdDeductDay" colspan="3">
			<html:hidden property="deductDay"/>
			<div style="float:left; padding-top:5px">
				<html:radio onclick="onOrderTimeDeduct(true)" styleClass="radio" property="orderTimeDeduct" value="true" styleId="orderTimeDeductYes"/>&nbsp;<label for="orderTimeDeductYes">订购时间</label>&nbsp;
				<html:radio onclick="onOrderTimeDeduct(false)" styleClass="radio" property="orderTimeDeduct" value="false" styleId="orderTimeDeductNo"/>&nbsp;<label for="orderTimeDeductNo">指定时间</label>&nbsp;
				&nbsp;
			</div>
<%			String monthDayItems = null;
			for(int i=1; i<=28; i++) {
				monthDayItems = (monthDayItems==null ? "" : monthDayItems + "\\n") + i + "日|" + i;
			}%>
			<div id="deductDayCustom" style="float:left;<ext:equal property="orderTimeDeduct" value="true">display:none;</ext:equal>">
				<span id="deductDayCustomByMonth" style="<ext:notEqual property="paymentMode" value="按月">display:none;</ext:notEqual>">
					<ext:field property="deductDayOfMonth" style="width:80px" selectOnly="true" itemsText="<%=monthDayItems%>" itemTitleProperty="deductDayOfMonth" itemValueProperty="deductDay"/>
				</span>
				<span id="deductDayCustomByWeek" style="<ext:notEqual property="paymentMode" value="按周">display:none;</ext:notEqual>">
					<ext:field property="deductDayOfWeek" style="width:80px" selectOnly="true" itemsText="星期一|1\n星期二|2\n星期三|3\n星期四|4\n星期五|5\n星期六|6\n星期日|0" itemTitleProperty="deductDayOfWeek" itemValueProperty="deductDay"/>
				</span>
				<span id="deductDayCustomByYear" style="<ext:notEqual property="paymentMode" value="按年">display:none;</ext:notEqual>">
					<div style="float:left">
						<ext:field property="deductMonthOfYear" style="width:80px" selectOnly="true" itemsText="1月|1\n2月|2\n3月|3\n4月|4\n5月|5\n6月|6\n7月|7\n8月|8\n9月|9\n10月|10\n11月|11\n12月|12"/>
					</div>
					<div style="float:left">&nbsp;</div>
					<div style="float:left">
						<ext:field property="deductDayOfYear" style="width:80px" selectOnly="true" itemsText="<%=monthDayItems%>"/>
					</div>
				</span>
			</div>
		</td>
	</tr>
	<tr id="trDeductPolicy" style="<ext:equal property="orderTimeDeduct" value="true">display:none;</ext:equal><ext:equal property="paymentMode" value="一次性付款">display:none;</ext:equal><ext:equal property="paymentMode" value="按天">display:none;</ext:equal>">
		<td>计费策略</td>
		<td colspan="3">
			<div style="float:left;padding-top:6px">
				<html:radio onclick="onDeductPolicyChange(value)" styleClass="radio" property="deductPolicy" value="day" styleId="day"/>&nbsp;<label for="day">按实际使用天数</label>&nbsp;
				<html:radio onclick="onDeductPolicyChange(value)" styleClass="radio" property="deductPolicy" value="custom" styleId="custom"/>&nbsp;<label for="custom">自定义</label>&nbsp;
			</div>
			<span id="deductPolicyCustom" style="<ext:equal property="deductPolicy" value="day">display:none;</ext:equal>">
				<div style="float:left;padding-top:6px">使用天数&nbsp;</div>
				<div style="float:left;padding-top:1px"><ext:field style="width:50px" styleClass="field required" property="policyComparison" selectOnly="true" value="超过" itemsText="超过\n少于\n介于" onchange="onPolicyComparisonChange(value)"/></div>
				<div style="float:left">
					<span>&nbsp;<html:text style="width:30" property="policyDayMin" styleClass="field required" value=""/>&nbsp;天</span>
					<span id="dayRange" style="display:none">和&nbsp;<html:text style="width:30" property="policyDayMax" styleClass="field required" value=""/>&nbsp;天</span>
					，扣款&nbsp;<html:text style="width:50" property="policyPercentage" styleClass="field required" value=""/>&nbsp;%
					<input class="button" style="width:60px" type="button" value="添加策略" onclick="FormUtils.doAction('appendDeductPolicy')"/>
				</div>
				<div style="line-height:26px">
					<br>
					<ext:iterate id="policy" indexId="policyIndex" property="serviceDeductPolicies">
						<ext:writeNumber name="policyIndex" plus="1"/>、<ext:write name="policy" property="description"/>&nbsp;<a href="javascript:FormUtils.doAction('deleteDeductPolicy', 'policyId=<ext:write name="policy" property="id"/>')"  style="color:#ff0000"/>&nbsp;删除</a><br>
					</ext:iterate>
				</div>
			</span>
		</td>
	</tr>
	<tr>
		<td>创建人</td>
		<td><ext:write property="creator"/></td>
		<td>创建时间</td>
		<td><ext:write property="created" format="yyyy-MM-dd"/></td>  
	</tr>	
	<tr>
		<td>备注</td>
		<td colspan="3"><html:text property="remark"/></td>
	</tr>
</table>
<html:hidden property="serviceId"/>