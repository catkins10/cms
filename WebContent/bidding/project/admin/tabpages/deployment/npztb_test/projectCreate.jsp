<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onPledgeBankChoosed() {
		var pledgeBankChoose = document.getElementsByName('pledgeBankChoose')[0].value;
		if(pledgeBankChoose=='') {
			return;
		}
		var pledgeBankChoosTable = document.getElementById('pledgeBankChoosTable');
		for(var i=2; i<pledgeBankChoosTable.rows[0].cells.length; i++) {
			pledgeBankChoosTable.rows[0].cells[i].style.visibility = pledgeBankChoose=='other' ? 'visible' : 'hidden';
		}
		if(pledgeBankChoose!='other') {
			var values = pledgeBankChoose.split(',');
			document.getElementsByName('pledgeBank')[0].value = values[0];
			document.getElementsByName('pledgeAccountName')[0].value = values[1];
			document.getElementsByName('pledgeAccount')[0].value = values[2];
		}
	}
</script>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/bidding/enterprise/js/enterprise.js"></script>
<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<ext:equal value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">项目名称</td>
			<td class="tdcontent"><ext:field property="projectName"/></td>
			<td class="tdtitle" nowrap="nowrap">工程类别</td>
			<td class="tdcontent"><ext:field property="projectCategory"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">招标内容</td>
			<td class="tdcontent"><ext:field property="projectProcedure"/></td>
			<td class="tdtitle" nowrap="nowrap">所属地区</td>
			<td class="tdcontent"><ext:field property="city"/></td>
		</tr>
	</ext:equal>
	<ext:notEqual value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">名称</td>
			<td class="tdcontent"><ext:field property="projectName"/></td>
			<td class="tdtitle" nowrap="nowrap">工程类别</td>
			<td class="tdcontent"><ext:field writeonly="true" property="projectCategory"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">招标内容</td>
			<td class="tdcontent"><ext:field writeonly="true" property="projectProcedure"/></td>
			<td class="tdtitle" nowrap="nowrap">所属地区</td>
			<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
		</tr>
	</ext:notEqual>
	<tr>
		<td class="tdtitle" nowrap="nowrap">招标方式</td>
		<td class="tdcontent"><ext:field property="biddingMode"/></td>
		<td class="tdtitle" nowrap="nowrap">是否委托招标代理</td>
		<td class="tdcontent"><ext:field property="agentEnable" onclick="onOptionChange()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">资格审查方式</td>
		<td class="tdcontent"><ext:field property="approvalMode"/></td>
		<td class="tdtitle" nowrap="nowrap">代理产生方式</td>
		<td class="tdcontent"><ext:field property="agentMode" onclick="onOptionChange()"/></td>
	</tr>
	<tr id="agentRow1">
		<td class="tdtitle" nowrap="nowrap">指定代理</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentName"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentRepresentative"/></td>
	</tr>
	<tr id="agentRow2">
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="biddingAgent.agentTel"/></td>
	</tr>
	<tr id="agentDrawRow1">
		<td class="tdtitle" nowrap="nowrap">代理内容</td>
		<td class="tdcontent"><ext:field property="agentDraw.content"/></td>
		<td class="tdtitle" nowrap="nowrap">代理机构报名时间</td>
		<td class="tdcontent">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%"><ext:field property="agentDraw.agentSignup"/></td>
					<td nowrap="nowrap">&nbsp;至&nbsp;</td>
					<td width="50%"><ext:field property="agentDraw.agentSignupEnd"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="agentDrawRow2">
		<td class="tdtitle" nowrap="nowrap">代理抽选时间</td>
		<td class="tdcontent"><ext:field property="agentDraw.drawTime"/></td>
		<td class="tdtitle" nowrap="nowrap">代理抽选公示期(天)</td>
		<td class="tdcontent"><ext:field property="agentDraw.publicLimit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设单位</td>
		<td class="tdcontent" colspan="3"><ext:field property="owner"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目人员需求</td>
		<td class="tdcontent" colspan="3"><jsp:include page="projectJobholder.jsp" flush="true"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="ownerLinkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="ownerTel"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">单位性质</td>
		<td class="tdcontent"><ext:field property="ownerType"/></td>
		<td class="tdtitle" nowrap="nowrap">法人代表</td>
		<td class="tdcontent"><ext:field property="ownerRepresentative"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">联系人身份证</td>
		<td class="tdcontent"><ext:field property="ownerLinkmanIdCard"/></td>
	</tr>
	<tr style="display:none">
		<td class="tdtitle" nowrap="nowrap">传真</td>
		<td class="tdcontent"><ext:field property="ownerFax"/></td>
		<td class="tdtitle" nowrap="nowrap">电子邮件</td>
		<td class="tdcontent"><ext:field property="ownerMail"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">建设地点</td>
		<td class="tdcontent"><ext:field property="projectAddress"/></td>
		<td class="tdtitle" nowrap="nowrap">建设规模</td>
		<td class="tdcontent"><ext:field property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">建筑面积(㎡)</td>
		<td class="tdcontent"><ext:field property="area"/></td>
		<td class="tdtitle" nowrap="nowrap">招标编号</td>
		<td class="tdcontent"><ext:field property="projectNumber"/></td>
	</tr>
	<ext:empty property="pledgeBank">
		<tr>
			<td class="tdtitle" nowrap="nowrap">保证金开户行</td>
			<td class="tdcontent" colspan="3">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" id="pledgeBankChoosTable">
					<tr>
						<td width="25%"><ext:field property="pledgeBankChoose" onchange="onPledgeBankChoosed()"/></td>
						<td nowrap="nowrap">&nbsp;</td>
						<td width="25%" style="visibility:hidden"><ext:field property="pledgeBank"/></td>
						<td nowrap="nowrap" style="visibility:hidden">&nbsp;帐户名：</td>
						<td width="25%" style="visibility:hidden"><ext:field property="pledgeAccountName"/></td>
						<td nowrap="nowrap" style="visibility:hidden">&nbsp;帐号：</td>
						<td width="25%" style="visibility:hidden"><ext:field property="pledgeAccount"/></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">备注</td>
			<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
		</tr>
	</ext:empty>
	<ext:notEmpty property="pledgeBank">
		<tr>
			<td class="tdtitle" nowrap="nowrap">保证金开户行</td>
			<td class="tdcontent"><ext:field property="pledgeBank"/></td>
			<td class="tdtitle" nowrap="nowrap">保证金帐户名</td>
			<td class="tdcontent"><ext:field property="pledgeAccountName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">保证金帐号</td>
			<td class="tdcontent"><ext:field property="pledgeAccount"/></td>
			<td class="tdtitle" nowrap="nowrap">备注</td>
			<td class="tdcontent"><ext:field property="remark"/></td>
		</tr>
	</ext:notEmpty>
