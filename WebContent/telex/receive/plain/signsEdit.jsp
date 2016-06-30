<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<div style="padding-bottom:3px">
	<table border="0" cellpadding="2" cellspacing="0">
		<tr>
			<td><input type="button" class="button" value="添加单位" style="width:66px" onclick="appendUnitSigns()"></td>
			<td><input type="button" class="button" value="添加个人" style="width:66px" onclick="appendPersonSigns()"></td>
			<td><input type="button" class="button" value="删除单位(个人)" style="width:96px" onclick="deleteSigns()"></td>
		</tr>
	</table>
	<ext:notEqual value="0" property="signId">
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td><b>签收人:</b></td>
				<td style="width:80px"><ext:field property="signPersonName"/></td>
				<td>&nbsp;<b>经办人:</b><ext:write name="SessionInfo" property="userName" scope="session"/></td>
				<td>&nbsp;<b>签收时间</b>:</td>
				<td style="width:170px"><ext:field property="signTime"/></td>
				<td><input type="button" class="button" value="完成签收" style="width:66px" onclick="doSign()"></td>
			</tr>
		</table>
	</ext:notEqual>
</div>
<table width="100%" style="border-collapse:collapse;" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" align="center" width="32px">
	<col valign="middle" align="center" width="32px">
	<col valign="middle" align="left">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="70px">
	<col valign="middle" align="center" width="110px">
	<col valign="middle" align="center" width="60px">
	<tr>
		<td class="tdtitle" align="center">选择</td>
		<td class="tdtitle" align="center">序号</td>
		<td class="tdtitle" align="center">接收单位</td>
		<td class="tdtitle" align="center">签收人</td>
		<td class="tdtitle" align="center">经办人</td>
		<td class="tdtitle" align="center">签收时间</td>
		<td class="tdtitle" align="center">&nbsp;</td>
	</tr>
	<ext:iterate id="sign" indexId="signIndex" property="signs">
		<tr>
			<td class="tdcontent"><input name="selectSign" type="checkbox" class="checkbox" value="<ext:write name="sign" property="id"/>"><span style="display:none"><ext:write name="sign" property="receiverId"/></span></td>
			<td class="tdcontent"><ext:writeNumber name="signIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="sign" property="receiverName"/></td>
			<td class="tdcontent" class="tdcontent"><ext:write name="sign" property="signPersonName"/><ext:equal value="1" name="sign" property="isAgentSign">(代)</ext:equal></td>
			<td class="tdcontent"><ext:write name="sign" property="signOperatorName"/></td>
			<td class="tdcontent"><ext:write name="sign" property="signTime" format="yyyy-MM-dd HH:mm"/></td>
			<td class="tdcontent">
				<ext:empty name="sign" property="signTime">
					<ext:equal value="0" property="signId">
						<a href="#" onclick="FormUtils.doAction('sign', 'signId=<ext:write name="sign" property="id"/>')">签收</a>
					</ext:equal>
				</ext:empty>
			</td>
		</tr>
	</ext:iterate>
</table>

<script>
	function doSign() {
		if(document.getElementsByName("signPersonName")[0].value=="") {
			alert("签收人不能为空");
			return;
		}
		FormUtils.doAction("doSign");
	}
</script>