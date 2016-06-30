<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:3px;">办理人：</div>
<table width="100%" style="border-collapse:collapse;" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="center" width="43px">
	<col valign="middle" align="left">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<tr>
		<td class="tdtitle" align="center">序号</td>
		<td class="tdtitle" align="center">接收单位</td>
		<td class="tdtitle" align="center">签收人</td>
		<td class="tdtitle" align="center">经办人</td>
		<td class="tdtitle" align="center">签收时间</td>
	</tr>
	<ext:iterate id="sign" indexId="signIndex" property="signs">
		<tr>
			<td class="tdcontent"><ext:writeNumber name="signIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="sign" property="receiverName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signPersonName"/><ext:equal value="1" name="sign" property="isAgentSign">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="signOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>