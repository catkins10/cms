<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" style="table-layout:fixed" border="0" cellpadding="3" cellspacing="1" class="contentTable">
	<col valign="middle" width="80px" align="right">
	<col valign="middle" width="100%">
	<ext:equal value="anonymous" name="SessionInfo" property="loginName" scope="session">
		<tr>
			<td class="tdtitle" nowrap="nowrap">昵称</td>
			<td class="tdcontent"><ext:field property="creatorNickname"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">回复主题</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td valign="top" class="tdtitle">回复内容</td>
		<td class="tdcontent"><ext:field property="body"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">验证码</td>
		<td class="tdcontent">
			<img id="validateCodeImage" src="<%=request.getContextPath()%>/jeaf/validatecode/generateValidateCodeImage.shtml"> <a style="color:blue" href="javascript:FormUtils.reloadValidateCodeImage()">看不清，换一张</a>			
			<ext:field property="validateCode"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent"><ext:field property="attachment"/></td>
	</tr>
</table>
<br>
<center>
	<ext:button name="提交" onclick="FormUtils.submitForm()" width="60px"/>
	<ext:button name="取消" onclick="history.back()" width="60px"/>
</center>