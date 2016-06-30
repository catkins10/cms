<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" align="center">
		<td class="tdtitle" nowrap="nowrap" width="32px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">报名号</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">报名时间</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">保证金缴纳时间</td>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">开户行</td>
		<td class="tdtitle" nowrap="nowrap" width="200px">帐号</td>
	</tr>
<%	int signUpIndex = 1; %>
	<ext:iterate id="signUp" property="signUps">
		<ext:notEmpty name="signUp" property="pledgePaymentTime">
<%
			com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp signUp = (com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp)pageContext.getAttribute("signUp");
%>
			<tr valign="middle" align="center" id="trSignUp">
				<td class="tdcontent"><%=(signUpIndex++)%></td>
				<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpTime"/></td>
				<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
				<td class="tdcontent" align="left">
					<input type="hidden" name="signUpId" value="<ext:write name="signUp" property="id"/>"/>
<%					if(signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || signUp.getEnterpriseName().indexOf('*')!=-1) { %>
						<ext:field name="signUp" property="enterpriseName" onchange="accountComplement(this)"/>
<%					}
					else { %>
						<ext:field name="signUp" property="enterpriseName" onchange="accountComplement(this)" style="display:none"/>
						<ext:field writeonly="true" name="signUp" property="enterpriseName"/>
<%					} %>
				</td>
				<td class="tdcontent" align="left">
					<ext:field name="signUp" property="enterpriseBank" onchange="accountComplement(this)"/>
				</td>
				<td class="tdcontent" align="left">
<%					if(signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty() || signUp.getEnterpriseAccount().indexOf('*')!=-1) { %>
						<ext:field name="signUp" property="enterpriseAccount" onchange="accountComplement(this)"/>
<%					}
					else { %>
						<ext:field name="signUp" property="enterpriseAccount" onchange="accountComplement(this)" style="display:none"/>
						<ext:field writeonly="true" name="signUp" property="enterpriseAccount"/>
<%					} %>
				</td>
			</tr>
		</ext:notEmpty>
	</ext:iterate>
</table>
<iframe id="accountComplementFrame" style="display:none"></iframe>
<script>
	function accountComplement(src) {
		var parent = src.parentElement;
		for(; parent.id!='trSignUp'; parent=parent.parentElement);
		document.getElementById('accountComplementFrame').src = '<%=request.getContextPath()%>/bidding/project/accountComplement.shtml' +
																'?projectId=<ext:write property="id"/>' +
																'&signUpId=' + DomUtils.getElement(parent, 'input', 'signUpId').value +
																'&enterpriseName=' + StringUtils.utf8Encode(DomUtils.getElement(parent, 'input', 'enterpriseName').value) +
																'&enterpriseBank=' + StringUtils.utf8Encode(DomUtils.getElement(parent, 'input', 'enterpriseBank').value) +
																'&enterpriseAccount=' + StringUtils.utf8Encode(DomUtils.getElement(parent, 'input', 'enterpriseAccount').value);
	}
</script>