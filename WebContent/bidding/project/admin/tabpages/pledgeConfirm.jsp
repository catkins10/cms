<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom: 3px; font-weight: bold;">
	<input type="button" class="button" value="打印投标保证金汇总表" onclick="window.open('<%=request.getContextPath()%>/bidding/project/report/admin/projectPledgeReport.shtml?projectId=<ext:write property="id"/>')">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="30px">排除</td>
		<td class="tdtitle" nowrap="nowrap" width="30px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="72px">报名号</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">企业名称</td>
		<td class="tdtitle" nowrap="nowrap" width="130px">开户行</td>
		<td class="tdtitle" nowrap="nowrap" width="140px">帐号</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">保证金金额</td>
		<td class="tdtitle" nowrap="nowrap" width="95px">保证金缴纳时间</td>
		<td class="tdtitle" nowrap="nowrap">备注或用途</td>
	</tr>
<%
	int signUpIndex = 1;
%>
	<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
		<ext:notEmpty name="signUp" property="pledgePaymentTime"><ext:notEqual value="3" name="signUp" property="pledgeConfirm">
<%				
			com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp signUp = (com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)pageContext.getAttribute("signUp");
%>
			<tr valign="middle" align="center" id="trSignUp">
				<td class="tdcontent">
					<ext:empty name="signUp" property="signUpNo">
						<input type="checkbox" name="pledgeConfirmSignUpId" value="<ext:write name="signUp" property="id"/>" <%if(signUp.getPledgeConfirm()=='2') { %>checked="true"<%}%>/>
					</ext:empty>
				</td>
				<td class="tdcontent"><%=signUpIndex++%></td>
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
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="pledgeBillBackRemark"/></td>
			</tr>
		</ext:notEqual></ext:notEmpty>
	</ext:iterate>
</table>