<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:3px;">办理人：</div>
<table width="100%" style="border-collapse:collapse;" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="center" width="43px">
	<col valign="middle" align="left">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<col valign="middle" align="center" width="68px">
	<tr align="center">
		<td class="tdtitle" align="center">序号</td>
		<td class="tdtitle" align="center">接收单位</td>
		<td class="tdtitle" align="center">签收人</td>
		<td class="tdtitle" align="center">签收经办</td>
		<td class="tdtitle" align="center">签收时间</td>
		<td class="tdtitle" align="center">清退人</td>
		<td class="tdtitle" align="center">清退经办</td>
		<td class="tdtitle" align="center">清退时间</td>
	</tr>
	<ext:iterate id="sign" indexId="signIndex" property="signs">
		<tr>
			<td class="tdcontent"><ext:writeNumber name="signIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="sign" property="receiverName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signPersonName"/><ext:equal value="1" name="sign" property="isAgentSign">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="signOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent"><ext:write name="sign" property="returnPersonName"/><ext:equal value="1" name="sign" property="isAgentReturn">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="returnOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="returnTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>