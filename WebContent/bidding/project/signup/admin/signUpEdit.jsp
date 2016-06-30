<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">报名号</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signUpNo"/></td>
		<td class="tdtitle" nowrap="nowrap">报名时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="signUpTime"/></td>
	</tr>
	<ext:empty property="paymentTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书价格</td>
			<td class="tdcontent"><ext:field writeonly="true" property="paymentMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">标书缴费时间</td>
			<td class="tdcontent"><ext:field property="paymentTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书缴费银行</td>
			<td class="tdcontent"><ext:field property="paymentBank"/></td>
			<td class="tdtitle" nowrap="nowrap">银行回单号</td>
			<td class="tdcontent"><ext:field property="billBack"/></td>
		</tr>
	</ext:empty>
	<ext:notEmpty property="paymentTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书价格</td>
			<td class="tdcontent"><ext:field writeonly="true" property="paymentMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">标书缴费时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="paymentTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">标书缴费方式</td>
			<td class="tdcontent"><ext:field writeonly="true" property="isInternetPayment"/></td>
			<td class="tdtitle" nowrap="nowrap">标书缴费银行</td>
			<td class="tdcontent"><ext:field writeonly="true" property="paymentBank"/></td>
		</tr>
		<ext:notEqual value="1" property="isInternetPayment">
			<tr>
				<td class="tdtitle" nowrap="nowrap">银行回单号</td>
				<td class="tdcontent"><ext:field writeonly="true" property="billBack"/></td>
				<td class="tdtitle" nowrap="nowrap">记录人姓名</td>
				<td class="tdcontent"><ext:field writeonly="true" property="recorder"/></td>
			</tr>
		</ext:notEqual>
		<ext:notEmpty property="uploadTime">
			<tr>
				<td class="tdtitle" nowrap="nowrap">标书上传时间</td>
				<td class="tdcontent" colspan="3"><ext:field writeonly="true" property="uploadTime"/></td>
			</tr>
		</ext:notEmpty>
	</ext:notEmpty>
	
	<ext:empty property="drawPaymentTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">图纸价格</td>
			<td class="tdcontent"><ext:field writeonly="true" property="drawPaymentMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">图纸缴费时间</td>
			<td class="tdcontent"><ext:field property="drawPaymentTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">图纸缴费银行</td>
			<td class="tdcontent"><ext:field property="drawPaymentBank"/></td>
			<td class="tdtitle" nowrap="nowrap">银行回单号</td>
			<td class="tdcontent"><ext:field property="drawBillBack"/></td>
		</tr>
	</ext:empty>
	<ext:notEmpty property="drawPaymentTime">
		<tr>
			<td class="tdtitle" nowrap="nowrap">图纸纸价格</td>
			<td class="tdcontent"><ext:field writeonly="true" property="drawPaymentMoney"/></td>
			<td class="tdtitle" nowrap="nowrap">图纸缴费时间</td>
			<td class="tdcontent"><ext:field writeonly="true" property="drawPaymentTime"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">图纸缴费方式</td>
			<td class="tdcontent"><ext:field writeonly="true" property="isDrawInternetPayment"/></td>
			<td class="tdtitle" nowrap="nowrap">图纸缴费银行</td>
			<td class="tdcontent"><ext:field writeonly="true" property="drawPaymentBank"/></td>
		</tr>
		<ext:notEqual value="1" property="isDrawInternetPayment">
			<tr>
				<td class="tdtitle" nowrap="nowrap">银行回单号</td>
				<td class="tdcontent"><ext:field writeonly="true" property="drawBillBack"/></td>
				<td class="tdtitle" nowrap="nowrap">记录人姓名</td>
				<td class="tdcontent"><ext:field writeonly="true" property="drawRecorder"/></td>
			</tr>
		</ext:notEqual>
	</ext:notEmpty>
	
	<ext:equal value="true" property="paymentPledge">
		<ext:empty property="pledgePaymentTime">
			<tr>
				<td class="tdtitle" nowrap="nowrap">保证金金额</td>
				<td class="tdcontent"><ext:field writeonly="true" property="pledgePaymentMoney"/></td>
				<td class="tdtitle" nowrap="nowrap">保证金实收金额</td>
				<td class="tdcontent"><ext:field property="pledgePaidMoney"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">保证金缴费时间</td>
				<td class="tdcontent"><ext:field property="pledgePaymentTime"/></td>
				<td class="tdtitle" nowrap="nowrap">保证金银行回单号</td>
				<td class="tdcontent"><ext:field property="pledgeBillBack"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">保证金缴费银行</td>
				<td class="tdcontent"><ext:field property="pledgePaymentBank"/></td>
				<td class="tdtitle" nowrap="nowrap">投标企业名称</td>
				<td class="tdcontent"><ext:field property="enterpriseName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">投标企业开户行</td>
				<td class="tdcontent"><ext:field property="enterpriseBank"/></td>
				<td class="tdtitle" nowrap="nowrap">投标企业帐号</td>
				<td class="tdcontent"><ext:field property="enterpriseAccount"/></td>
			</tr>
		</ext:empty>
		<ext:notEmpty property="pledgePaymentTime">
			<tr>
				<td class="tdtitle" nowrap="nowrap">保证金实收金额</td>
				<td class="tdcontent"><ext:field writeonly="true" property="pledgePaidMoney"/></td>
				<td class="tdtitle" nowrap="nowrap">保证金缴费时间</td>
				<td class="tdcontent"><ext:field writeonly="true" property="pledgePaymentTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">保证金缴费方式</td>
				<td class="tdcontent"><ext:field writeonly="true" property="isPledgeInternetPayment"/></td>
				<td class="tdtitle" nowrap="nowrap">保证金缴费银行</td>
				<td class="tdcontent"><ext:field writeonly="true" property="pledgePaymentBank"/></td>
			</tr>
		</ext:notEmpty>
	</ext:equal>
</table>