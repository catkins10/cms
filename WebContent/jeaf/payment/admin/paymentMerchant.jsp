<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/savePaymentMerchant">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col align="right">
		<col width="100%">
		<ext:empty property="paymentMethod">
			<tr>
				<td class="tdtitle" nowrap>银行</td>
				<td class="tdcontent"><ext:field property="paymentMethod" onchange="if(value!='')FormUtils.doAction('paymentMerchant')"/></td>
			</tr>
		</ext:empty>
		<ext:notEmpty property="paymentMethod">
			<tr>
				<td class="tdtitle" nowrap>银行</td>
				<td class="tdcontent">
					<html:hidden property="paymentMethod"/>
					<ext:field writeonly="true" property="paymentMethod"/>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap>户名</td>
				<td class="tdcontent"><ext:field property="name"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap>帐号</td>
				<td class="tdcontent"><ext:field property="account"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap>开户银行</td>
				<td class="tdcontent"><ext:field property="bank"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap>转账密码</td>
				<td class="tdcontent"><ext:field property="transferPassword"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap>启用的帐户类型</td>
				<td class="tdcontent"><ext:field property="accountTypeArray"/></td>
			</tr>
			<ext:iterate id="parameter" property="parameters">
				<tr>
					<td class="tdtitle" nowrap><ext:write name="parameter" property="parameterDefine.label"/></td>
					<td class="tdcontent"><ext:field name="parameter" property="parameterValue" title="<%=((com.yuanluesoft.jeaf.payment.pojo.PaymentMerchantParameter)pageContext.getAttribute("parameter")).getParameterDefine().getRemark()%>"/></td>
				</tr>
			</ext:iterate>
			<tr>
				<td class="tdtitle" nowrap>是否停用</td>
				<td class="tdcontent"><ext:field property="halt"/></td>
			</tr>
		</ext:notEmpty>
	</table>
</ext:form>