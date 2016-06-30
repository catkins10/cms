<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom: 3px; font-weight: bold;">
	<input type="button" class="button" value="打印投标保证金汇总表" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>')">
	<input type="button" class="button" value="打印投标保证金返还名单(待返还)" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>&status=1')">
	<input type="button" class="button" value="打印投标保证金返还清单(已返还)" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>&status=2')">
</div>
<div style="padding-bottom: 3px; padding-top:5px; font-weight: bold;">
	未返还：
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="30px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="30px">评标排名</td>
		<td class="tdtitle" nowrap="nowrap" width="72px">报名号</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">企业名称</td>
		<td class="tdtitle" nowrap="nowrap" width="130px">开户行</td>
		<td class="tdtitle" nowrap="nowrap" width="140px">帐号</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">保证金金额</td>
		<td class="tdtitle" nowrap="nowrap" width="95px">保证金缴纳时间</td>
		<td class="tdtitle" nowrap="nowrap" width="32px">计息天数</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">返还金额</td>
		<!-- td class="tdtitle" nowrap="nowrap" width="110px">转账单最后生成时间</td -->
		<td class="tdtitle" nowrap="nowrap" width="95px">转账时间</td>
		<td class="tdtitle" nowrap="nowrap">转账失败原因</td>
	</tr>
<%
	int signUpIndex = 1;
	double pledgePaidMoneyTotal = 0;
	double pledgeReturnMoneyTotal = 0;
%>
	<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
		<ext:notEmpty name="signUp" property="pledgePaymentTime"><ext:equal value="1" name="signUp" property="pledgeConfirm"><ext:empty name="signUp" property="pledgeReturnTime">
<%				
			com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp signUp = (com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)pageContext.getAttribute("signUp");
			pledgePaidMoneyTotal += signUp.getPledgePaidMoney();
			pledgeReturnMoneyTotal += signUp.getPledgeReturnMoney();
%>
			<tr valign="middle" align="center" id="trSignUp" signUpId="<ext:field writeonly="true" name="signUp" property="id"/>">
				<td class="tdcontent"><%=signUpIndex++%></td>
				<td class="tdcontent">
					<ext:notEqual value="0" name="signUp" property="ranking">
						<ext:field name="signUp" property="ranking"/>
					</ext:notEqual>
				</td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseBank"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseAccount"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaidMoney"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnDays"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnMoney"/></td>
				<!-- td class="tdcontent"><ext:field name="signUp" property="pledgeReturnExportTime"/></td -->
				<td class="tdcontent"><ext:field name="signUp" property="pledgeReturnTransferTime"/></td>
				<td class="tdcontent"><ext:field name="signUp" property="pledgeReturnTransferError"/></td>
			</tr>
		</ext:empty></ext:equal></ext:notEmpty>
	</ext:iterate>
</table>
<div style="float:right; padding-top:3px">
	保证金实收合计:<%=new java.text.DecimalFormat("###,###.##").format(pledgePaidMoneyTotal)%>元&nbsp;
	保证金返还合计:<%=new java.text.DecimalFormat("###,###.##").format(pledgeReturnMoneyTotal)%>元
</div>

<div style="padding-bottom: 3px; padding-top:15px; font-weight: bold;">已返还：</div>
<%
	signUpIndex = 1;
	pledgePaidMoneyTotal = 0;
	pledgeReturnMoneyTotal = 0;
%>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="30px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="30px">评标排名</td>
		<td class="tdtitle" nowrap="nowrap" width="72px">报名号</td>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdtitle" nowrap="nowrap" width="130px">开户行</td>
		<td class="tdtitle" nowrap="nowrap" width="140px">帐号</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">保证金金额</td>
		<td class="tdtitle" nowrap="nowrap" width="95px">保证金缴纳时间</td>
		<td class="tdtitle" nowrap="nowrap" width="32px">计息天数</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">返还金额</td>
		<td class="tdtitle" nowrap="nowrap" width="95px">返还时间</td>
	</tr>
	<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
		<ext:notEmpty name="signUp" property="pledgeReturnTime">
<%
			com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp signUp = (com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)pageContext.getAttribute("signUp");
			pledgePaidMoneyTotal += signUp.getPledgePaidMoney();
			pledgeReturnMoneyTotal += signUp.getPledgeReturnMoney();
%>
			<tr valign="middle" align="center" id="trSignUp">
				<td class="tdcontent"><%=signUpIndex++%></td>
				<td class="tdcontent">
					<ext:notEqual value="0" name="signUp" property="ranking">
						<ext:field name="signUp" property="ranking"/>
					</ext:notEqual>
				</td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseBank"/></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseAccount"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaidMoney"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnDays"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnMoney"/></td>
				<td class="tdcontent""><ext:field writeonly="true" name="signUp" property="pledgeReturnTime"/></td>
			</tr>
		</ext:notEmpty>
	</ext:iterate>
</table>
<div style="float:right; padding-top:3px">
	保证金实收合计:<%=new java.text.DecimalFormat("###,###.##").format(pledgePaidMoneyTotal)%>元&nbsp;
	保证金返还合计:<%=new java.text.DecimalFormat("###,###.##").format(pledgeReturnMoneyTotal)%>元
</div>