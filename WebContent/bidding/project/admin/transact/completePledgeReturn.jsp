<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:equal value="false" property="pledgeReturned">
	<div style="width:600px">
		<b>下列企业保证金未返还，是否确定继续执行当前操作？</b>
		<table width="100%" class="table" style="margin-top: 3px" border="1" cellpadding="0" cellspacing="0">
			<tr height="23px" align="center">
				<td class="tdtitle" nowrap="nowrap" width="80px">报名号</td>
				<td class="tdtitle" nowrap="nowrap">企业名称</td>
				<td class="tdtitle" nowrap="nowrap" width="200px">返还失败原因</td>
			</tr>
			<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
				<ext:equal value="1" name="signUp" property="pledgeReturnEnabled"><ext:notEmpty name="signUp" property="pledgePaymentTime"><ext:empty name="signUp" property="pledgeReturnTime">
					<tr valign="middle" align="center" id="trSignUp">
						<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="signUpNo"/></td>
						<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="signUp" property="pledgeReturnTransferError"/></td>
					</tr>
				</ext:empty></ext:notEmpty></ext:equal>
			</ext:iterate>
		</table>
	</div>
</ext:equal>

<ext:equal value="true" property="pledgeReturned">
	<div style="width:300px">
		<div style="font-size:14px">
			是否确定保证金返还已经完成？
		</div>
	</div>
</ext:equal>