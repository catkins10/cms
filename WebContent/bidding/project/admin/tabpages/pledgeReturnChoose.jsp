<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function selectPledgeReturnSignUps(selected, top3Exclude, top3Only) {
		var pledgeReturnSignUpIds = document.getElementsByName("pledgeReturnSignUpId");
		for(var i=0; i<pledgeReturnSignUpIds.length; i++) {
			pledgeReturnSignUpIds[i].checked = selected;
			if(selected) {
				var ranking = Number(pledgeReturnSignUpIds[i].id);
				var top3 = !isNaN(ranking) && ranking>0 && ranking<=3;
				if(top3Exclude) {
					pledgeReturnSignUpIds[i].checked = !top3;
				}
				else if(top3Only) {
					pledgeReturnSignUpIds[i].checked = top3;
				}
			}
		}
	}
</script>

<div style="padding-bottom: 3px; font-weight: bold;">
	未返还：
	<input type="button" class="button" value="除前3名外全选" onclick="selectPledgeReturnSignUps(true, true)">
	<input type="button" class="button" value="选择前3名" onclick="selectPledgeReturnSignUps(true, false, true)">
	<input type="button" class="button" value="全部选中" onclick="selectPledgeReturnSignUps(true)">
	<input type="button" class="button" value="全部取消" onclick="selectPledgeReturnSignUps(false)">
	<input type="button" class="button" value="打印投标保证金返还名单(待返还)" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>&status=1')">
	<input type="button" class="button" value="打印投标保证金返还清单(已返还)" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>&status=2')">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="30px">选择</td>
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
		<td class="tdtitle" nowrap="nowrap" width="110px">制表时间</td>
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
			<tr valign="middle" align="center" id="trSignUp">
				<td class="tdcontent"><input type="checkbox" name="pledgeReturnSignUpId" value="<ext:write name="signUp" property="id"/>" <ext:equal value="1" name="signUp" property="pledgeReturnEnabled">checked="true"</ext:equal> id="<ext:field name="signUp" property="ranking"/>"/></td>
				<td class="tdcontent"><%=signUpIndex++%></td>
				<td class="tdcontent">
					<ext:notEqual value="0" name="signUp" property="ranking">
						<ext:field name="signUp" property="ranking"/>
					</ext:notEqual>
				</td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent" align="left">
					<input type="hidden" name="signUpId" value="<ext:write name="signUp" property="id"/>"/>
<%					if(signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || signUp.getEnterpriseName().indexOf('*')!=-1) { %>
						<ext:field name="signUp" property="enterpriseName"/>
<%					}
					else { %>
						<ext:field name="signUp" property="enterpriseName" style="display:none"/>
						<ext:field writeonly="true" name="signUp" property="enterpriseName"/>
<%					} %>
				</td>
				<td class="tdcontent" align="left">
<%					if(signUp.getEnterpriseBank()==null || signUp.getEnterpriseBank().isEmpty()) { %>
						<ext:field name="signUp" property="enterpriseBank"/>
<%					}
					else { %>
						<ext:field name="signUp" property="enterpriseBank" style="display:none"/>
						<ext:field writeonly="true" name="signUp" property="enterpriseBank"/>
<%					} %>
				</td>
				<td class="tdcontent" align="left">
<%					if(signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty() || signUp.getEnterpriseAccount().indexOf('*')!=-1) { %>
						<ext:field name="signUp" property="enterpriseAccount"/>
<%					}
					else { %>
						<ext:field name="signUp" property="enterpriseAccount" style="display:none"/>
						<ext:field writeonly="true" name="signUp" property="enterpriseAccount"/>
<%					} %>
				</td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaidMoney"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnDays"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnMoney"/></td>
				<td class="tdcontent"><ext:field name="signUp" property="pledgeReturnExportTime"/></td>
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

<div style="padding-bottom: 3px; padding-top:10px; font-weight: bold;">已返还：</div>
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