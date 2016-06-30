<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="width:600px">
	<b>是否确定完成保证金确认，剔除企业名单如下：</b>
	<table width="100%" class="table" style="margin-top: 3px" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" align="center">
			<td class="tdtitle" nowrap="nowrap">企业名称</td>
			<td class="tdtitle" nowrap="nowrap" width="110px">缴纳时间</td>
			<td class="tdtitle" nowrap="nowrap" width="200px">备注或用途</td>
		</tr>
		<ext:iterate id="signUp" indexId="signUpIndex" property="signUps">
			<ext:equal value="2" name="signUp" property="pledgeConfirm">
				<tr valign="middle" align="center">
					<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="enterpriseName"/></td>
					<td class="tdcontent" align="center"><ext:field writeonly="true" name="signUp" property="pledgePaymentTime"/></td>
					<td class="tdcontent" align="left"><ext:field writeonly="true" name="signUp" property="pledgeBillBackRemark"/></td>
				</tr>
			</ext:equal>
		</ext:iterate>
	</table>
</div>