</table>

<script>
function onAgentEnable() {
	document.getElementsByName('owner')[0].readOnly = document.getElementsByName('agentEnable')[1].checked;
}
function selectOwner() {
	if(document.getElementsByName('agentEnable')[0].checked) { //委托代理招标
		DialogUtils.openSelectDialog('bidding/project', 'projectOwner', 600, 400, false, 'owner{owner},ownerType{ownerType},ownerRepresentative{ownerRepresentative},ownerLinkman{ownerLinkman},ownerLinkmanIdCard{ownerLinkmanIdCard},ownerTel{ownerTel},ownerFax{ownerFax},ownerMail{ownerMail}', '', '', '', ',', true);
	}
	else { //自主招标
		selectEnterprise(600, 400, false, 'ownerId{id},owner{name},ownerType{kind},ownerRepresentative{legalRepresentative},ownerLinkman{linkman},ownerLinkmanIdCard{linkmanIdNumber},ownerTel{tel}');
	}
}
function onAgentModeChange() {
	var displayAgentRow = document.getElementsByName('agentMode')[0].checked;
	for(var i=1; i<=2; i++) {
		document.getElementById('agentRow' + i).style.display = displayAgentRow ? '' : 'none';
		document.getElementById('agentDrawRow' + i).style.display = displayAgentRow ? 'none' : '';
	}
}
function onOptionChange() {
	var agentEnable = document.getElementsByName('agentEnable')[0].checked;
	document.getElementsByName('owner')[0].readOnly = !agentEnable;
	//document.getElementsByName('agentMode')[0].disabled = !agentEnable;
	//document.getElementsByName('agentMode')[1].disabled = !agentEnable;
	var agentDraw = document.getElementsByName('agentMode')[0].checked;
	document.getElementById('agentRow1').style.display = !agentDraw && agentEnable ? '' : 'none';
	document.getElementById('agentRow2').style.display = !agentDraw && agentEnable ? '' : 'none';
	document.getElementById('agentDrawRow1').style.display = agentDraw && agentEnable ? '' : 'none';
	document.getElementById('agentDrawRow2').style.display = agentDraw && agentEnable ? '' : 'none';
}
onOptionChange();
</script>