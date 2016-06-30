<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="width:700px">
	<b>返还下列企业的保证金：</b>
	<table width="100%" class="table" style="margin-top: 3px" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" align="center">
			<td class="tdtitle" nowrap="nowrap" width="80px">报名号</td>
			<td class="tdtitle" nowrap="nowrap">企业名称</td>
			<td class="tdtitle" nowrap="nowrap" width="80px">保证金金额</td>
			<td class="tdtitle" nowrap="nowrap" width="100px">保证金缴纳时间</td>
			<td class="tdtitle" nowrap="nowrap" width="50px">计息天数</td>
			<td class="tdtitle" nowrap="nowrap" width="80px">返还金额</td>
		</tr>
		<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
			<ext:equal value="1" name="signUp" property="pledgeReturnEnabled">
				<tr valign="middle" align="center" id="trSignUp">
					<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
					<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
					<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaidMoney"/></td>
					<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
					<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnDays"/></td>
					<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnMoney"/></td>
				</tr>
			</ext:equal>
		</ext:iterate>
	</table>
</div>