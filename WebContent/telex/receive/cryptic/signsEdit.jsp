<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:3px">
	<input type="button" class="button" value="添加单位" style="width:66px" onclick="appendUnitSigns()">
	<input type="button" class="button" value="添加个人" style="width:66px" onclick="appendPersonSigns()">
	<input type="button" class="button" value="删除单位(个人)" style="width:96px" onclick="deleteSigns()">
</div>
<table width="100%" style="border-collapse:collapse;" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="center" width="43px">
	<col valign="middle" align="center" width="43px">
	<col valign="middle" align="left">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<col valign="middle" align="center" width="60px">
	<col valign="middle" align="center" width="68px">
	<tr align="center">
		<td class="tdtitle" align="center">选择</td>
		<td class="tdtitle" align="center">序号</td>
		<td class="tdtitle" align="center">接收单位</td>
		<td class="tdtitle" align="center">签收人</td>
		<td class="tdtitle" align="center">签收经办</td>
		<td class="tdtitle" align="center">签收时间</td>
		<td class="tdtitle" align="center">清退人</td>
		<td class="tdtitle" align="center">清退经办</td>
		<td class="tdtitle" align="center">清退时间</td>
		<td class="tdtitle" align="center">退报设置</td>
		<td class="tdtitle" align="center">&nbsp;</td>
	</tr>
	<ext:iterate id="sign" indexId="signIndex" property="signs">
		<tr>
			<td class="tdcontent"><input name="selectSign" type="checkbox" class="checkbox" value="<ext:write name="sign" property="id"/>"><span style="display:none"><ext:write name="sign" property="receiverId"/></span></td>
			<td class="tdcontent"><ext:writeNumber name="signIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="sign" property="receiverName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signPersonName"/><ext:equal value="1" name="sign" property="isAgentSign">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="signOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent"><ext:write name="sign" property="returnPersonName"/><ext:equal value="1" name="sign" property="isAgentReturn">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="returnOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="returnTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent">
				<ext:empty name="sign" property="returnTime">
					<ext:equal value="1" name="sign" property="needReturn">
						<input type="checkbox" class="checkbox" onclick="FormUtils.doAction('setReturnOption', 'receiverNeedReturn=false&telegramSignId=<ext:write name="sign" property="id"/>')" checked="checked">
					</ext:equal>
					<ext:notEqual value="1" name="sign" property="needReturn">
						<input type="checkbox" class="checkbox" onclick="FormUtils.doAction('setReturnOption', 'receiverNeedReturn=true&telegramSignId=<ext:write name="sign" property="id"/>')">
					</ext:notEqual>
				</ext:empty>
			</td>
			<td class="tdcontent">
				<ext:notEmpty name="sign" property="signTime">
					<a target="_blank" href="printBill.shtml?selectedTelegramSignIds=<ext:write name="sign" property="id"/>">打印办理单</a>
				</ext:notEmpty>
			</td>
		</tr>
	</ext:iterate>
</table>

<script>
	
</script>