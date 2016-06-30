<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/signTelegrams">
	<script>
	function signTelegrams() { //签收电报
		var selectSignTelegrams = document.getElementsByName("selectSignTelegram");
		var ids = "";
		for(var i=0; i<selectSignTelegrams.length; i++) {
			if(selectSignTelegrams[i].checked) {
				ids += (ids=="" ? "" : ",") + selectSignTelegrams[i].value;
			}
		}
		if(ids=="") {
			alert("尚未选择待签收电报。");
			return;
		}
		document.getElementsByName("selectedTelegramSignIds")[0].value = ids;
		FormUtils.doAction("signTelegrams");
	}
	function returnTelegrams() { //清退电报
		var selectReturnTelegrams = document.getElementsByName("selectReturnTelegram");
		var ids = "";
		for(var i=0; i<selectReturnTelegrams.length; i++) {
			if(selectReturnTelegrams[i].checked) {
				ids += (ids=="" ? "" : ",") + selectReturnTelegrams[i].value;
			}
		}
		if(ids=="") {
			alert("尚未选择待清退电报。");
			return;
		}
		document.getElementsByName("selectedTelegramSignIds")[0].value = ids;
		FormUtils.doAction("returnTelegrams");
	}
	function selectAllSignTelegrams(checked) { //全选待签收电报
		var selectSignTelegrams = document.getElementsByName("selectSignTelegram");
		for(var i=0; i<selectSignTelegrams.length; i++) {
			selectSignTelegrams[i].checked = checked;
		}
	}
	function selectAllReturnTelegrams(checked) { //全选待清退电报
		var selectReturnTelegrams = document.getElementsByName("selectReturnTelegram");
		for(var i=0; i<selectReturnTelegrams.length; i++) {
			selectReturnTelegrams[i].checked = checked;
		}
	}
	</script>
	<ext:empty property="signTelegrams">
		<ext:empty property="returnTelegrams">
			<table border="0" cellspacing="1" width="360px" cellpadding="2">
				<tr>
					<td style="padding-left:15px;padding-top:10px; font-size:14px; line-height:20px">
						<div style="float:left"><img src="<%=request.getContextPath()%>/jeaf/form/img/warn.gif" align="absmiddle"></div>
						<div style="float:left;padding-top:3px;padding-left:5px">
							签收人：<ext:write property="signPersonName"/><ext:equal value="true" property="agent">(代签收)</ext:equal><br>
							所在单位：<ext:write property="signPersonOrgFullName"/><br>
							没有需要签收或清退的电报。
						</div>
					</td>
				</tr>
			</table>
		</ext:empty>
	</ext:empty>
	
	<ext:notEmpty property="signTelegrams">
		<b>签收人：</b><ext:write property="signPersonName"/><ext:equal value="true" property="agent">(代签收)</ext:equal>&nbsp;&nbsp;
		<b>所在单位：</b><ext:write property="signPersonOrgFullName"/>
		<br><br>
	</ext:notEmpty>
	<ext:empty property="signTelegrams">
		<ext:notEmpty property="returnTelegrams">
			<b>签收人：</b><ext:write property="signPersonName"/><ext:equal value="true" property="agent">(代签收)</ext:equal>&nbsp;&nbsp;
			<b>所在单位：</b><ext:write property="signPersonOrgFullName"/>
			<br><br>
		</ext:notEmpty>
	</ext:empty>

	<ext:notEmpty property="signTelegrams">
		<div style="padding-bottom: 3px">
			<b>待签收电报:</b>
			<input type="button" class="button" value="签收" style="width:36px" onclick="signTelegrams()">
		</div>
		<table onselectstart="return false;" style="border-collapse:collapse; table-layout:fixed" width="720px" border="1" cellpadding="0" cellspacing="0" bordercolor="#999999">
			<col valign="middle" align="center" width="32px">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="left" width="50%">
			<col valign="middle" align="left" width="50%">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="center" width="70px">
			<col valign="middle" align="center" width="60px">
			<tr align="center" valign="bottom">
				<td class="tdtitle" align="center">
					<input type="checkbox" class="checkbox" onclick="selectAllSignTelegrams(checked)">
				</td>
				<td class="tdtitle" align="center">榕机密收</td>
				<td class="tdtitle" align="center">标题</td>
				<td class="tdtitle" align="center">摘要</td>
				<td class="tdtitle" align="center">部委号</td>
				<td class="tdtitle" align="center">类别</td>
				<td class="tdtitle" align="center">登记时间</td>
				<td class="tdtitle" align="center">接收人</td>
			</tr>
			<ext:iterate id="telegramSign" indexId="signIndex" property="signTelegrams">
				<tr>
					<td class="tdcontent">
						<input name="selectSignTelegram" type="checkbox" class="checkbox" value="<ext:write name="telegramSign" property="id"/>">
					</td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.sequence"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.subject"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.summary" maxCharCount="60" ellipsis="..."/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.unitCode"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.category"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.created" format="yyyy-MM-dd"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="receiverName"/></td>
				</tr>
			</ext:iterate>
		</table>
		<br>
	</ext:notEmpty>
	
	<ext:notEmpty property="returnTelegrams">
		<div style="padding-bottom: 3px">
			<b>待清退电报:</b>
			<input type="button" class="button" value="清退" style="width:36px" onclick="returnTelegrams()">
		</div>
		<table onselectstart="return false;" style="border-collapse:collapse; table-layout:fixed" width="720px" border="1" cellpadding="0" cellspacing="0" bordercolor="#999999">
			<col valign="middle" align="center" width="32px">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="left" width="50%">
			<col valign="middle" align="left" width="50%">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="center" width="60px">
			<col valign="middle" align="center" width="70px">
			<col valign="middle" align="center" width="60px">
			<tr align="center" valign="bottom">
				<td class="tdtitle" align="center">
					<input type="checkbox" class="checkbox" onclick="selectAllReturnTelegrams(checked)">
				</td>
				<td class="tdtitle" align="center">榕机密收</td>
				<td class="tdtitle" align="center">标题</td>
				<td class="tdtitle" align="center">摘要</td>
				<td class="tdtitle" align="center">部委号</td>
				<td class="tdtitle" align="center">类别</td>
				<td class="tdtitle" align="center">签收时间</td>
				<td class="tdtitle" align="center">接收人</td>
			</tr>
			<ext:iterate id="telegramSign" indexId="telegramSignIndex" property="returnTelegrams">
				<tr>
					<td class="tdcontent">
						<input name="selectReturnTelegram" type="checkbox" class="checkbox" value="<ext:write name="telegramSign" property="id"/>">
					</td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.sequence"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.subject"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.summary"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.unitCode"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="telegram.category"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="signTime" format="yyyy-MM-dd"/></td>
					<td class="tdcontent"><ext:write name="telegramSign" property="receiverName"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:notEmpty>
</ext:form>