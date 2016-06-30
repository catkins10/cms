<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table border="0" cellpadding="3" cellspacing="3" style="color:#000000; width:430px" align="center">
	<col align="right">
	<col>
	<tr>
		<td nowrap>充值卡号：</td>
		<td>
			<ext:equal value="0" property="cardNumber" >
				<html:text property="cardNumber" styleClass="field required" value=""/>
			</ext:equal>
			<ext:notEqual value="0" property="cardNumber" >
				<html:text property="cardNumber" styleClass="field required"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td nowrap>充值卡密码：</td>
		<td>
			<ext:equal value="0" property="cardPassword" >
				<html:text property="cardPassword" styleClass="field required" value=""/>
			</ext:equal>
			<ext:notEqual value="0" property="cardPassword" >
				<html:text property="cardPassword" styleClass="field required"/>
			</ext:notEqual>
		</td>
	</tr>
	<tr>
		<td>验证码：</td>
		<td>
			<img id="validateCodeImage" src="<%=request.getContextPath()%>/jeaf/validatecode/generateValidateCodeImage.shtml"> <a style="color:blue" href="javascript:FormUtils.reloadValidateCodeImage()">看不清，换一张</a>
			<html:text property="validateCode" styleClass="field required" maxlength="10"/>
		</td>
		<td nowrap="nowrap"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<br>
			<input type="button" value="下一步" class="button" onclick="FormUtils.submitForm()">
		</td>
	</tr>
</table>
