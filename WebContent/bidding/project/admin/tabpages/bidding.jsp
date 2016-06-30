<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:5px">
	<input type="button" class="button" value="打印投标报名汇总表" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectSignUpReport.shtml?projectId=<ext:write property="id"/>')">
</div>
<%
	int rowNum = 0;
	double paymentMoneyTotal = 0;
	double drawPaymentMoneyTotal = 0;
%>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" align="center">
		<td class="tdtitle" nowrap="nowrap" width="32px" rowspan="2">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%" rowspan="2">报名号</td>
		<td class="tdtitle" nowrap="nowrap" width="110px" rowspan="2">报名时间</td>
		<td class="tdtitle" nowrap="nowrap" colspan="3">标书</td>
		<td class="tdtitle" nowrap="nowrap" colspan="3">图纸</td>
	</tr>
	<tr height="23px" align="center">	
		<td class="tdtitle" nowrap="nowrap" width="110px">购买时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">支付方式</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">收款银行</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">购买时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">支付方式</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">收款银行</td>
	</tr>
	<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
		<ext:notEmpty name="signUp" property="paymentTime">
<%
			rowNum++;
			com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp signUp = (com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)pageContext.getAttribute("signUp");
			paymentMoneyTotal += signUp.getPaymentTime()==null ? 0 : signUp.getPaymentMoney();
			drawPaymentMoneyTotal += signUp.getDrawPaymentTime()==null ? 0 : signUp.getDrawPaymentMoney();
%>
			<tr valign="top" align="center">
				<td class="tdcontent"><%=rowNum%></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="paymentTime"/></td>
				<td class="tdcontent"><ext:write name="signUp" property="paymentMode"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="paymentBank"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="drawPaymentTime"/></td>
				<td class="tdcontent"><ext:write name="signUp" property="drawPaymentMode"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="drawPaymentBank"/></td>
			</tr>
		</ext:notEmpty>
	</ext:iterate>
</table>
<div style="text-align: right; padding-top: 5px">
	报名企业数量：<ext:write property="signUpTotal.total"/>&nbsp;&nbsp;
	支付完成企业数量：<ext:write property="signUpTotal.paymentTotal"/>&nbsp;&nbsp;
	报名费累计：<%=new java.text.DecimalFormat("###,###.##").format(paymentMoneyTotal)%>元&nbsp;&nbsp;
	图纸购买累计：<%=new java.text.DecimalFormat("###,###.##").format(drawPaymentMoneyTotal)%>元&nbsp;&nbsp;
</div>